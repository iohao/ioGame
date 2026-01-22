package com.iohao.game.external.core.ratelimit;

import com.iohao.game.external.core.config.ExternalGlobalConfig;
import com.iohao.game.external.core.monitor.SystemResourceMonitor;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 基于Netflix Gradient算法的自适应限流器
 * 通过计算无负载时的RTT与当前RTT的比值来判断是否出现请求排队
 * 
 * 参考：https://gummary.github.io/post/netfix%E8%87%AA%E9%80%82%E5%BA%94%E9%99%90%E6%B5%81%E7%AE%97%E6%B3%95/
 */
@Slf4j
public class AdaptiveRateLimiter {
    
    private static final AdaptiveRateLimiter INSTANCE = new AdaptiveRateLimiter();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    
    /** 当前限流阈值 */
    private final AtomicInteger currentLimit = new AtomicInteger();
    
    /** 当前连接数 */
    private final AtomicInteger currentConnections = new AtomicInteger(0);
    
    /** 被拒绝的连接数 */
    private final AtomicLong rejectedConnections = new AtomicLong(0);
    
    /** 总连接请求数 */
    private final AtomicLong totalRequests = new AtomicLong(0);
    
    /** 限流统计信息 */
    private final AtomicReference<RateLimitStats> stats = new AtomicReference<>();
    
    /** 是否已启动 */
    private volatile boolean started = false;
    
    /** 系统资源监控器 */
    private final SystemResourceMonitor resourceMonitor = SystemResourceMonitor.getInstance();
    
    /** RTT相关参数 */
    private volatile double rttNoLoad = 0.0;           // 无负载时的RTT
    private volatile double rttActual = 0.0;           // 当前实际RTT
    private volatile long lastUpdateTime = 0L;         // 上次更新时间
    
    /** 自适应参数 */
    private static final double MIN_GRADIENT = 0.5;    // 最小梯度值
    private static final double MAX_GRADIENT = 1.0;    // 最大梯度值
    private static final double ALPHA = 0.1;           // 平滑因子
    private static final int MIN_SAMPLE_SIZE = 10;     // 最小样本数
    
    /** 样本统计 */
    private final AtomicLong sampleCount = new AtomicLong(0);
    private final AtomicLong totalRtt = new AtomicLong(0);
    
    private AdaptiveRateLimiter() {
        int maxConnections = ExternalGlobalConfig.TrafficProtectionOption.maxConnections;
        this.currentLimit.set(maxConnections);
        this.stats.set(new RateLimitStats());
        
        // 初始化RTT值
        this.rttNoLoad = 1.0;  // 默认1ms
        this.rttActual = 1.0;
    }
    
    public static AdaptiveRateLimiter getInstance() {
        return INSTANCE;
    }
    
    public void start() {
        if (started) return;
        
        if (!ExternalGlobalConfig.TrafficProtectionOption.enableAdaptiveRateLimit) {
            log.info("自适应限流已禁用");
            return;
        }
        
        resourceMonitor.start();
        adjustLimit();
        
        int interval = ExternalGlobalConfig.TrafficProtectionOption.monitorInterval;
        scheduler.scheduleAtFixedRate(this::adjustLimit, interval, interval, TimeUnit.MILLISECONDS);
        
        started = true;
        log.info("Gradient自适应限流器已启动，监控间隔: {}ms", interval);
    }
    
    public void stop() {
        if (!started) return;
        
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        resourceMonitor.stop();
        started = false;
        log.info("Gradient自适应限流器已停止");
    }
    
    public boolean tryAcquire() {
        totalRequests.incrementAndGet();
        
        int limit = currentLimit.get();
        int connections = currentConnections.get();
        
        if (connections >= limit) {
            rejectedConnections.incrementAndGet();
            return false;
        }
        
        return true;
    }
    
    public void incrementConnections() {
        currentConnections.incrementAndGet();
    }
    
    public void decrementConnections() {
        currentConnections.decrementAndGet();
    }
    
    /**
     * 记录RTT样本
     * 
     * @param rtt 响应时间（毫秒）
     */
    public void recordRtt(double rtt) {
        if (rtt <= 0) return;
        
        sampleCount.incrementAndGet();
        totalRtt.addAndGet((long) (rtt * 1000)); // 转换为微秒
        
        // 更新实际RTT（使用指数移动平均）
        updateRtt(rtt);
    }
    
    public int getCurrentConnections() {
        return currentConnections.get();
    }
    
    public int getCurrentLimit() {
        return currentLimit.get();
    }
    
    public RateLimitStats getStats() {
        RateLimitStats currentStats = stats.get();
        currentStats.setCurrentConnections(currentConnections.get());
        currentStats.setCurrentLimit(currentLimit.get());
        currentStats.setRejectedConnections(rejectedConnections.get());
        currentStats.setTotalRequests(totalRequests.get());
        currentStats.setRttNoLoad(rttNoLoad);
        currentStats.setRttActual(rttActual);
        currentStats.setGradient(calculateGradient());
        currentStats.setQueueSize(calculateQueueSize());
        currentStats.setTimestamp(System.currentTimeMillis());
        return currentStats;
    }
    
    /**
     * 基于Gradient算法调整限流阈值
     */
    private void adjustLimit() {
        try {
            // 获取系统资源信息
            SystemResourceMonitor.SystemResourceInfo resourceInfo = resourceMonitor.getCurrentInfo();
            
            // 获取配置参数
            int maxConnections = ExternalGlobalConfig.TrafficProtectionOption.maxConnections;
            int minThreshold = ExternalGlobalConfig.TrafficProtectionOption.minRateLimitThreshold;
            int maxThreshold = ExternalGlobalConfig.TrafficProtectionOption.maxRateLimitThreshold;
            
            // 计算新的阈值
            int newLimit = calculateNewLimit(resourceInfo, maxConnections, minThreshold, maxThreshold);
            
            // 更新阈值
            int oldLimit = currentLimit.get();
            if (newLimit != oldLimit) {
                currentLimit.set(newLimit);
                log.info("限流阈值已调整: {} -> {} (Gradient: {:.3f}, RTT比值: {:.3f}/{:.3f})", 
                        oldLimit, newLimit, calculateGradient(), rttNoLoad, rttActual);
            }
            
            // 更新统计信息
            updateStats(resourceInfo);
            
        } catch (Exception e) {
            log.error("调整限流阈值失败", e);
        }
    }
    
    /**
     * 计算新的限流阈值
     * 基于Gradient算法：newLimit = currentLimit × gradient + queueSize
     */
    private int calculateNewLimit(
            SystemResourceMonitor.SystemResourceInfo resourceInfo,
            int maxConnections, int minThreshold, int maxThreshold) {
        
        int currentLimit = this.currentLimit.get();
        
        // 计算Gradient值
        double gradient = calculateGradient();
        
        // 计算队列大小（允许一定的排队）
        int queueSize = calculateQueueSize();
        
        // 应用Gradient算法
        double newLimitDouble = currentLimit * gradient + queueSize;
        int newLimit = (int) Math.round(newLimitDouble);
        
        // 限制在有效范围内
        newLimit = Math.max(minThreshold, Math.min(maxThreshold, newLimit));
        
        // 考虑系统资源限制
        newLimit = applyResourceConstraints(newLimit, resourceInfo);
        
        return newLimit;
    }
    
    /**
     * 计算Gradient值
     * gradient = RTTNoLoad / RTTactual
     * 当gradient = 1时，说明当前请求没有排队
     * 当gradient < 1时，说明当前开始排队了，需要降低limit
     */
    private double calculateGradient() {
        if (rttActual <= 0) return MAX_GRADIENT;
        
        double gradient = rttNoLoad / rttActual;
        
        // 限制在有效范围内
        return Math.max(MIN_GRADIENT, Math.min(MAX_GRADIENT, gradient));
    }
    
    /**
     * 计算队列大小
     * 一般设置为当前limit的平方根，对于小的limit增长很快，大的limit变动平稳
     */
    private int calculateQueueSize() {
        int currentLimit = this.currentLimit.get();
        return (int) Math.sqrt(currentLimit);
    }
    
    /**
     * 应用系统资源约束
     */
    private int applyResourceConstraints(int newLimit, SystemResourceMonitor.SystemResourceInfo resourceInfo) {
        double cpuUsage = resourceInfo.getCpuUsage();
        double memoryUsage = resourceInfo.getMemoryUsage();
        
        double cpuThreshold = ExternalGlobalConfig.TrafficProtectionOption.cpuThreshold;
        double memoryThreshold = ExternalGlobalConfig.TrafficProtectionOption.memoryThreshold;
        
        // 如果CPU或内存使用率过高，减少limit
        if (cpuUsage > cpuThreshold || memoryUsage > memoryThreshold) {
            double reductionFactor = 0.8; // 减少20%
            newLimit = (int) (newLimit * reductionFactor);
        }
        
        return newLimit;
    }
    
    /**
     * 更新RTT值
     * 使用指数移动平均来平滑RTT变化
     */
    private void updateRtt(double rtt) {
        long currentTime = System.currentTimeMillis();
        
        // 更新实际RTT
        if (rttActual == 0) {
            rttActual = rtt;
        } else {
            rttActual = ALPHA * rtt + (1 - ALPHA) * rttActual;
        }
        
        // 更新无负载RTT（取最小值）
        if (rttNoLoad == 0 || rtt < rttNoLoad) {
            rttNoLoad = rtt;
        }
        
        // 定期更新无负载RTT（防止长期不更新）
        if (currentTime - lastUpdateTime > 60000) { // 1分钟
            updateNoLoadRtt();
            lastUpdateTime = currentTime;
        }
    }
    
    /**
     * 更新无负载RTT
     * 使用最近一段时间的最小RTT作为无负载RTT
     */
    private void updateNoLoadRtt() {
        if (sampleCount.get() < MIN_SAMPLE_SIZE) return;
        
        // 计算平均RTT
        double avgRtt = (double) totalRtt.get() / sampleCount.get() / 1000.0; // 转换回毫秒
        
        // 如果当前RTT接近平均RTT，说明系统负载较低，可以更新无负载RTT
        if (rttActual < avgRtt * 1.2) {
            rttNoLoad = Math.min(rttNoLoad, rttActual);
        }
        
        // 重置统计
        sampleCount.set(0);
        totalRtt.set(0);
    }
    
    private void updateStats(SystemResourceMonitor.SystemResourceInfo resourceInfo) {
        RateLimitStats currentStats = stats.get();
        currentStats.setCpuUsage(resourceInfo.getCpuUsage());
        currentStats.setMemoryUsage(resourceInfo.getMemoryUsage());
        currentStats.setSystemLoad(resourceInfo.getSystemLoad());
        currentStats.setJvmMemoryUsage(resourceInfo.getJvmMemoryUsage());
    }
    
    @Getter
    @Accessors(chain = true)
    public static class RateLimitStats {
        private int currentConnections = 0;
        private int currentLimit = 0;
        private long rejectedConnections = 0;
        private long totalRequests = 0;
        private double cpuUsage = 0.0;
        private double memoryUsage = 0.0;
        private double systemLoad = 0.0;
        private double jvmMemoryUsage = 0.0;
        private double rttNoLoad = 0.0;
        private double rttActual = 0.0;
        private double gradient = 0.0;
        private int queueSize = 0;
        private long timestamp = 0L;
        
        public RateLimitStats setCurrentConnections(int currentConnections) {
            this.currentConnections = currentConnections;
            return this;
        }
        
        public RateLimitStats setCurrentLimit(int currentLimit) {
            this.currentLimit = currentLimit;
            return this;
        }
        
        public RateLimitStats setRejectedConnections(long rejectedConnections) {
            this.rejectedConnections = rejectedConnections;
            return this;
        }
        
        public RateLimitStats setTotalRequests(long totalRequests) {
            this.totalRequests = totalRequests;
            return this;
        }
        
        public RateLimitStats setCpuUsage(double cpuUsage) {
            this.cpuUsage = cpuUsage;
            return this;
        }
        
        public RateLimitStats setMemoryUsage(double memoryUsage) {
            this.memoryUsage = memoryUsage;
            return this;
        }
        
        public RateLimitStats setSystemLoad(double systemLoad) {
            this.systemLoad = systemLoad;
            return this;
        }
        
        public RateLimitStats setJvmMemoryUsage(double jvmMemoryUsage) {
            this.jvmMemoryUsage = jvmMemoryUsage;
            return this;
        }
        
        public RateLimitStats setRttNoLoad(double rttNoLoad) {
            this.rttNoLoad = rttNoLoad;
            return this;
        }
        
        public RateLimitStats setRttActual(double rttActual) {
            this.rttActual = rttActual;
            return this;
        }
        
        public RateLimitStats setGradient(double gradient) {
            this.gradient = gradient;
            return this;
        }
        
        public RateLimitStats setQueueSize(int queueSize) {
            this.queueSize = queueSize;
            return this;
        }
        
        public RateLimitStats setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public double getRejectionRate() {
            if (totalRequests == 0) return 0.0;
            return ((double) rejectedConnections / totalRequests) * 100.0;
        }
        
        public double getConnectionRate() {
            if (currentLimit == 0) return 0.0;
            return ((double) currentConnections / currentLimit) * 100.0;
        }
        
        @Override
        public String toString() {
            return String.format(
                    "RateLimitStats{currentConnections=%d, currentLimit=%d, " +
                    "rejectedConnections=%d, totalRequests=%d, rejectionRate=%.2f%%, " +
                    "connectionRate=%.2f%%, gradient=%.3f, rttNoLoad=%.3f, rttActual=%.3f, " +
                    "queueSize=%d, cpuUsage=%.2f%%, memoryUsage=%.2f%%, " +
                    "systemLoad=%.2f, jvmMemoryUsage=%.2f%%, timestamp=%d}",
                    currentConnections, currentLimit, rejectedConnections, totalRequests,
                    getRejectionRate(), getConnectionRate(), gradient, rttNoLoad, rttActual,
                    queueSize, cpuUsage, memoryUsage, systemLoad, jvmMemoryUsage, timestamp
            );
        }
    }
}

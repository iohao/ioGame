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
 * 限流器抽象基类
 * 提供通用的实现逻辑，子类只需要实现具体的限流算法
 * @author undertaker86001
 * @date 2025-08-15
 */
@Slf4j
public abstract class AbstractRateLimiter implements RateLimiter {
    
    protected final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    
    /** 当前限流阈值 */
    protected final AtomicInteger currentLimit = new AtomicInteger();
    
    /** 当前连接数 */
    protected final AtomicInteger currentConnections = new AtomicInteger(0);
    
    /** 被拒绝的连接数 */
    protected final AtomicLong rejectedConnections = new AtomicLong(0);
    
    /** 总连接请求数 */
    protected final AtomicLong totalRequests = new AtomicLong(0);
    
    /** 限流统计信息 */
    protected final AtomicReference<BaseRateLimitStats> stats = new AtomicReference<>();
    
    /** 是否已启动 */
    protected volatile boolean started = false;
    
    /** 系统资源监控器 */
    protected final SystemResourceMonitor resourceMonitor = SystemResourceMonitor.getInstance();
    
    /** 样本统计 */
    protected final AtomicLong sampleCount = new AtomicLong(0);
    protected final AtomicLong totalRtt = new AtomicLong(0);
    
    protected AbstractRateLimiter() {
        int maxConnections = ExternalGlobalConfig.TrafficProtectionOption.maxConnections;
        this.currentLimit.set(maxConnections);
        this.stats.set(new BaseRateLimitStats());
    }
    
    @Override
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
        log.info("{} 自适应限流器已启动，监控间隔: {}ms", getAlgorithmName(), interval);
    }
    
    @Override
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
        log.info("{} 自适应限流器已停止", getAlgorithmName());
    }
    
    @Override
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
    
    @Override
    public void incrementConnections() {
        currentConnections.incrementAndGet();
    }
    
    @Override
    public void decrementConnections() {
        currentConnections.decrementAndGet();
    }
    
    @Override
    public int getCurrentConnections() {
        return currentConnections.get();
    }
    
    @Override
    public int getCurrentLimit() {
        return currentLimit.get();
    }
    
    @Override
    public void recordRtt(double rtt) {
        if (rtt <= 0) return;
        
        sampleCount.incrementAndGet();
        totalRtt.addAndGet((long) (rtt * 1000)); // 转换为微秒
        
        // 子类实现具体的RTT更新逻辑
        updateRtt(rtt);
    }
    
    @Override
    public RateLimitStats getStats() {
        BaseRateLimitStats currentStats = stats.get();
        currentStats.setCurrentConnections(currentConnections.get());
        currentStats.setCurrentLimit(currentLimit.get());
        currentStats.setRejectedConnections(rejectedConnections.get());
        currentStats.setTotalRequests(totalRequests.get());
        currentStats.setTimestamp(System.currentTimeMillis());
        
        // 子类实现具体的统计信息更新
        updateStats(currentStats);
        
        return currentStats;
    }
    
    /**
     * 获取算法名称
     */
    protected abstract String getAlgorithmName();
    
    /**
     * 调整限流阈值
     * 子类实现具体的限流算法
     */
    protected abstract void adjustLimit();
    
    /**
     * 更新RTT值
     * 子类实现具体的RTT更新逻辑
     */
    protected abstract void updateRtt(double rtt);
    
    /**
     * 更新统计信息
     * 子类实现具体的统计信息更新
     */
    protected abstract void updateStats(BaseRateLimitStats stats);
    
    /**
     * 应用系统资源约束
     */
    protected int applyResourceConstraints(int newLimit, SystemResourceMonitor.SystemResourceInfo resourceInfo) {
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
     * 基础限流统计信息
     */
    @Getter
    @Accessors(chain = true)
    protected static class BaseRateLimitStats implements RateLimitStats {
        private int currentConnections = 0;
        private int currentLimit = 0;
        private long rejectedConnections = 0;
        private long totalRequests = 0;
        private double cpuUsage = 0.0;
        private double memoryUsage = 0.0;
        private double systemLoad = 0.0;
        private double jvmMemoryUsage = 0.0;
        private long timestamp = 0L;
        
        public BaseRateLimitStats setCurrentConnections(int currentConnections) {
            this.currentConnections = currentConnections;
            return this;
        }
        
        public BaseRateLimitStats setCurrentLimit(int currentLimit) {
            this.currentLimit = currentLimit;
            return this;
        }
        
        public BaseRateLimitStats setRejectedConnections(long rejectedConnections) {
            this.rejectedConnections = rejectedConnections;
            return this;
        }
        
        public BaseRateLimitStats setTotalRequests(long totalRequests) {
            this.totalRequests = totalRequests;
            return this;
        }
        
        public BaseRateLimitStats setCpuUsage(double cpuUsage) {
            this.cpuUsage = cpuUsage;
            return this;
        }
        
        public BaseRateLimitStats setMemoryUsage(double memoryUsage) {
            this.memoryUsage = memoryUsage;
            return this;
        }
        
        public BaseRateLimitStats setSystemLoad(double systemLoad) {
            this.systemLoad = systemLoad;
            return this;
        }
        
        public BaseRateLimitStats setJvmMemoryUsage(double jvmMemoryUsage) {
            this.jvmMemoryUsage = jvmMemoryUsage;
            return this;
        }
        
        public BaseRateLimitStats setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        @Override
        public double getRejectionRate() {
            if (totalRequests == 0) return 0.0;
            return ((double) rejectedConnections / totalRequests) * 100.0;
        }
        
        @Override
        public double getConnectionRate() {
            if (currentLimit == 0) return 0.0;
            return ((double) currentConnections / currentLimit) * 100.0;
        }
    }
}

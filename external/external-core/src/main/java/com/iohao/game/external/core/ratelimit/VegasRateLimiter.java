package com.iohao.game.external.core.ratelimit;

import com.iohao.game.external.core.config.ExternalGlobalConfig;
import com.iohao.game.external.core.monitor.SystemResourceMonitor;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * Vegas限流算法实现
 * 启发自TCP的Vegas拥塞避免算法，根据RTT来判断网络是否出现拥塞
 */
@Slf4j
public class VegasRateLimiter extends AbstractRateLimiter {
    
    private static final VegasRateLimiter INSTANCE = new VegasRateLimiter();
    
    /** RTT相关参数 */
    private volatile double rttNoLoad = 0.0;           // 无负载时的RTT
    private volatile double rttActual = 0.0;           // 当前实际RTT
    private volatile long lastUpdateTime = 0L;         // 上次更新时间
    
    /** Vegas算法参数 */
    private static final double ALPHA = 0.1;           // 平滑因子
    private static final int MIN_SAMPLE_SIZE = 10;     // 最小样本数
    
    private VegasRateLimiter() {
        this.rttNoLoad = 1.0;
        this.rttActual = 1.0;
    }
    
    public static VegasRateLimiter getInstance() {
        return INSTANCE;
    }
    
    @Override
    protected String getAlgorithmName() {
        return "Vegas";
    }
    
    @Override
    protected void adjustLimit() {
        try {
            SystemResourceMonitor.SystemResourceInfo resourceInfo = resourceMonitor.getCurrentInfo();
            
            int maxConnections = ExternalGlobalConfig.TrafficProtectionOption.maxConnections;
            int minThreshold = ExternalGlobalConfig.TrafficProtectionOption.minRateLimitThreshold;
            int maxThreshold = ExternalGlobalConfig.TrafficProtectionOption.maxRateLimitThreshold;
            
            int newLimit = calculateNewLimit(resourceInfo, maxConnections, minThreshold, maxThreshold);
            
            int oldLimit = currentLimit.get();
            if (newLimit != oldLimit) {
                currentLimit.set(newLimit);
                log.info("限流阈值已调整: {} -> {} (QueueSize: {}, RTT比值: {:.3f}/{:.3f})", 
                        oldLimit, newLimit, calculateQueueSize(), rttNoLoad, rttActual);
            }
            
        } catch (Exception e) {
            log.error("调整限流阈值失败", e);
        }
    }
    
    /**
     * 基于Vegas算法计算新的限流阈值
     * 直接使用等待队列的大小queueSize进行判断是否限流
     */
    private int calculateNewLimit(
            SystemResourceMonitor.SystemResourceInfo resourceInfo,
            int maxConnections, int minThreshold, int maxThreshold) {
        
        int currentLimit = this.currentLimit.get();
        int queueSize = calculateQueueSize();
        
        // 计算动态调整阈值
        double alpha = 3 * Math.log10(currentLimit);
        double beta = 6 * Math.log10(currentLimit);
        double threshold = Math.log10(currentLimit);
        
        int newLimit = currentLimit;
        
        // Vegas算法核心逻辑
        if (queueSize < threshold) {
            // 队列大小小于阈值，扩大窗口
            newLimit = currentLimit + (int) beta;
        } else if (queueSize < alpha) {
            // 阈值 < 队列大小 < alpha，适度增加
            newLimit = currentLimit + (int) Math.log10(currentLimit);
        } else if (queueSize > beta) {
            // 队列大小 > beta，缩小窗口
            newLimit = currentLimit - (int) Math.log10(currentLimit);
        }
        // 其他情况保持当前值
        
        // 限制在有效范围内
        newLimit = Math.max(minThreshold, Math.min(maxThreshold, newLimit));
        
        // 考虑系统资源限制
        newLimit = applyResourceConstraints(newLimit, resourceInfo);
        
        return newLimit;
    }
    
    /**
     * 计算队列大小
     * queueSize = limit × (1 - RttNoLoad / RTTactual)
     */
    private int calculateQueueSize() {
        if (rttActual <= 0) return 0;
        
        int currentLimit = this.currentLimit.get();
        double queueSize = currentLimit * (1 - rttNoLoad / rttActual);
        
        return Math.max(0, (int) Math.round(queueSize));
    }
    
    @Override
    protected void updateRtt(double rtt) {
        long currentTime = System.currentTimeMillis();
        
        // 更新实际RTT（使用指数移动平均）
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
        if (currentTime - lastUpdateTime > 60000) {
            updateNoLoadRtt();
            lastUpdateTime = currentTime;
        }
    }
    
    /**
     * 更新无负载RTT
     */
    private void updateNoLoadRtt() {
        if (sampleCount.get() < MIN_SAMPLE_SIZE) return;
        
        double avgRtt = (double) totalRtt.get() / sampleCount.get() / 1000.0;
        
        if (rttActual < avgRtt * 1.2) {
            rttNoLoad = Math.min(rttNoLoad, rttActual);
        }
        
        sampleCount.set(0);
        totalRtt.set(0);
    }
    
    @Override
    protected void updateStats(BaseRateLimitStats stats) {
        SystemResourceMonitor.SystemResourceInfo resourceInfo = resourceMonitor.getCurrentInfo();
        stats.setCpuUsage(resourceInfo.getCpuUsage());
        stats.setMemoryUsage(resourceInfo.getMemoryUsage());
        stats.setSystemLoad(resourceInfo.getSystemLoad());
        stats.setJvmMemoryUsage(resourceInfo.getJvmMemoryUsage());
        
        if (stats instanceof VegasRateLimitStats) {
            VegasRateLimitStats vegasStats = (VegasRateLimitStats) stats;
            vegasStats.setRttNoLoad(rttNoLoad);
            vegasStats.setRttActual(rttActual);
            vegasStats.setQueueSize(calculateQueueSize());
            vegasStats.setAlpha(3 * Math.log10(currentLimit.get()));
            vegasStats.setBeta(6 * Math.log10(currentLimit.get()));
            vegasStats.setThreshold(Math.log10(currentLimit.get()));
        }
    }
    
    @Override
    public RateLimitStats getStats() {
        VegasRateLimitStats currentStats = new VegasRateLimitStats();
        currentStats.setCurrentConnections(currentConnections.get());
        currentStats.setCurrentLimit(currentLimit.get());
        currentStats.setRejectedConnections(rejectedConnections.get());
        currentStats.setTotalRequests(totalRequests.get());
        currentStats.setTimestamp(System.currentTimeMillis());
        
        updateStats(currentStats);
        
        return currentStats;
    }
    
    /**
     * Vegas限流统计信息
     */
    @Getter
    @Accessors(chain = true)
    public static class VegasRateLimitStats extends BaseRateLimitStats {
        private double rttNoLoad = 0.0;
        private double rttActual = 0.0;
        private int queueSize = 0;
        private double alpha = 0.0;
        private double beta = 0.0;
        private double threshold = 0.0;
        
        public VegasRateLimitStats setRttNoLoad(double rttNoLoad) {
            this.rttNoLoad = rttNoLoad;
            return this;
        }
        
        public VegasRateLimitStats setRttActual(double rttActual) {
            this.rttActual = rttActual;
            return this;
        }
        
        public VegasRateLimitStats setQueueSize(int queueSize) {
            this.queueSize = queueSize;
            return this;
        }
        
        public VegasRateLimitStats setAlpha(double alpha) {
            this.alpha = alpha;
            return this;
        }
        
        public VegasRateLimitStats setBeta(double beta) {
            this.beta = beta;
            return this;
        }
        
        public VegasRateLimitStats setThreshold(double threshold) {
            this.threshold = threshold;
            return this;
        }
        
        @Override
        public String toString() {
            return String.format(
                    "VegasRateLimitStats{currentConnections=%d, currentLimit=%d, " +
                    "rejectedConnections=%d, totalRequests=%d, rejectionRate=%.2f%%, " +
                    "connectionRate=%.2f%%, queueSize=%d, alpha=%.3f, beta=%.3f, " +
                    "threshold=%.3f, rttNoLoad=%.3f, rttActual=%.3f, " +
                    "cpuUsage=%.2f%%, memoryUsage=%.2f%%, systemLoad=%.2f, " +
                    "jvmMemoryUsage=%.2f%%, timestamp=%d}",
                    getCurrentConnections(), getCurrentLimit(), getRejectedConnections(), getTotalRequests(),
                    getRejectionRate(), getConnectionRate(), queueSize, alpha, beta, threshold,
                    rttNoLoad, rttActual, getCpuUsage(), getMemoryUsage(), getSystemLoad(), 
                    getJvmMemoryUsage(), getTimestamp()
            );
        }
    }
}

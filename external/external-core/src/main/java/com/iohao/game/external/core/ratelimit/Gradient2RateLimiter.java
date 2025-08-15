package com.iohao.game.external.core.ratelimit;

import com.iohao.game.external.core.config.ExternalGlobalConfig;
import com.iohao.game.external.core.monitor.SystemResourceMonitor;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * Gradient2限流算法实现
 * 将noLoadRTT设置为RTT的指数移动平均，会随着RTT的增大而增大，缓解了过度保护的情况
 */
@Slf4j
public class Gradient2RateLimiter extends AbstractRateLimiter {
    
    private static final Gradient2RateLimiter INSTANCE = new Gradient2RateLimiter();
    
    /** RTT相关参数 */
    private volatile double rttNoLoad = 0.0;           // 无负载时的RTT
    private volatile double rttActual = 0.0;           // 当前实际RTT
    private volatile double longRtt = 0.0;             // 长期RTT（指数移动平均）
    private volatile long lastUpdateTime = 0L;         // 上次更新时间
    
    /** 自适应参数 */
    private static final double MIN_GRADIENT = 0.5;    // 最小梯度值
    private static final double MAX_GRADIENT = 1.0;    // 最大梯度值
    private static final double ALPHA = 0.1;           // 平滑因子
    private static final double WINDOW_SIZE = 100.0;   // 请求窗口大小
    private static final int MIN_SAMPLE_SIZE = 10;     // 最小样本数
    
    private Gradient2RateLimiter() {
        this.rttNoLoad = 1.0;
        this.rttActual = 1.0;
        this.longRtt = 1.0;
    }
    
    public static Gradient2RateLimiter getInstance() {
        return INSTANCE;
    }
    
    @Override
    protected String getAlgorithmName() {
        return "Gradient2";
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
                log.info("限流阈值已调整: {} -> {} (Gradient: {:.3f}, LongRTT: {:.3f}, RTT: {:.3f})", 
                        oldLimit, newLimit, calculateGradient(), longRtt, rttActual);
            }
            
        } catch (Exception e) {
            log.error("调整限流阈值失败", e);
        }
    }
    
    private int calculateNewLimit(
            SystemResourceMonitor.SystemResourceInfo resourceInfo,
            int maxConnections, int minThreshold, int maxThreshold) {
        
        int currentLimit = this.currentLimit.get();
        double gradient = calculateGradient();
        int queueSize = calculateQueueSize();
        
        double newLimitDouble = currentLimit * gradient + queueSize;
        int newLimit = (int) Math.round(newLimitDouble);
        
        newLimit = Math.max(minThreshold, Math.min(maxThreshold, newLimit));
        newLimit = applyResourceConstraints(newLimit, resourceInfo);
        
        return newLimit;
    }
    
    private double calculateGradient() {
        if (rttActual <= 0) return MAX_GRADIENT;
        
        double gradient = longRtt / rttActual;
        return Math.max(MIN_GRADIENT, Math.min(MAX_GRADIENT, gradient));
    }
    
    private int calculateQueueSize() {
        int currentLimit = this.currentLimit.get();
        return (int) Math.sqrt(currentLimit);
    }
    
    @Override
    protected void updateRtt(double rtt) {
        long currentTime = System.currentTimeMillis();
        
        if (rttActual == 0) {
            rttActual = rtt;
        } else {
            rttActual = ALPHA * rtt + (1 - ALPHA) * rttActual;
        }
        
        if (longRtt == 0) {
            longRtt = rtt;
        } else {
            longRtt = (1 - 1.0 / WINDOW_SIZE) * longRtt + (1.0 / WINDOW_SIZE) * rtt;
        }
        
        if (rttNoLoad == 0 || rtt < rttNoLoad) {
            rttNoLoad = rtt;
        }
        
        if (currentTime - lastUpdateTime > 60000) {
            updateNoLoadRtt();
            lastUpdateTime = currentTime;
        }
    }
    
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
        
        if (stats instanceof Gradient2RateLimitStats) {
            Gradient2RateLimitStats gradient2Stats = (Gradient2RateLimitStats) stats;
            gradient2Stats.setRttNoLoad(rttNoLoad);
            gradient2Stats.setRttActual(rttActual);
            gradient2Stats.setLongRtt(longRtt);
            gradient2Stats.setGradient(calculateGradient());
            gradient2Stats.setQueueSize(calculateQueueSize());
        }
    }
    
    @Override
    public RateLimitStats getStats() {
        Gradient2RateLimitStats currentStats = new Gradient2RateLimitStats();
        currentStats.setCurrentConnections(currentConnections.get());
        currentStats.setCurrentLimit(currentLimit.get());
        currentStats.setRejectedConnections(rejectedConnections.get());
        currentStats.setTotalRequests(totalRequests.get());
        currentStats.setTimestamp(System.currentTimeMillis());
        
        updateStats(currentStats);
        
        return currentStats;
    }
    
    @Getter
    @Accessors(chain = true)
    public static class Gradient2RateLimitStats extends BaseRateLimitStats {
        private double rttNoLoad = 0.0;
        private double rttActual = 0.0;
        private double longRtt = 0.0;
        private double gradient = 0.0;
        private int queueSize = 0;
        
        public Gradient2RateLimitStats setRttNoLoad(double rttNoLoad) {
            this.rttNoLoad = rttNoLoad;
            return this;
        }
        
        public Gradient2RateLimitStats setRttActual(double rttActual) {
            this.rttActual = rttActual;
            return this;
        }
        
        public Gradient2RateLimitStats setLongRtt(double longRtt) {
            this.longRtt = longRtt;
            return this;
        }
        
        public Gradient2RateLimitStats setGradient(double gradient) {
            this.gradient = gradient;
            return this;
        }
        
        public Gradient2RateLimitStats setQueueSize(int queueSize) {
            this.queueSize = queueSize;
            return this;
        }
        
        @Override
        public String toString() {
            return String.format(
                    "Gradient2RateLimitStats{currentConnections=%d, currentLimit=%d, " +
                    "rejectedConnections=%d, totalRequests=%d, rejectionRate=%.2f%%, " +
                    "connectionRate=%.2f%%, gradient=%.3f, rttNoLoad=%.3f, rttActual=%.3f, " +
                    "longRtt=%.3f, queueSize=%d, cpuUsage=%.2f%%, memoryUsage=%.2f%%, " +
                    "systemLoad=%.2f, jvmMemoryUsage=%.2f%%, timestamp=%d}",
                    getCurrentConnections(), getCurrentLimit(), getRejectedConnections(), getTotalRequests(),
                    getRejectionRate(), getConnectionRate(), gradient, rttNoLoad, rttActual,
                    longRtt, queueSize, getCpuUsage(), getMemoryUsage(), getSystemLoad(), getJvmMemoryUsage(), getTimestamp()
            );
        }
    }
}

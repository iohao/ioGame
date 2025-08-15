package com.iohao.game.external.core.ratelimit;

import com.iohao.game.external.core.monitor.SystemResourceMonitor;

/**
 * 限流器抽象接口
 * 为各种自适应限流算法提供统一的抽象
 * 
 * 参考：https://gummary.github.io/post/netfix%E8%87%AA%E9%80%82%E5%BA%94%E9%99%90%E6%B5%81%E7%AE%97%E6%B3%95/
 */
public interface RateLimiter {
    
    /**
     * 启动限流器
     */
    void start();
    
    /**
     * 停止限流器
     */
    void stop();
    
    /**
     * 尝试获取连接许可
     *
     * @return true 允许连接，false 拒绝连接
     */
    boolean tryAcquire();
    
    /**
     * 增加连接数
     */
    void incrementConnections();
    
    /**
     * 减少连接数
     */
    void decrementConnections();
    
    /**
     * 获取当前连接数
     *
     * @return 当前连接数
     */
    int getCurrentConnections();
    
    /**
     * 获取当前限流阈值
     *
     * @return 当前限流阈值
     */
    int getCurrentLimit();
    
    /**
     * 记录RTT样本
     *
     * @param rtt 响应时间（毫秒）
     */
    void recordRtt(double rtt);
    
    /**
     * 获取限流统计信息
     *
     * @return 限流统计信息
     */
    RateLimitStats getStats();
    
    /**
     * 限流统计信息接口
     */
    interface RateLimitStats {
        /**
         * 获取当前连接数
         */
        int getCurrentConnections();
        
        /**
         * 获取当前限流阈值
         */
        int getCurrentLimit();
        
        /**
         * 获取被拒绝的连接数
         */
        long getRejectedConnections();
        
        /**
         * 获取总连接请求数
         */
        long getTotalRequests();
        
        /**
         * 获取CPU使用率
         */
        double getCpuUsage();
        
        /**
         * 获取内存使用率
         */
        double getMemoryUsage();
        
        /**
         * 获取系统负载
         */
        double getSystemLoad();
        
        /**
         * 获取JVM内存使用率
         */
        double getJvmMemoryUsage();
        
        /**
         * 获取拒绝率
         */
        double getRejectionRate();
        
        /**
         * 获取连接率
         */
        double getConnectionRate();
        
        /**
         * 获取时间戳
         */
        long getTimestamp();
    }
    
    /**
     * 限流器工厂接口
     */
    interface Factory {
        /**
         * 创建限流器
         *
         * @param algorithm 算法类型
         * @return 限流器实例
         */
        RateLimiter createRateLimiter(Algorithm algorithm);
        
        /**
         * 算法类型枚举
         */
        enum Algorithm {
            /** Gradient算法 */
            GRADIENT,
            /** Gradient2算法 */
            GRADIENT2,
            /** Vegas算法 */
            VEGAS
        }
    }
}

package com.iohao.game.external.core.protection;

import com.iohao.game.external.core.config.ExternalGlobalConfig;
import com.iohao.game.external.core.ratelimit.AdaptiveRateLimiter;
import lombok.extern.slf4j.Slf4j;

/**
 * 流量保护器
 * 负责在连接建立时进行流量控制和保护
 * @author undertaker86001
 * @date 2025-08-15
 */
@Slf4j
public class TrafficProtector {
    
    private static final TrafficProtector INSTANCE = new TrafficProtector();
    private final AdaptiveRateLimiter rateLimiter = AdaptiveRateLimiter.getInstance();
    
    private TrafficProtector() {}
    
    public static TrafficProtector getInstance() {
        return INSTANCE;
    }
    
    /**
     * 检查是否允许新连接
     *
     * @param clientIp 客户端IP地址
     * @return 连接检查结果
     */
    public ConnectionCheckResult checkConnection(String clientIp) {
        // 检查是否启用流量保护
        if (!ExternalGlobalConfig.TrafficProtectionOption.enableTrafficProtection) {
            return ConnectionCheckResult.allowed();
        }
        
        try {
            // 尝试获取连接许可
            if (rateLimiter.tryAcquire()) {
                // 允许连接，增加连接数
                rateLimiter.incrementConnections();
                
                if (ExternalGlobalConfig.TrafficProtectionOption.enableRejectionLog) {
                    log.info("连接允许 - IP: {}, 当前连接数: {}, 阈值: {}", 
                            clientIp, rateLimiter.getCurrentConnections(), rateLimiter.getCurrentThreshold());
                }
                
                return ConnectionCheckResult.allowed();
            } else {
                // 拒绝连接
                if (ExternalGlobalConfig.TrafficProtectionOption.enableRejectionLog) {
                    log.warn("连接拒绝 - IP: {}, 当前连接数: {}, 阈值: {}, 原因: 连接数超限", 
                            clientIp, rateLimiter.getCurrentConnections(), rateLimiter.getCurrentThreshold());
                }
                
                return ConnectionCheckResult.rejected(ConnectionRejectionReason.CONNECTION_LIMIT_EXCEEDED);
            }
            
        } catch (Exception e) {
            log.error("流量保护检查失败 - IP: {}", clientIp, e);
            
            // 发生异常时，为了安全起见，拒绝连接
            return ConnectionCheckResult.rejected(ConnectionRejectionReason.SYSTEM_ERROR);
        }
    }
    
    /**
     * 连接断开时减少连接数
     */
    public void onConnectionClosed() {
        if (ExternalGlobalConfig.TrafficProtectionOption.enableTrafficProtection) {
            rateLimiter.decrementConnections();
        }
    }
    
    /**
     * 获取当前连接数
     *
     * @return 当前连接数
     */
    public int getCurrentConnections() {
        return rateLimiter.getCurrentConnections();
    }
    
    /**
     * 获取当前限流阈值
     *
     * @return 当前限流阈值
     */
    public int getCurrentThreshold() {
        return rateLimiter.getCurrentThreshold();
    }
    
    /**
     * 获取限流统计信息
     *
     * @return 限流统计信息
     */
    public AdaptiveRateLimiter.RateLimitStats getRateLimitStats() {
        return rateLimiter.getStats();
    }
    
    /**
     * 启动流量保护器
     */
    public void start() {
        if (ExternalGlobalConfig.TrafficProtectionOption.enableTrafficProtection) {
            rateLimiter.start();
            log.info("流量保护器已启动");
        }
    }
    
    /**
     * 停止流量保护器
     */
    public void stop() {
        if (ExternalGlobalConfig.TrafficProtectionOption.enableTrafficProtection) {
            rateLimiter.stop();
            log.info("流量保护器已停止");
        }
    }
    
    /**
     * 连接检查结果
     */
    public static class ConnectionCheckResult {
        private final boolean allowed;
        private final ConnectionRejectionReason rejectionReason;
        private final String errorMessage;
        
        private ConnectionCheckResult(boolean allowed, ConnectionRejectionReason rejectionReason, String errorMessage) {
            this.allowed = allowed;
            this.rejectionReason = rejectionReason;
            this.errorMessage = errorMessage;
        }
        
        public static ConnectionCheckResult allowed() {
            return new ConnectionCheckResult(true, null, null);
        }
        
        public static ConnectionCheckResult rejected(ConnectionRejectionReason reason) {
            return new ConnectionCheckResult(false, reason, reason.getMessage());
        }
        
        public boolean isAllowed() {
            return allowed;
        }
        
        public ConnectionRejectionReason getRejectionReason() {
            return rejectionReason;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
    }
    
    /**
     * 连接拒绝原因
     */
    public enum ConnectionRejectionReason {
        CONNECTION_LIMIT_EXCEEDED(1001, "连接数超限"),
        SYSTEM_OVERLOAD(1002, "系统负载过高"),
        RESOURCE_INSUFFICIENT(1003, "资源不足"),
        RATE_LIMIT_TRIGGERED(1004, "限流保护触发"),
        SYSTEM_ERROR(1005, "系统错误");
        
        private final int errorCode;
        private final String message;
        
        ConnectionRejectionReason(int errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }
        
        public int getErrorCode() {
            return errorCode;
        }
        
        public String getMessage() {
            return message;
        }
    }
}

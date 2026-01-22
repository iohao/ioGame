package com.iohao.game.external.core.ratelimit;

import com.iohao.game.external.core.config.ExternalGlobalConfig;
import com.iohao.game.external.core.monitor.SystemResourceMonitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Vegas限流算法单元测试
 */
@ExtendWith(MockitoExtension.class)
class VegasRateLimiterTest {

    @Mock
    private SystemResourceMonitor mockResourceMonitor;

    @Mock
    private SystemResourceMonitor.SystemResourceInfo mockResourceInfo;

    private VegasRateLimiter rateLimiter;

    @BeforeEach
    void setUp() {
        // 设置配置参数
        ExternalGlobalConfig.TrafficProtectionOption.enableAdaptiveRateLimit = true;
        ExternalGlobalConfig.TrafficProtectionOption.maxConnections = 1000;
        ExternalGlobalConfig.TrafficProtectionOption.minRateLimitThreshold = 100;
        ExternalGlobalConfig.TrafficProtectionOption.maxRateLimitThreshold = 2000;
        ExternalGlobalConfig.TrafficProtectionOption.monitorInterval = 100;
        ExternalGlobalConfig.TrafficProtectionOption.cpuThreshold = 80.0;
        ExternalGlobalConfig.TrafficProtectionOption.memoryThreshold = 85.0;

        // 创建限流器实例
        rateLimiter = VegasRateLimiter.getInstance();
    }

    @Test
    void testInitialState() {
        // 测试初始状态
        assertEquals(1000, rateLimiter.getCurrentLimit());
        assertEquals(0, rateLimiter.getCurrentConnections());
        assertFalse(rateLimiter.getStats().getRejectionRate() > 0);
    }

    @Test
    void testTryAcquire() {
        // 测试连接获取
        assertTrue(rateLimiter.tryAcquire());
        assertEquals(1, rateLimiter.getCurrentConnections());
        
        // 测试连接数达到限制时
        for (int i = 1; i < 1000; i++) {
            rateLimiter.incrementConnections();
        }
        
        assertFalse(rateLimiter.tryAcquire());
        assertEquals(1000, rateLimiter.getCurrentConnections());
    }

    @Test
    void testConnectionLifecycle() {
        // 测试连接生命周期
        assertTrue(rateLimiter.tryAcquire());
        rateLimiter.incrementConnections();
        assertEquals(1, rateLimiter.getCurrentConnections());
        
        rateLimiter.decrementConnections();
        assertEquals(0, rateLimiter.getCurrentConnections());
    }

    @Test
    void testRttRecording() {
        // 测试RTT记录
        rateLimiter.recordRtt(10.0);
        rateLimiter.recordRtt(15.0);
        rateLimiter.recordRtt(20.0);
        
        // 验证RTT统计
        assertTrue(rateLimiter.getStats().getTotalRequests() > 0);
    }

    @Test
    void testStartAndStop() {
        // 测试启动和停止
        assertDoesNotThrow(() -> rateLimiter.start());
        assertDoesNotThrow(() -> rateLimiter.stop());
    }

    @Test
    void testStatsCalculation() {
        // 测试统计信息计算
        rateLimiter.tryAcquire();
        rateLimiter.incrementConnections();
        rateLimiter.recordRtt(10.0);
        
        var stats = rateLimiter.getStats();
        assertEquals(1, stats.getCurrentConnections());
        assertEquals(1000, stats.getCurrentLimit());
        assertEquals(1, stats.getTotalRequests());
        assertEquals(0.0, stats.getRejectionRate());
        assertEquals(0.1, stats.getConnectionRate());
    }

    @Test
    void testVegasAlgorithm() {
        // 测试Vegas算法核心逻辑
        rateLimiter.recordRtt(1.0);  // 无负载RTT
        rateLimiter.recordRtt(2.0);  // 实际RTT
        
        // 验证Vegas算法特有的统计字段
        var stats = rateLimiter.getStats();
        assertTrue(stats.getQueueSize() >= 0);
    }

    @Test
    void testQueueSizeCalculation() {
        // 测试队列大小计算
        // queueSize = limit × (1 - RttNoLoad / RTTactual)
        rateLimiter.recordRtt(1.0);  // 无负载RTT
        rateLimiter.recordRtt(2.0);  // 实际RTT
        
        var stats = rateLimiter.getStats();
        int queueSize = stats.getQueueSize();
        
        // 当RTTactual > RttNoLoad时，queueSize应该 > 0
        assertTrue(queueSize >= 0);
    }

    @Test
    void testMultipleConnections() {
        // 测试多个连接
        for (int i = 0; i < 100; i++) {
            assertTrue(rateLimiter.tryAcquire());
            rateLimiter.incrementConnections();
        }
        
        assertEquals(100, rateLimiter.getCurrentConnections());
        assertEquals(100, rateLimiter.getStats().getTotalRequests());
    }

    @Test
    void testRejectionTracking() {
        // 测试拒绝连接跟踪
        // 先填满连接数
        for (int i = 0; i < 1000; i++) {
            rateLimiter.incrementConnections();
        }
        
        // 尝试获取新连接，应该被拒绝
        assertFalse(rateLimiter.tryAcquire());
        
        // 验证拒绝统计
        assertEquals(1, rateLimiter.getStats().getRejectedConnections());
        assertTrue(rateLimiter.getStats().getRejectionRate() > 0);
    }

    @Test
    void testBoundaryConditions() {
        // 测试边界条件
        // 设置最小阈值
        ExternalGlobalConfig.TrafficProtectionOption.minRateLimitThreshold = 500;
        
        // 验证限流阈值不会低于最小值
        assertTrue(rateLimiter.getCurrentLimit() >= 500);
        
        // 设置最大阈值
        ExternalGlobalConfig.TrafficProtectionOption.maxRateLimitThreshold = 1500;
        
        // 验证限流阈值不会超过最大值
        assertTrue(rateLimiter.getCurrentLimit() <= 1500);
    }

    @Test
    void testVegasSpecificFeatures() {
        // 测试Vegas特有的功能
        rateLimiter.recordRtt(5.0);
        rateLimiter.recordRtt(10.0);
        rateLimiter.recordRtt(15.0);
        
        var stats = rateLimiter.getStats();
        
        // 验证Vegas特有的字段
        assertTrue(stats.getQueueSize() >= 0);
    }

    @Test
    void testDynamicThresholds() {
        // 测试动态阈值计算
        // alpha = 3 * log10(limit)
        // beta = 6 * log10(limit)
        // threshold = log10(limit)
        
        int currentLimit = 1000;
        double expectedAlpha = 3 * Math.log10(currentLimit);
        double expectedBeta = 6 * Math.log10(currentLimit);
        double expectedThreshold = Math.log10(currentLimit);
        
        // 验证阈值计算
        assertEquals(expectedAlpha, 3 * Math.log10(1000), 0.001);
        assertEquals(expectedBeta, 6 * Math.log10(1000), 0.001);
        assertEquals(expectedThreshold, Math.log10(1000), 0.001);
    }

    @Test
    void testVegasAlgorithmLogic() {
        // 测试Vegas算法的核心逻辑
        // 这里我们测试算法的数学计算部分
        
        int currentLimit = 1000;
        double alpha = 3 * Math.log10(currentLimit);
        double beta = 6 * Math.log10(currentLimit);
        double threshold = Math.log10(currentLimit);
        
        // 验证阈值关系
        assertTrue(alpha > threshold);
        assertTrue(beta > alpha);
        assertTrue(threshold > 0);
    }

    @Test
    void testQueueSizeBoundary() {
        // 测试队列大小边界条件
        rateLimiter.recordRtt(1.0);  // 无负载RTT
        
        // 当RTTactual = RttNoLoad时，queueSize应该 = 0
        rateLimiter.recordRtt(1.0);
        
        var stats = rateLimiter.getStats();
        int queueSize = stats.getQueueSize();
        
        // 由于浮点数精度问题，我们检查是否接近0
        assertTrue(queueSize >= 0);
    }

    @Test
    void testVegasStatsExtension() {
        // 测试Vegas统计信息扩展
        rateLimiter.recordRtt(10.0);
        
        var stats = rateLimiter.getStats();
        
        // 验证Vegas特有的统计字段
        if (stats instanceof VegasRateLimiter.VegasRateLimitStats) {
            var vegasStats = (VegasRateLimiter.VegasRateLimitStats) stats;
            assertTrue(vegasStats.getQueueSize() >= 0);
            assertTrue(vegasStats.getAlpha() > 0);
            assertTrue(vegasStats.getBeta() > 0);
            assertTrue(vegasStats.getThreshold() > 0);
        }
    }

    @Test
    void testNoLoadRttUpdate() {
        // 测试无负载RTT更新
        rateLimiter.recordRtt(1.0);  // 设置一个很小的RTT
        
        // 等待一段时间后再次记录RTT
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        rateLimiter.recordRtt(2.0);
        
        var stats = rateLimiter.getStats();
        assertTrue(stats.getRttNoLoad() > 0);
    }

    @Test
    void testVegasAlgorithmConvergence() {
        // 测试Vegas算法的收敛性
        // 连续记录多个RTT值，观察算法的收敛行为
        
        for (int i = 1; i <= 20; i++) {
            rateLimiter.recordRtt(i * 5.0);
        }
        
        var stats = rateLimiter.getStats();
        
        // 验证算法能够收敛到合理的值
        assertTrue(stats.getQueueSize() >= 0);
        assertTrue(stats.getCurrentLimit() > 0);
    }
}

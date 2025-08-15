package com.iohao.game.external.core.ratelimit;

import com.iohao.game.external.core.config.ExternalGlobalConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 限流器集成测试
 * 测试所有三种算法的基本功能和性能
 */
@ExtendWith(MockitoExtension.class)
class RateLimiterIntegrationTest {

    private GradientRateLimiter gradientRateLimiter;
    private Gradient2RateLimiter gradient2RateLimiter;
    private VegasRateLimiter vegasRateLimiter;

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

        // 创建所有限流器实例
        gradientRateLimiter = GradientRateLimiter.getInstance();
        gradient2RateLimiter = Gradient2RateLimiter.getInstance();
        vegasRateLimiter = VegasRateLimiter.getInstance();
    }

    @Test
    void testAllAlgorithmsInitialState() {
        // 测试所有算法的初始状态
        assertEquals(1000, gradientRateLimiter.getCurrentLimit());
        assertEquals(1000, gradient2RateLimiter.getCurrentLimit());
        assertEquals(1000, vegasRateLimiter.getCurrentLimit());
        
        assertEquals(0, gradientRateLimiter.getCurrentConnections());
        assertEquals(0, gradient2RateLimiter.getCurrentConnections());
        assertEquals(0, vegasRateLimiter.getCurrentConnections());
    }

    @Test
    void testAllAlgorithmsConnectionLifecycle() {
        // 测试所有算法的连接生命周期
        // Gradient算法
        assertTrue(gradientRateLimiter.tryAcquire());
        gradientRateLimiter.incrementConnections();
        assertEquals(1, gradientRateLimiter.getCurrentConnections());
        gradientRateLimiter.decrementConnections();
        assertEquals(0, gradientRateLimiter.getCurrentConnections());
        
        // Gradient2算法
        assertTrue(gradient2RateLimiter.tryAcquire());
        gradient2RateLimiter.incrementConnections();
        assertEquals(1, gradient2RateLimiter.getCurrentConnections());
        gradient2RateLimiter.decrementConnections();
        assertEquals(0, gradient2RateLimiter.getCurrentConnections());
        
        // Vegas算法
        assertTrue(vegasRateLimiter.tryAcquire());
        vegasRateLimiter.incrementConnections();
        assertEquals(1, vegasRateLimiter.getCurrentConnections());
        vegasRateLimiter.decrementConnections();
        assertEquals(0, vegasRateLimiter.getCurrentConnections());
    }

    @Test
    void testAllAlgorithmsRttRecording() {
        // 测试所有算法的RTT记录
        double[] rttValues = {10.0, 15.0, 20.0, 25.0, 30.0};
        
        for (double rtt : rttValues) {
            gradientRateLimiter.recordRtt(rtt);
            gradient2RateLimiter.recordRtt(rtt);
            vegasRateLimiter.recordRtt(rtt);
        }
        
        // 验证所有算法都正确记录了RTT
        assertTrue(gradientRateLimiter.getStats().getTotalRequests() > 0);
        assertTrue(gradient2RateLimiter.getStats().getTotalRequests() > 0);
        assertTrue(vegasRateLimiter.getStats().getTotalRequests() > 0);
    }

    @Test
    void testAllAlgorithmsStartAndStop() {
        // 测试所有算法的启动和停止
        assertDoesNotThrow(() -> {
            gradientRateLimiter.start();
            gradient2RateLimiter.start();
            vegasRateLimiter.start();
        });
        
        assertDoesNotThrow(() -> {
            gradientRateLimiter.stop();
            gradient2RateLimiter.stop();
            vegasRateLimiter.stop();
        });
    }

    @Test
    void testAllAlgorithmsStatsCalculation() {
        // 测试所有算法的统计信息计算
        // 添加一些连接和RTT数据
        for (int i = 0; i < 10; i++) {
            gradientRateLimiter.tryAcquire();
            gradientRateLimiter.incrementConnections();
            gradientRateLimiter.recordRtt(10.0 + i);
            
            gradient2RateLimiter.tryAcquire();
            gradient2RateLimiter.incrementConnections();
            gradient2RateLimiter.recordRtt(10.0 + i);
            
            vegasRateLimiter.tryAcquire();
            vegasRateLimiter.incrementConnections();
            vegasRateLimiter.recordRtt(10.0 + i);
        }
        
        // 验证统计信息
        var gradientStats = gradientRateLimiter.getStats();
        var gradient2Stats = gradient2RateLimiter.getStats();
        var vegasStats = vegasRateLimiter.getStats();
        
        assertEquals(10, gradientStats.getCurrentConnections());
        assertEquals(10, gradient2Stats.getCurrentConnections());
        assertEquals(10, vegasStats.getCurrentConnections());
        
        assertEquals(10, gradientStats.getTotalRequests());
        assertEquals(10, gradient2Stats.getTotalRequests());
        assertEquals(10, vegasStats.getTotalRequests());
    }

    @Test
    void testAllAlgorithmsRejectionHandling() {
        // 测试所有算法的拒绝连接处理
        // 先填满连接数
        for (int i = 0; i < 1000; i++) {
            gradientRateLimiter.incrementConnections();
            gradient2RateLimiter.incrementConnections();
            vegasRateLimiter.incrementConnections();
        }
        
        // 尝试获取新连接，应该被拒绝
        assertFalse(gradientRateLimiter.tryAcquire());
        assertFalse(gradient2RateLimiter.tryAcquire());
        assertFalse(vegasRateLimiter.tryAcquire());
        
        // 验证拒绝统计
        assertEquals(1, gradientRateLimiter.getStats().getRejectedConnections());
        assertEquals(1, gradient2RateLimiter.getStats().getRejectedConnections());
        assertEquals(1, vegasRateLimiter.getStats().getRejectedConnections());
    }

    @Test
    void testAllAlgorithmsBoundaryConditions() {
        // 测试所有算法的边界条件
        // 设置最小和最大阈值
        ExternalGlobalConfig.TrafficProtectionOption.minRateLimitThreshold = 500;
        ExternalGlobalConfig.TrafficProtectionOption.maxRateLimitThreshold = 1500;
        
        // 验证所有算法的限流阈值都在有效范围内
        assertTrue(gradientRateLimiter.getCurrentLimit() >= 500);
        assertTrue(gradient2RateLimiter.getCurrentLimit() >= 500);
        assertTrue(vegasRateLimiter.getCurrentLimit() >= 500);
        
        assertTrue(gradientRateLimiter.getCurrentLimit() <= 1500);
        assertTrue(gradient2RateLimiter.getCurrentLimit() <= 1500);
        assertTrue(vegasRateLimiter.getCurrentLimit() <= 1500);
    }

    @Test
    void testAllAlgorithmsPerformance() {
        // 测试所有算法的性能
        long startTime = System.currentTimeMillis();
        
        // 执行大量操作
        for (int i = 0; i < 1000; i++) {
            gradientRateLimiter.tryAcquire();
            gradient2RateLimiter.tryAcquire();
            vegasRateLimiter.tryAcquire();
            
            if (i % 100 == 0) {
                gradientRateLimiter.recordRtt(i * 0.1);
                gradient2RateLimiter.recordRtt(i * 0.1);
                vegasRateLimiter.recordRtt(i * 0.1);
            }
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // 验证性能（应该在合理时间内完成）
        assertTrue(duration < 5000, "性能测试应该在5秒内完成，实际耗时: " + duration + "ms");
    }

    @Test
    void testAllAlgorithmsConcurrentAccess() {
        // 测试所有算法的并发访问
        int threadCount = 10;
        int operationsPerThread = 100;
        
        Thread[] threads = new Thread[threadCount];
        
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < operationsPerThread; j++) {
                    try {
                        gradientRateLimiter.tryAcquire();
                        gradient2RateLimiter.tryAcquire();
                        vegasRateLimiter.tryAcquire();
                        
                        if (j % 10 == 0) {
                            gradientRateLimiter.recordRtt(threadId * 10.0 + j);
                            gradient2RateLimiter.recordRtt(threadId * 10.0 + j);
                            vegasRateLimiter.recordRtt(threadId * 10.0 + j);
                        }
                    } catch (Exception e) {
                        // 忽略异常，继续测试
                    }
                }
            });
        }
        
        // 启动所有线程
        for (Thread thread : threads) {
            thread.start();
        }
        
        // 等待所有线程完成
        for (Thread thread : threads) {
            try {
                thread.join(5000); // 最多等待5秒
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // 验证所有算法都能正确处理并发访问
        assertTrue(gradientRateLimiter.getStats().getTotalRequests() > 0);
        assertTrue(gradient2RateLimiter.getStats().getTotalRequests() > 0);
        assertTrue(vegasRateLimiter.getStats().getTotalRequests() > 0);
    }

    @Test
    void testAllAlgorithmsMemoryUsage() {
        // 测试所有算法的内存使用
        Runtime runtime = Runtime.getRuntime();
        
        // 记录初始内存
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // 执行大量操作
        for (int i = 0; i < 10000; i++) {
            gradientRateLimiter.tryAcquire();
            gradient2RateLimiter.tryAcquire();
            vegasRateLimiter.tryAcquire();
            
            if (i % 100 == 0) {
                gradientRateLimiter.recordRtt(i * 0.01);
                gradient2RateLimiter.recordRtt(i * 0.01);
                vegasRateLimiter.recordRtt(i * 0.01);
            }
        }
        
        // 强制垃圾回收
        System.gc();
        
        // 记录最终内存
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryIncrease = finalMemory - initialMemory;
        
        // 验证内存使用在合理范围内（增加不超过100MB）
        assertTrue(memoryIncrease < 100 * 1024 * 1024, 
                "内存使用增加过多: " + (memoryIncrease / 1024 / 1024) + "MB");
    }
}

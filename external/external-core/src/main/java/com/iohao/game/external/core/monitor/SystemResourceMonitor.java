/*
 * ioGame
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.iohao.game.external.core.monitor;

import com.iohao.game.external.core.config.ExternalGlobalConfig;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 系统资源监控器
 * 负责监控CPU、内存等系统资源使用情况
 *
 * @author undertaker86001
 * @date 2025-08-15
 */
@Slf4j
public class SystemResourceMonitor {
    
    /** 单例实例 */
    private static final SystemResourceMonitor INSTANCE = new SystemResourceMonitor();
    
    /** 操作系统MXBean */
    private final OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
    
    /** 内存MXBean */
    private final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    
    /** 定时任务执行器 */
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    
    /** 当前系统资源信息 */
    private final AtomicReference<SystemResourceInfo> currentInfo = new AtomicReference<>();
    
    /** 是否已启动 */
    private volatile boolean started = false;
    
    /** 私有构造函数 */
    private SystemResourceMonitor() {
        // 初始化当前资源信息
        this.currentInfo.set(new SystemResourceInfo());
    }
    
    /**
     * 获取单例实例
     *
     * @return SystemResourceMonitor实例
     */
    public static SystemResourceMonitor getInstance() {
        return INSTANCE;
    }
    
    /**
     * 启动监控
     */
    public void start() {
        if (started) {
            return;
        }
        
        if (!ExternalGlobalConfig.TrafficProtectionOption.enableResourceMonitoring) {
            log.info("系统资源监控已禁用");
            return;
        }
        
        // 立即执行一次监控
        updateResourceInfo();
        
        // 启动定时监控任务
        int interval = ExternalGlobalConfig.TrafficProtectionOption.monitorInterval;
        scheduler.scheduleAtFixedRate(this::updateResourceInfo, interval, interval, TimeUnit.MILLISECONDS);
        
        started = true;
        log.info("系统资源监控已启动，监控间隔: {}ms", interval);
    }
    
    /**
     * 停止监控
     */
    public void stop() {
        if (!started) {
            return;
        }
        
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        started = false;
        log.info("系统资源监控已停止");
    }
    
    /**
     * 获取当前系统资源信息
     *
     * @return 系统资源信息
     */
    public SystemResourceInfo getCurrentInfo() {
        return currentInfo.get();
    }
    
    /**
     * 更新系统资源信息
     */
    private void updateResourceInfo() {
        try {
            SystemResourceInfo info = new SystemResourceInfo();
            
            // 获取CPU使用率
            info.setCpuUsage(getCpuUsage());
            
            // 获取内存使用率
            info.setMemoryUsage(getMemoryUsage());
            
            // 获取系统负载
            info.setSystemLoad(getSystemLoad());
            
            // 获取可用内存
            info.setAvailableMemory(getAvailableMemory());
            
            // 获取总内存
            info.setTotalMemory(getTotalMemory());
            
            // 获取JVM内存使用情况
            info.setJvmMemoryUsage(getJvmMemoryUsage());
            
            // 设置时间戳
            info.setTimestamp(System.currentTimeMillis());
            
            // 更新当前信息
            currentInfo.set(info);
            
            // 检查是否需要告警
            checkResourceAlerts(info);
            
        } catch (Exception e) {
            log.error("更新系统资源信息失败", e);
        }
    }
    
    /**
     * 获取CPU使用率
     *
     * @return CPU使用率（百分比）
     */
    private double getCpuUsage() {
        try {
            // 获取系统CPU负载
            double systemLoad = osBean.getSystemLoadAverage();
            
            // 如果系统负载不可用，返回0
            if (systemLoad < 0) {
                return 0.0;
            }
            
            // 获取CPU核心数
            int cpuCores = osBean.getAvailableProcessors();
            
            // 计算CPU使用率（系统负载 / CPU核心数 * 100）
            double cpuUsage = (systemLoad / cpuCores) * 100.0;
            
            // 限制在0-100范围内
            return Math.min(100.0, Math.max(0.0, cpuUsage));
            
        } catch (Exception e) {
            log.warn("获取CPU使用率失败", e);
            return 0.0;
        }
    }
    
    /**
     * 获取内存使用率
     *
     * @return 内存使用率（百分比）
     */
    private double getMemoryUsage() {
        try {
            long totalMemory = getTotalMemory();
            long availableMemory = getAvailableMemory();
            
            if (totalMemory <= 0) {
                return 0.0;
            }
            
            long usedMemory = totalMemory - availableMemory;
            double memoryUsage = ((double) usedMemory / totalMemory) * 100.0;
            
            return Math.min(100.0, Math.max(0.0, memoryUsage));
            
        } catch (Exception e) {
            log.warn("获取内存使用率失败", e);
            return 0.0;
        }
    }
    
    /**
     * 获取系统负载
     *
     * @return 系统负载
     */
    private double getSystemLoad() {
        try {
            double load = osBean.getSystemLoadAverage();
            return load < 0 ? 0.0 : load;
        } catch (Exception e) {
            log.warn("获取系统负载失败", e);
            return 0.0;
        }
    }
    
    /**
     * 获取可用内存
     *
     * @return 可用内存（字节）
     */
    private long getAvailableMemory() {
        try {
            return Runtime.getRuntime().freeMemory();
        } catch (Exception e) {
            log.warn("获取可用内存失败", e);
            return 0L;
        }
    }
    
    /**
     * 获取总内存
     *
     * @return 总内存（字节）
     */
    private long getTotalMemory() {
        try {
            return Runtime.getRuntime().totalMemory();
        } catch (Exception e) {
            log.warn("获取总内存失败", e);
            return 0L;
        }
    }
    
    /**
     * 获取JVM内存使用率
     *
     * @return JVM内存使用率（百分比）
     */
    private double getJvmMemoryUsage() {
        try {
            long usedMemory = memoryBean.getHeapMemoryUsage().getUsed();
            long maxMemory = memoryBean.getHeapMemoryUsage().getMax();
            
            if (maxMemory <= 0) {
                return 0.0;
            }
            
            double jvmMemoryUsage = ((double) usedMemory / maxMemory) * 100.0;
            return Math.min(100.0, Math.max(0.0, jvmMemoryUsage));
            
        } catch (Exception e) {
            log.warn("获取JVM内存使用率失败", e);
            return 0.0;
        }
    }
    
    /**
     * 检查资源告警
     *
     * @param info 系统资源信息
     */
    private void checkResourceAlerts(SystemResourceInfo info) {
        double cpuThreshold = ExternalGlobalConfig.TrafficProtectionOption.cpuThreshold;
        double memoryThreshold = ExternalGlobalConfig.TrafficProtectionOption.memoryThreshold;
        
        // CPU使用率告警
        if (info.getCpuUsage() > cpuThreshold) {
            log.warn("CPU使用率过高: {}%, 阈值: {}%", 
                    String.format("%.2f", info.getCpuUsage()), 
                    String.format("%.2f", cpuThreshold));
        }
        
        // 内存使用率告警
        if (info.getMemoryUsage() > memoryThreshold) {
            log.warn("内存使用率过高: {}%, 阈值: {}%", 
                    String.format("%.2f", info.getMemoryUsage()), 
                    String.format("%.2f", memoryThreshold));
        }
        
        // JVM内存使用率告警
        if (info.getJvmMemoryUsage() > 90.0) {
            log.warn("JVM内存使用率过高: {}%", 
                    String.format("%.2f", info.getJvmMemoryUsage()));
        }
    }
    
    /**
     * 系统资源信息
     */
    @Getter
    @Accessors(chain = true)
    public static class SystemResourceInfo {
        /** CPU使用率（百分比） */
        private double cpuUsage = 0.0;
        
        /** 内存使用率（百分比） */
        private double memoryUsage = 0.0;
        
        /** 系统负载 */
        private double systemLoad = 0.0;
        
        /** 可用内存（字节） */
        private long availableMemory = 0L;
        
        /** 总内存（字节） */
        private long totalMemory = 0L;
        
        /** JVM内存使用率（百分比） */
        private double jvmMemoryUsage = 0.0;
        
        /** 时间戳 */
        private long timestamp = 0L;
        
        /**
         * 设置CPU使用率
         */
        public SystemResourceInfo setCpuUsage(double cpuUsage) {
            this.cpuUsage = cpuUsage;
            return this;
        }
        
        /**
         * 设置内存使用率
         */
        public SystemResourceInfo setMemoryUsage(double memoryUsage) {
            this.memoryUsage = memoryUsage;
            return this;
        }
        
        /**
         * 设置系统负载
         */
        public SystemResourceInfo setSystemLoad(double systemLoad) {
            this.systemLoad = systemLoad;
            return this;
        }
        
        /**
         * 设置可用内存
         */
        public SystemResourceInfo setAvailableMemory(long availableMemory) {
            this.availableMemory = availableMemory;
            return this;
        }
        
        /**
         * 设置总内存
         */
        public SystemResourceInfo setTotalMemory(long totalMemory) {
            this.totalMemory = totalMemory;
            return this;
        }
        
        /**
         * 设置JVM内存使用率
         */
        public SystemResourceInfo setJvmMemoryUsage(double jvmMemoryUsage) {
            this.jvmMemoryUsage = jvmMemoryUsage;
            return this;
        }
        
        /**
         * 设置时间戳
         */
        public SystemResourceInfo setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        @Override
        public String toString() {
            return String.format(
                    "SystemResourceInfo{cpuUsage=%.2f%%, memoryUsage=%.2f%%, systemLoad=%.2f, " +
                    "availableMemory=%d, totalMemory=%d, jvmMemoryUsage=%.2f%%, timestamp=%d}",
                    cpuUsage, memoryUsage, systemLoad, availableMemory, totalMemory, jvmMemoryUsage, timestamp
            );
        }
    }
}

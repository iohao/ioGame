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
package com.iohao.game.external.core.config;

import com.iohao.game.external.core.hook.AccessAuthenticationHook;
import com.iohao.game.external.core.hook.cache.ExternalCmdCache;
import com.iohao.game.external.core.hook.internal.DefaultAccessAuthenticationHook;
import com.iohao.game.external.core.message.ExternalMessage;
import lombok.experimental.UtilityClass;

/**
 * @author 渔民小镇
 * @date 2023-02-19
 */
@UtilityClass
public class ExternalGlobalConfig {
    /** 游戏对外服默认启动端口 */
    public final int externalPort = 10100;

    /**
     * 访问验证钩子接口
     */
    public AccessAuthenticationHook accessAuthenticationHook = new DefaultAccessAuthenticationHook();
    /** 游戏对外服路由缓存 */
    public ExternalCmdCache externalCmdCache;
    /** true 表示开启简单日志打印 netty handler. see SimpleLoggerHandler */
    public boolean enableLoggerHandler = true;
    /**
     * 协议开关，用于一些协议级别的开关控制，比如 安全加密校验等。 : 0 不校验
     * <pre>
     *     see {@link  ExternalMessage#getProtocolSwitch()}
     * </pre>
     */
    public int protocolSwitch;

    @UtilityClass
    public class CoreOption {
        /** 默认数据包最大 1MB */
        public int packageMaxSize = 1024 * 1024;
        /** http 升级 websocket 协议地址 */
        public String websocketPath = "/websocket";
    }

    /**
     * 流量过载保护配置
     */
    @UtilityClass
    public class TrafficProtectionOption {
        /** 是否启用流量保护功能 */
        public boolean enableTrafficProtection = false;
        
        /** 最大连接数限制，默认5000 */
        public int maxConnections = 5000;
        
        /** 是否启用自适应限流 */
        public boolean enableAdaptiveRateLimit = true;
        
        /** 监控间隔（毫秒），默认1000ms */
        public int monitorInterval = 1000;
        
        /** 自适应限流窗口大小（秒），默认60秒 */
        public int adaptiveWindowSize = 60;
        
        /** 最小限流阈值（连接数），默认1000 */
        public int minRateLimitThreshold = 1000;
        
        /** 最大限流阈值（连接数），默认8000 */
        public int maxRateLimitThreshold = 8000;
        
        /** CPU使用率阈值（百分比），超过此值将触发限流 */
        public double cpuThreshold = 80.0;
        
        /** 内存使用率阈值（百分比），超过此值将触发限流 */
        public double memoryThreshold = 85.0;
        
        /** 是否启用连接拒绝日志 */
        public boolean enableRejectionLog = true;
        
        /** 是否启用系统资源监控 */
        public boolean enableResourceMonitoring = true;
    }
}

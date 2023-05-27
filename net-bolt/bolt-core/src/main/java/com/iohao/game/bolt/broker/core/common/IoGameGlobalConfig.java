/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.bolt.broker.core.common;

import com.iohao.game.bolt.broker.core.aware.UserProcessorExecutorAware;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * ioGame 全局默认配置
 * <pre>
 *     当前大版本会兼容 BrokerGlobalConfig 配置，
 *     下个大版本将会移除 BrokerGlobalConfig；
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-11-11
 */
@UtilityClass
public class IoGameGlobalConfig {
    /** broker （游戏网关）默认端口 */
    public int brokerPort = BrokerGlobalConfig.brokerPort;
    /** bolt 消息发送超时时间 */
    public int timeoutMillis = BrokerGlobalConfig.timeoutMillis;
    /** 集群默认监听端口 Gossip listen port */
    public int gossipListenPort = BrokerGlobalConfig.gossipListenPort;
    /** true 开启日志 */
    public boolean openLog = BrokerGlobalConfig.openLog;
    /** true 开启请求响应相关日志，默认为 false */
    public boolean requestResponseLog = BrokerGlobalConfig.requestResponseLog;
    /** true 开启对外服相关日志，默认为 false */
    public boolean externalLog = BrokerGlobalConfig.externalLog;
    /** true 开启广播相关日志，默认为 false */
    public boolean broadcastLog = BrokerGlobalConfig.broadcastLog;

    /** true 开启集群相关日志 */
    public boolean brokerClusterLog = BrokerGlobalConfig.brokerClusterLog;
    /** true 使用调度器打印集群信息，默认 30 秒打印一次（目前不提供打印频率设置） */
    public boolean brokerClusterFixedRateLog;
    /** UserProcessor 构建 Executor 的策略 */
    public UserProcessorExecutorStrategy userProcessorExecutorStrategy = new DefaultUserProcessorExecutorStrategy();

    public Executor getExecutor(UserProcessorExecutorAware userProcessorExecutorAware) {

        if (Objects.isNull(userProcessorExecutorStrategy)) {
            // 不使用任何策略
            return null;
        }

        return userProcessorExecutorStrategy.getExecutor(userProcessorExecutorAware);
    }

    public boolean isExternalLog() {
        return openLog && externalLog;
    }

    public boolean isBrokerClusterLog() {
        return openLog && brokerClusterLog;
    }

    public boolean isBrokerClusterFixedRateLog() {
        return openLog && brokerClusterFixedRateLog;
    }

    public boolean isSendBrokerClientModuleMessage() {
        // 实验性功能
        return sendBrokerClientModuleMessage;
    }

    public boolean sendBrokerClientModuleMessage = false;
}

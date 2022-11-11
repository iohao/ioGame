/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    /** true 开启请求响应相关日志 */
    public boolean requestResponseLog = BrokerGlobalConfig.requestResponseLog;
    /** true 开启对外服相关日志 */
    public boolean externalLog = BrokerGlobalConfig.externalLog;
    /** true 开启广播相关日志 */
    public boolean broadcastLog = BrokerGlobalConfig.broadcastLog;

    /** true 开启集群相关日志 */
    public boolean brokerClusterLog = BrokerGlobalConfig.brokerClusterLog;
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
}

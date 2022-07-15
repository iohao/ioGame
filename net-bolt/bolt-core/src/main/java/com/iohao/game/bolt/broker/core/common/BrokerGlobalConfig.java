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

import lombok.experimental.UtilityClass;

/**
 * broker 全局默认配置
 *
 * @author 渔民小镇
 * @date 2022-05-16
 */
@UtilityClass
public class BrokerGlobalConfig {
    /** broker （游戏网关）默认端口 */
    public int brokerPort = 10200;
    /** bolt 消息发送超时时间 */
    public int timeoutMillis = 3000;
    /** 集群默认监听端口 Gossip listen port */
    public int gossipListenPort = 30056;
    /** true 开启日志 */
    public boolean openLog = true;
    /** true 开启请求响应相关日志 */
    public boolean requestResponseLog = false;
    /** true 开启对外服相关日志 */
    public boolean externalLog = false;
    /** true 开启广播相关日志 */
    public boolean broadcastLog = false;

    public boolean isExternalLog() {
        return openLog && externalLog;
    }
}

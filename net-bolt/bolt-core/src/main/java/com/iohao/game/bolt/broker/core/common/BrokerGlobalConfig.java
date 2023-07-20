/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
package com.iohao.game.bolt.broker.core.common;

import lombok.experimental.UtilityClass;

/**
 * broker 全局默认配置
 * <pre>
 *     请使用 IoGameGlobalConfig 来代替
 *
 *     BrokerGlobalConfig 有点表示网关全局配置的意思，名字不太理想
 *
 *     将在下个大版本中移除
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-16
 */
@Deprecated
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

    /** true 开启集群相关日志 */
    public boolean brokerClusterLog = true;

    public boolean isExternalLog() {
        return openLog && externalLog;
    }

    public boolean isBrokerClusterLog() {
        return openLog && brokerClusterLog;
    }

}

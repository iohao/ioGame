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
package com.iohao.game.bolt.broker.core.common;

import com.iohao.game.action.skeleton.core.IoGameCommonCoreConfig;
import com.iohao.game.bolt.broker.core.aware.UserProcessorExecutorAware;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * ioGame 全局默认配置
 *
 * @author 渔民小镇
 * @date 2022-11-11
 */
@UtilityClass
public class IoGameGlobalConfig {
    /** broker （游戏网关）默认端口 */
    public int brokerPort = 10200;
    /** bolt 消息发送超时时间 */
    public int timeoutMillis = 3000;
    /** 集群默认监听端口 Gossip listen port */
    public int gossipListenPort = 30056;
    /** true 开启日志 */
    public boolean openLog = true;
    /** true 开启请求响应相关日志，默认为 false */
    public boolean requestResponseLog;
    /** true 开启对外服相关日志，默认为 false */
    public boolean externalLog;
    /** true 开启广播相关日志，默认为 false */
    public boolean broadcastLog;
    /**
     * Broker（游戏网关）转发消息容错配置
     * <pre>
     *     游戏逻辑服与游戏对外服通信时，如果没有明确指定要通信游戏对外服，游戏网关则会将消息转发到所有的游戏对外服上。
     *     如果指定了游戏对外服的，游戏网关则会将消息转发到该游戏对外服上，而不会将消息转发到所有的对外服上。
     *
     *     当为 true 时，开启容错机制
     *         表示开发者在发送消息时，如果指定了游戏对外服的，
     *         但【游戏网关】中没有找到所指定的【游戏对外服】，则会将消息转发到所有的游戏对外服上，
     *         这么做的目的是，即使开发者填错了指定的游戏对外服，也能保证消息可以送达到游戏对外服。
     *
     *     当为 false 时，关闭容错机制
     *         表示在【游戏网关】中找不到指定的【游戏对外服】时，则不管了。
     *
     *     支持的通讯方式场景
     *         <a href="https://www.yuque.com/iohao/game/qv4qfo">广播、推送</a>
     *         <a href="https://www.yuque.com/iohao/game/ivxsw5">获取游戏对外服的数据与扩展</a>
     * </pre>
     * 另一种叙述版本
     * <pre>
     *     作用：
     *         在游戏逻辑服发送广播时，支持指定游戏对外服来广播；
     *         如果你能事先知道所要广播的游戏对外服，那么在广播时通过指定游戏对外服，可以避免一些无效的转发。
     *
     *         为了更好的理解的这个配置的作用，这里将作一些比喻：
     *         1. 将广播时指定的游戏对外服，看作是目标
     *         2. 将发送广播的游戏逻辑服，看作是命令
     *         3. 而 Broker（游戏网关）职责是对消息做转发，可看成是一名射击员；射击员手上有两把枪，分别是狙击枪和 AK47。
     *
     *         狙击枪的作用是单点目标，而 AK47 的作用则是扫射多个目标（就是所有的游戏对外服）。
     *
     *     场景一：
     *         当设置为 true 时，表示射击员可以将手中的狙击切换为 AK47，什么意思呢？
     *         意思就是如果在【游戏网关】中没有找到所指定的【游戏对外服】，则将广播数据发送给【所有的游戏对外服】。（换 AK 来扫射）
     *         这么做的目的是，即使开发者填错了指定的游戏对外服，也能保证消息可以送达到游戏对外服。
     *
     *     场景二：
     *         当设置为 false 时，表示找不到指定的【游戏对外服】时，则不管了。
     * </pre>
     */
    public boolean brokerSniperToggleAK47 = true;

    /** true 开启集群相关日志 */
    public boolean brokerClusterLog;
    /** true 使用调度器打印集群信息，默认 30 秒打印一次（目前不提供打印频率设置） */
    public boolean brokerClusterFixedRateLog;
    /** true 表示开启 traceId 特性 */
    public boolean openTraceId;

    @Getter
    boolean eventBusLog;

    /**
     * UserProcessor 构建 Executor 的策略
     * <pre>
     *     默认使用 DefaultUserProcessorExecutorStrategy 实现类，
     *     内容使用 Executors.newVirtualThreadPerTaskExecutor()
     * </pre>
     *
     * @see DefaultUserProcessorExecutorStrategy
     * @see VirtualThreadUserProcessorExecutorStrategy
     */
    public UserProcessorExecutorStrategy userProcessorExecutorStrategy = new DefaultUserProcessorExecutorStrategy();

    public Executor getExecutor(UserProcessorExecutorAware userProcessorExecutorAware) {

        if (Objects.isNull(userProcessorExecutorStrategy)) {
            // 不使用任何策略
            return null;
        }

        return userProcessorExecutorStrategy.getExecutor(userProcessorExecutorAware);
    }

    boolean userProcessorExecutorSelectorEnable;

    public void enableUserProcessorExecutorSelector() {
        userProcessorExecutorSelectorEnable = true;
    }

    public UserProcessorExecutorSelectorStrategy userProcessorExecutorSelectorStrategy;

    public UserProcessorExecutorSelectorStrategy getExecutorSelector() {

        if (!userProcessorExecutorSelectorEnable) {
            return null;
        }

        if (Objects.isNull(userProcessorExecutorSelectorStrategy)) {
            userProcessorExecutorSelectorStrategy = DefaultUserProcessorExecutorSelectorStrategy.me();
        }

        return userProcessorExecutorSelectorStrategy;
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

    public void setEventBusLog(boolean eventBusLog) {
        IoGameGlobalConfig.eventBusLog = eventBusLog;
        IoGameCommonCoreConfig.eventBusLog = eventBusLog;
    }

    /** 框架内部使用，开发者不要使用 */
    public interface InternalConfig {
        long executorIndex = 0;
        long clusterExecutorIndex = 2;
        long connectIndex = clusterExecutorIndex;
    }
}

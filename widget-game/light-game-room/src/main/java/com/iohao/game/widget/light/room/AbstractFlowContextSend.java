/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.widget.light.room;

import com.iohao.game.action.skeleton.core.ActionSend;
import com.iohao.game.action.skeleton.core.commumication.BroadcastContext;
import com.iohao.game.action.skeleton.core.commumication.BrokerClientContext;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.widget.light.domain.event.message.Eo;
import com.iohao.game.widget.light.domain.event.message.Topic;
import lombok.AccessLevel;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashSet;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * 抽象响应消息 推送
 *
 * @author 渔民小镇
 * @date 2022-03-31
 */
@Slf4j
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractFlowContextSend implements Topic, Eo, ActionSend {

    /** 需要推送的用户id列表 */
    final Set<Long> userIds = new NonBlockingHashSet<>();

    /** 是否执行发送领域事件操作: true 执行推送操作 */
    boolean doSend = true;

    /** 业务框架 flow 上下文 */
    protected final FlowContext flowContext;

    protected AbstractFlowContextSend(FlowContext flowContext) {
        this.flowContext = flowContext;
    }

    /**
     * 在将数据推送到调用方之前，触发的方法
     * <pre>
     *     可以做一些逻辑，在逻辑中可以决定是否执行推送
     * </pre>
     */
    protected void logic() {

    }

    /**
     * 小把戏 (钩子方法). 子类可以做些其他的事情
     * <pre>
     *     在将数据推送到调用方之前，触发的方法
     * </pre>
     */
    protected void trick() {
    }

    /**
     * 响应消息到远程, 此方法是同步推送
     * <pre>
     *     如果没有特殊情况 , 使用异步推送 (当前类的) send 方法
     * </pre>
     * 模板方法模式：
     * <pre>
     * 在一个方法中定义一个算法的骨架，而将一些步骤延迟到子类中。
     * 模板方法使得子类可以在不改变算法结构的情况下，重新定义算法中的某些步骤。
     * 要点：
     * - “模板方法”定义了算法的步骤，把这些步骤的实现延迟到子类。
     * - 模板方法模式为我们提供了一种代码复用的重要技巧。
     * - 模板方法的抽象类可以定义具体方法、抽象方法和钩子。
     * - 抽象方法由子类实现。
     * - 钩子是一种方法，它在抽象类中不做事，或者只做默认的事情，子类可以选择要不要去覆盖它。
     * - 为了防止子类改变模板方法中的算法，可以将模板方法声明为final。
     * - 好莱坞原则告诉我们，将决策权放在高层模块中，以便决定如何以及何时调用低层模块。
     * - 你将在真实世界代码中看到模板方法模式的许多变体，不要期待它们全都是一眼就可以被你一眼认出的。
     * - 策略模式和模板方法模式都封装算法，一个用组合，一个用继承。
     * - 工厂方法是模板方法的一种特殊版本。
     * </pre>
     */
    public final void execute() {
        Objects.requireNonNull(this.flowContext, "flowContext must not be null");

        if (userIds.isEmpty()) {
            throw new RuntimeException("没有添加消息推送人 " + this.getClass());
        }

        // 子类构建响应内容
        this.logic();

        // 钩子方法
        if (!doSend) {
            return;
        }

        // 响应对象
        ResponseMessage responseMessage = this.flowContext.getResponse();
        HeadMetadata headMetadata = responseMessage.getHeadMetadata();

        // 路由设置
        int cmdMerge = this.getCmdMerge();
        headMetadata.setCmdMerge(cmdMerge);

        // 在将数据推送前调用的钩子方法
        this.trick();

        BrokerClientContext brokerClientContext = flowContext.option(FlowAttr.brokerClientContext);

        // 推送响应 （广播消息）给指定的用户列表
        BroadcastContext broadcastContext = brokerClientContext.getBroadcastContext();
        broadcastContext.broadcast(responseMessage, this.userIds);
    }

    /**
     * 接收广播的用户
     *
     * @param userIds 用户id列表
     * @return me
     */
    public AbstractFlowContextSend addUserId(Collection<Long> userIds) {
        this.userIds.addAll(userIds);
        return this;
    }

    /**
     * 接收广播的用户
     *
     * @param userId 用户id
     * @return me
     */
    public AbstractFlowContextSend addUserId(long userId) {
        this.userIds.add(userId);
        return this;
    }

    /**
     * 添加用户id列表
     *
     * @param userIds       用户id列表
     * @param excludeUserId 需要排除的用户id
     * @return me
     */
    public AbstractFlowContextSend addUserId(Collection<Long> userIds, long excludeUserId) {
        return this.addUserId(userIds)
                .removeUserId(excludeUserId);
    }

    @Override
    public Class<?> getTopic() {
        return AbstractFlowContextSend.class;
    }


    @Override
    public int getCmdMerge() {
        return flowContext.getActionCommand().getCmdInfo().getCmdMerge();
    }

    /**
     * 不执行推送数据的操作
     */
    protected void disableSend() {
        this.doSend = false;
    }

    /**
     * 排除用户id
     *
     * @param userId 用户id
     * @return me
     */
    private AbstractFlowContextSend removeUserId(long userId) {
        if (userId > 0) {
            this.userIds.remove(userId);
        }

        return this;
    }
}

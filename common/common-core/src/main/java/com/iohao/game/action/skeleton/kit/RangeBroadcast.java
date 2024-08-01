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
package com.iohao.game.action.skeleton.kit;

import com.iohao.game.action.skeleton.core.commumication.CommunicationAggregationContext;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.kit.exception.ThrowKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashSet;

import java.util.Objects;
import java.util.Set;

/**
 * 范围内的广播，这个范围指的是，指定某些用户进行广播。
 * <pre>
 *     在执行广播前，开发者可以自定义业务逻辑，如
 *     - 添加一些需要广播的用户
 *     - 删除一些不需要接收广播的用户
 *     - 可通过重写 logic、trick 方法来做一些额外扩展
 * </pre>
 * for example
 * <pre>{@code
 *         // example - 1
 *         new RangeBroadcast(flowContext)
 *                 // 需要广播的数据
 *                 .setResponseMessage(responseMessage)
 *                 // 添加需要接收广播的用户
 *                 .addUserId(1)
 *                 .addUserId(2)
 *                 .addUserId(List.of(3L, 4L, 5L))
 *                 // 排除一些用户，被排除的用户将不会接收到广播
 *                 .removeUserId(1)
 *                 .removeUserId(4)
 *                 // 执行广播，只有 2、3、5 可以接收到广播
 *                 .execute();
 *
 *         // example - 2
 *         new RangeBroadcast(flowContext)
 *                 // 需要广播的数据（路由、业务数据）
 *                 .setResponseMessage(cmdInfo, StringValue.of("hello"))
 *                 // 添加需要接收广播的用户
 *                 .addUserId(1)
 *                 // 执行广播
 *                 .execute();
 *
 *         // example - 3
 *         BrokerClientContext brokerClient = ...;
 *         var aggregationContext = brokerClient.getCommunicationAggregationContext();
 *         new RangeBroadcast(aggregationContext)
 *                  // 需要广播的数据（路由、业务数据）
 *                 .setResponseMessage(cmdInfo, StringValue.of("hello"))
 *                 // 添加需要接收广播的用户
 *                 .addUserId(1)
 *                 // 执行广播
 *                 .execute();
 * }
 * </pre>
 * 此外，还支持协议碎片及 List。关于协议碎片可阅读 <a href="https://www.yuque.com/iohao/game/ieimzn">协议碎片 - 文档</a>
 * for example
 * <pre>{@code
 *     // ------------ object ------------
 *     // 广播单个对象
 *     DemoBroadcastMessage message = new DemoBroadcastMessage();
 *     message.msg = "helloBroadcast --- 1";
 *
 *     new RangeBroadcast(flowContext)
 *             .setResponseMessage(cmdInfo, message);
 *
 *     List<DemoBroadcastMessage> messageList = List.of(message);
 *     new RangeBroadcast(flowContext)
 *             .setResponseMessageList(cmdInfo, messageList);
 *
 *     // ------------ int ------------
 *
 *     // 广播 int
 *     int intValue = 1;
 *     new RangeBroadcast(flowContext)
 *             .setResponseMessage(cmdInfo, intValue);
 *
 *     // 广播 int list
 *     List<Integer> intValueList = List.of(1, 2);
 *     new RangeBroadcast(flowContext)
 *             .setResponseMessageIntList(cmdInfo, intValueList);
 *
 *     // ------------ long ------------
 *
 *     // 广播 long
 *     long longValue = 1L;
 *     new RangeBroadcast(flowContext)
 *             .setResponseMessage(cmdInfo, longValue);
 *
 *     // 广播 long list
 *     List<Long> longValueList = List.of(1L, 2L);
 *     new RangeBroadcast(flowContext)
 *             .setResponseMessageLongList(cmdInfo, longValueList);
 *
 *     // ------------ String ------------
 *
 *     // 广播 String
 *     String stringValue = "1";
 *     new RangeBroadcast(flowContext)
 *             .setResponseMessage(cmdInfo, stringValue);
 *
 *     // 广播 String list
 *     List<String> stringValueList = List.of("1L", "2L");
 *     new RangeBroadcast(flowContext)
 *             .setResponseMessageStringList(cmdInfo, stringValueList);
 *
 *     // ------------ boolean ------------
 *
 *     // 广播 boolean
 *     boolean boolValue = true;
 *     new RangeBroadcast(flowContext)
 *             .setResponseMessage(cmdInfo, boolValue);
 *
 *     // 广播 boolean list
 *     List<Boolean> boolValueList = List.of(true, false);
 *     new RangeBroadcast(flowContext)
 *             .setResponseMessageBoolList(cmdInfo, boolValueList);
 * }
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-04-23
 * @since 21.8
 */
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RangeBroadcast implements RangeBroadcaster {
    @Getter(AccessLevel.PROTECTED)
    final CommunicationAggregationContext aggregationContext;
    /** 需要推送的 userId 列表 */
    @Getter(AccessLevel.PROTECTED)
    final Set<Long> userIds = new NonBlockingHashSet<>();
    /** 响应（广播）数据 ResponseMessage */
    @Getter(AccessLevel.PROTECTED)
    ResponseMessage responseMessage;
    /** 是否执行发送领域事件操作: true 执行推送操作 */
    boolean doSend = true;
    /** 检查 userIds ；当值为 true 时，userIds 必须有元素 */
    boolean checkEmptyUser = false;

    /**
     * create by CommunicationAggregationContext
     *
     * @param aggregationContext 网络通讯聚合接口
     */
    public RangeBroadcast(CommunicationAggregationContext aggregationContext) {
        Objects.requireNonNull(aggregationContext);
        this.aggregationContext = aggregationContext;
    }

    /**
     * create by CommunicationAggregationContext
     *
     * @param flowContext flowContext CommunicationAggregationContext
     */
    public RangeBroadcast(FlowContext flowContext) {
        this(flowContext.option(FlowAttr.aggregationContext));
    }

    /**
     * 检测空用户，如果没有任何用户，广播（推送）时将触发异常
     *
     * @return this
     */
    public RangeBroadcaster enableEmptyUserCheck() {
        this.checkEmptyUser = false;
        return this;
    }

    @Override
    public Set<Long> listUserId() {
        return this.userIds;
    }

    /**
     * 设置响应的广播数据 ResponseMessage
     *
     * @param responseMessage ResponseMessage
     * @return this
     */
    public RangeBroadcaster setResponseMessage(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
        return this;
    }

    /**
     * 响应消息到远程端（用户、玩家）
     * <pre>
     *     模板方法模式：
     *         在一个方法中定义一个算法的骨架，而将一些步骤延迟到子类中。
     *         模板方法使得子类可以在不改变算法结构的情况下，重新定义算法中的某些步骤。
     *
     *         要点：
     *         - “模板方法”定义了算法的步骤，把这些步骤的实现延迟到子类。
     *         - 模板方法模式为我们提供了一种代码复用的重要技巧。
     *         - 模板方法的抽象类可以定义具体方法、抽象方法和钩子。
     *         - 抽象方法由子类实现。
     *         - 钩子是一种方法，它在抽象类中不做事，或者只做默认的事情，子类可以选择要不要去覆盖它。
     *         - 为了防止子类改变模板方法中的算法，可以将模板方法声明为final。
     *         - 好莱坞原则告诉我们，将决策权放在高层模块中，以便决定如何以及何时调用低层模块。
     *         - 你将在真实世界代码中看到模板方法模式的许多变体，不要期待它们全都是一眼就可以被你一眼认出的。
     *         - 策略模式和模板方法模式都封装算法，一个用组合，一个用继承。
     *         - 工厂方法是模板方法的一种特殊版本。
     * </pre>
     */
    public final void execute() {
        // 子类可根据需要来构建响应内容
        this.logic();

        // 钩子方法，see disableSend()
        if (!this.doSend) {
            return;
        }

        // 在将数据推送前调用的钩子方法
        this.trick();

        Objects.requireNonNull(this.responseMessage);

        // 开始广播
        this.broadcast();
    }

    /**
     * 在将数据推送到调用方之前，触发的方法
     * <pre>
     *     可以做一些逻辑，在逻辑中可以决定是否执行推送
     *     {@code
     *         // 不执行推送数据的操作
     *         this.disableSend()
     *     }
     * </pre>
     */
    protected void logic() {
    }

    /**
     * 小把戏 (钩子方法)，子类可以做些其他的事情；执行广播（推送）之前，触发的方法。
     */
    protected void trick() {
    }

    /**
     * 广播数据
     */
    protected void broadcast() {
        boolean emptyUser = CollKit.isEmpty(this.userIds);
        if (checkEmptyUser && emptyUser) {
            ThrowKit.ofRuntimeException("没有添加消息推送人");
        }

        // 推送响应（广播消息）给指定的用户列表
        if (!emptyUser) {
            this.aggregationContext.broadcast(this.responseMessage, this.userIds);
        }
    }

    /**
     * 不执行推送数据的操作
     */
    protected void disableSend() {
        this.doSend = false;
    }
}
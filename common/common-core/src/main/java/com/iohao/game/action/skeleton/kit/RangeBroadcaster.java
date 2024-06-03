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

import com.iohao.game.action.skeleton.core.BarMessageKit;
import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.commumication.CommunicationAggregationContext;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.action.skeleton.protocol.wrapper.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 范围内的广播接口，这个范围指的是，指定某些用户进行广播。
 * <pre>
 *     在执行广播前，开发者可以自定义业务逻辑，如
 *     - 添加一些需要广播的用户
 *     - 删除一些不需要接收广播的用户
 * </pre>
 * for example
 * <pre>{@code
 *         // example - 1
 *         RangeBroadcaster.of(flowContext)
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
 *         RangeBroadcaster.of(flowContext)
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
 *         RangeBroadcaster.of(aggregationContext)
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
 *     // 广播 object
 *     DemoBroadcastMessage message = new DemoBroadcastMessage();
 *     message.msg = "helloBroadcast --- 1";
 *     RangeBroadcaster.of(flowContext)
 *             .setResponseMessage(cmdInfo, message);
 *
 *     // 广播 object list
 *     List<DemoBroadcastMessage> messageList = List.of(message);
 *     RangeBroadcaster.of(flowContext)
 *             .setResponseMessageList(cmdInfo, messageList);
 *
 *     // ------------ int ------------
 *     // 广播 int
 *     int intValue = 1;
 *     RangeBroadcaster.of(flowContext)
 *             .setResponseMessage(cmdInfo, intValue);
 *
 *     // 广播 int list
 *     List<Integer> intValueList = List.of(1, 2);
 *     RangeBroadcaster.of(flowContext)
 *             .setResponseMessageIntList(cmdInfo, intValueList);
 *
 *     // ------------ long ------------
 *     // 广播 long
 *     long longValue = 1L;
 *     RangeBroadcaster.of(flowContext)
 *             .setResponseMessage(cmdInfo, longValue);
 *
 *     // 广播 long list
 *     List<Long> longValueList = List.of(1L, 2L);
 *     RangeBroadcaster.of(flowContext)
 *             .setResponseMessageLongList(cmdInfo, longValueList);
 *
 *     // ------------ String ------------
 *     // 广播 String
 *     String stringValue = "1";
 *     RangeBroadcaster.of(flowContext)
 *             .setResponseMessage(cmdInfo, stringValue);
 *
 *     // 广播 String list
 *     List<String> stringValueList = List.of("1L", "2L");
 *     RangeBroadcaster.of(flowContext)
 *             .setResponseMessageStringList(cmdInfo, stringValueList);
 *
 *     // ------------ boolean ------------
 *     // 广播 boolean
 *     boolean boolValue = true;
 *     RangeBroadcaster.of(flowContext)
 *             .setResponseMessage(cmdInfo, boolValue);
 *
 *     // 广播 boolean list
 *     List<Boolean> boolValueList = List.of(true, false);
 *     RangeBroadcaster.of(flowContext)
 *             .setResponseMessageBoolList(cmdInfo, boolValueList);
 * }
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-06-02
 * @see RangeBroadcast impl
 * @since 21.9
 */
public interface RangeBroadcaster {
    /**
     * 接收广播的用户
     *
     * @return 接收广播的用户
     */
    Set<Long> listUserId();

    /**
     * 设置响应的广播数据 ResponseMessage
     *
     * @param responseMessage ResponseMessage
     * @return this
     */
    RangeBroadcaster setResponseMessage(ResponseMessage responseMessage);

    /**
     * 响应消息到远程端（用户、玩家）
     */
    void execute();

    /**
     * 创建一个默认的 RangeBroadcaster 对象（框架内置）
     *
     * @param aggregationContext 框架网络通讯聚合接口
     * @return RangeBroadcaster
     */
    static RangeBroadcaster of(CommunicationAggregationContext aggregationContext) {
        return new RangeBroadcast(aggregationContext);
    }

    /**
     * 创建一个默认的 RangeBroadcaster 对象（框架内置）
     *
     * @param flowContext FlowContext
     * @return RangeBroadcaster
     */
    static RangeBroadcaster of(FlowContext flowContext) {
        return new RangeBroadcast(flowContext);
    }

    /**
     * 接收广播的用户
     *
     * @param userIds userIds
     * @return this
     */
    default RangeBroadcaster addUserId(Collection<Long> userIds) {
        this.listUserId().addAll(userIds);
        return this;
    }

    /**
     * 接收广播的用户
     *
     * @param userId userId
     * @return this
     */
    default RangeBroadcaster addUserId(long userId) {
        this.listUserId().add(userId);
        return this;
    }

    /**
     * 添加接收广播的用户，顺带排除一个不需要接收广播的用户
     *
     * @param userIds       接收广播的 userIds
     * @param excludeUserId 需要排除的 userId
     * @return this
     */
    default RangeBroadcaster addUserId(Collection<Long> userIds, long excludeUserId) {
        return this.addUserId(userIds).removeUserId(excludeUserId);
    }

    /**
     * 排除 userId
     *
     * @param excludeUserId 需要排除的 userId
     * @return this
     */
    default RangeBroadcaster removeUserId(long excludeUserId) {
        if (excludeUserId > 0) {
            this.listUserId().remove(excludeUserId);
        }

        return this;
    }

    /**
     * 设置响应的广播数据
     *
     * @param cmdInfo 路由
     * @return this
     */
    default RangeBroadcaster setResponseMessage(CmdInfo cmdInfo) {
        var responseMessage = BarMessageKit.createResponseMessage(cmdInfo);
        return this.setResponseMessage(responseMessage);
    }

    /**
     * 设置响应的广播数据
     *
     * @param cmdInfo 路由
     * @param bizData 业务数据
     * @return this
     */
    default RangeBroadcaster setResponseMessage(CmdInfo cmdInfo, Object bizData) {
        var responseMessage = BarMessageKit.createResponseMessage(cmdInfo, bizData);
        return this.setResponseMessage(responseMessage);
    }

    /**
     * 设置响应的广播数据。业务数据会使用 {@link ByteValueList} 来包装（<a href="https://www.yuque.com/iohao/game/ieimzn">.协议碎片</a>）。
     *
     * @param cmdInfo 路由
     * @param bizData 业务数据
     * @return this
     */
    default RangeBroadcaster setResponseMessageList(CmdInfo cmdInfo, Collection<?> bizData) {
        var value = ByteValueList.ofList(bizData);
        return this.setResponseMessage(cmdInfo, value);
    }

    /**
     * 设置响应的广播数据。业务数据会使用 {@link IntValue} 来包装（<a href="https://www.yuque.com/iohao/game/ieimzn">.协议碎片</a>）。
     *
     * @param cmdInfo 路由
     * @param bizData 业务数据
     * @return this
     */
    default RangeBroadcaster setResponseMessage(CmdInfo cmdInfo, int bizData) {
        var value = IntValue.of(bizData);
        return this.setResponseMessage(cmdInfo, value);
    }

    /**
     * 设置响应的广播数据。业务数据会使用 {@link IntValueList} 来包装（<a href="https://www.yuque.com/iohao/game/ieimzn">.协议碎片</a>）。
     *
     * @param cmdInfo 路由
     * @param bizData 业务数据
     * @return this
     */
    default RangeBroadcaster setResponseMessageIntList(CmdInfo cmdInfo, List<Integer> bizData) {
        var value = IntValueList.of(bizData);
        return this.setResponseMessage(cmdInfo, value);
    }

    /**
     * 设置响应的广播数据。业务数据会使用 {@link LongValue} 来包装（<a href="https://www.yuque.com/iohao/game/ieimzn">.协议碎片</a>）。
     *
     * @param cmdInfo 路由
     * @param bizData 业务数据
     * @return this
     */
    default RangeBroadcaster setResponseMessage(CmdInfo cmdInfo, long bizData) {
        var value = LongValue.of(bizData);
        return this.setResponseMessage(cmdInfo, value);
    }

    /**
     * 设置响应的广播数据。业务数据会使用 {@link LongValueList} 来包装（<a href="https://www.yuque.com/iohao/game/ieimzn">.协议碎片</a>）。
     *
     * @param cmdInfo 路由
     * @param bizData 业务数据
     * @return this
     */
    default RangeBroadcaster setResponseMessageLongList(CmdInfo cmdInfo, List<Long> bizData) {
        var value = LongValueList.of(bizData);
        return this.setResponseMessage(cmdInfo, value);
    }

    /**
     * 设置响应的广播数据。业务数据会使用 {@link StringValue} 来包装（<a href="https://www.yuque.com/iohao/game/ieimzn">.协议碎片</a>）。
     *
     * @param cmdInfo 路由
     * @param bizData 业务数据
     * @return this
     */
    default RangeBroadcaster setResponseMessage(CmdInfo cmdInfo, String bizData) {
        var value = StringValue.of(bizData);
        return this.setResponseMessage(cmdInfo, value);
    }

    /**
     * 设置响应的广播数据。业务数据会使用 {@link StringValueList} 来包装（<a href="https://www.yuque.com/iohao/game/ieimzn">.协议碎片</a>）。
     *
     * @param cmdInfo 路由
     * @param bizData 业务数据
     * @return this
     */
    default RangeBroadcaster setResponseMessageStringList(CmdInfo cmdInfo, List<String> bizData) {
        var value = StringValueList.of(bizData);
        return this.setResponseMessage(cmdInfo, value);
    }

    /**
     * 设置响应的广播数据。业务数据会使用 {@link BoolValue} 来包装（<a href="https://www.yuque.com/iohao/game/ieimzn">.协议碎片</a>）。
     *
     * @param cmdInfo 路由
     * @param bizData 业务数据
     * @return this
     */
    default RangeBroadcaster setResponseMessage(CmdInfo cmdInfo, boolean bizData) {
        var value = BoolValue.of(bizData);
        return this.setResponseMessage(cmdInfo, value);
    }

    /**
     * 设置响应的广播数据。业务数据会使用 {@link BoolValueList} 来包装（<a href="https://www.yuque.com/iohao/game/ieimzn">.协议碎片</a>）。
     *
     * @param cmdInfo 路由
     * @param bizData 业务数据
     * @return this
     */
    default RangeBroadcaster setResponseMessageBoolList(CmdInfo cmdInfo, List<Boolean> bizData) {
        var value = BoolValueList.of(bizData);
        return this.setResponseMessage(cmdInfo, value);
    }
}

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
package com.iohao.game.action.skeleton.core.flow.attr;

import com.iohao.game.action.skeleton.core.commumication.BrokerClientContext;
import com.iohao.game.action.skeleton.core.commumication.ChannelContext;
import com.iohao.game.action.skeleton.core.commumication.CommunicationAggregationContext;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.common.kit.concurrent.executor.ThreadExecutor;

/**
 * flow 上下文的一些扩展属性
 * <pre>
 *     参考 {@link FlowContext}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-01-31
 */
public interface FlowAttr {
    /** 异常消息 */
    FlowOption<String> msgException = FlowOption.valueOf("msgException");
    /** 当前项目启动的服务上下文（当前服务器），see: BrokerClient */
    FlowOption<BrokerClientContext> brokerClientContext = FlowOption.valueOf("brokerClientContext");
    FlowOption<CommunicationAggregationContext> aggregationContext = FlowOption.valueOf("aggregationContext");
    /** 通信通道接口 */
    FlowOption<ChannelContext> channelContext = FlowOption.valueOf("channelContext");
    /** 逻辑服 id */
    FlowOption<String> logicServerId = FlowOption.valueOf("logicServerId");
    /** 逻辑服 tag 类型 */
    FlowOption<String> logicServerTag = FlowOption.valueOf("logicServerTag");
    /** action 中的业务参数 */
    FlowOption<Object> actionBizParam = FlowOption.valueOf("actionBizParam");
    /** 当前线程执行器 */
    FlowOption<ThreadExecutor> threadExecutor = FlowOption.valueOf("threadExecutor");
}

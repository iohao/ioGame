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
package com.iohao.game.action.skeleton.core.flow.attr;

import com.iohao.game.action.skeleton.core.commumication.BrokerClientContext;
import com.iohao.game.action.skeleton.core.commumication.ChannelContext;
import com.iohao.game.action.skeleton.core.flow.FlowContext;

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
    /** 通信通道接口 */
    FlowOption<ChannelContext> channelContext = FlowOption.valueOf("channelContext");
    /** 逻辑服 id */
    FlowOption<String> logicServerId = FlowOption.valueOf("logicServerId");
    /** 逻辑服 tag 类型 */
    FlowOption<String> logicServerTag = FlowOption.valueOf("logicServerTag");

    /** action 中的业务参数 */
    FlowOption<Object> actionBizParam = FlowOption.valueOf("actionBizParam");
    /**
     * 请求参数的业务数据
     * <pre>
     *     将在下个大版本中移除，请使用 FlowAttr.actionBizParam 代替。
     *     该属性当前已经失效。
     * </pre>
     */
    @Deprecated
    FlowOption<Object> data = FlowOption.valueOf("data");
}

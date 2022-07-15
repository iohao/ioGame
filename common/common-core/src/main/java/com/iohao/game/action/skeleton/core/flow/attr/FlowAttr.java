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

import com.alipay.remoting.AsyncContext;
import com.iohao.game.action.skeleton.core.commumication.BrokerClientContext;
import com.iohao.game.action.skeleton.core.flow.codec.DataCodec;
import com.iohao.game.action.skeleton.core.*;

/**
 * flow 上下文的一些扩展属性
 *
 * @author 渔民小镇
 * @date 2022-01-31
 */
public interface FlowAttr {
    /** 异常消息 */
    FlowOption<String> msgException = FlowOption.valueOf("msgException");
    /** 当前项目启动的服务上下文（当前服务器） */
    FlowOption<BrokerClientContext> brokerClientContext = FlowOption.valueOf("brokerClientContext");
    /** bolt async 上下文 */
    FlowOption<AsyncContext> asyncContext = FlowOption.valueOf("asyncContext");
    /** 请求参数的业务数据 */
    FlowOption<Object> data = FlowOption.valueOf("data");
    /**
     * 业务参数的编解码器
     * <pre>
     *     在业务框架开始处理前，框架会赋值
     *     see {@link BarSkeleton#handle}
     * </pre>
     */
    FlowOption<DataCodec> dataCodec = FlowOption.valueOf("dataCodec");
    /** 逻辑服 id */
    FlowOption<String> logicServerId = FlowOption.valueOf("logicServerId");
    /** 逻辑服 tag 类型 */
    FlowOption<String> logicServerTag = FlowOption.valueOf("logicServerTag");
}

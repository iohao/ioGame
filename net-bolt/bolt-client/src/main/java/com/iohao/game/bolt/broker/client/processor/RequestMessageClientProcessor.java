/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.bolt.broker.client.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.commumication.ChannelContext;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.FlowContextKit;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.bolt.broker.client.action.skeleton.BoltChannelContext;
import com.iohao.game.bolt.broker.core.aware.BrokerClientAware;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.bolt.broker.core.common.processor.hook.ClientProcessorHooks;
import com.iohao.game.bolt.broker.core.common.processor.hook.RequestMessageClientProcessorHook;

/**
 * 客户端请求处理器
 * <pre>
 *     通过业务框架把请求派发给指定的业务类来处理
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
public class RequestMessageClientProcessor extends AbstractAsyncUserProcessor<RequestMessage>
        implements BrokerClientAware {

    BrokerClient brokerClient;
    RequestMessageClientProcessorHook requestMessageClientProcessorHook;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, RequestMessage request) {
        /*
         * 多次访问的变量，保存到局部变量，可以提升性能。
         * 把成员变量的访问变为局部变量的访问 。 通过栈帧访问（线程栈），不用每次从堆中得到成员变量
         *
         * 因为这段代码访问频繁，才这样做。常规下不需要这么做
         * 可以参考 HashMap 的 putVal 方法相关
         */
        final BrokerClient brokerClient = this.brokerClient;

        // 得到逻辑服对应的业务框架
        BarSkeleton barSkeleton = brokerClient.getBarSkeleton();

        // 业务框架 flow 上下文
        FlowContext flowContext = barSkeleton
                // 业务框架 flow 上下文 工厂
                .getFlowContextFactory()
                // 创建 flow 上下文
                .createFlowContext();

        // 设置请求参数
        flowContext.setRequest(request);
        flowContext.setBarSkeleton(barSkeleton);

        // 动态属性添加
        ChannelContext channelContext = new BoltChannelContext(asyncCtx);
        flowContext.option(FlowAttr.channelContext, channelContext);
        flowContext.option(FlowAttr.brokerClientContext, brokerClient);
        flowContext.option(FlowAttr.logicServerId, brokerClient.getId());
        flowContext.option(FlowAttr.logicServerTag, brokerClient.getTag());

        // 设置 flowContext 的一些属性值
        FlowContextKit.employ(flowContext);

        // 执行业务框架
        this.requestMessageClientProcessorHook.processLogic(barSkeleton, flowContext);
    }

    @Override
    public void setBrokerClient(BrokerClient brokerClient) {
        this.brokerClient = brokerClient;

        ClientProcessorHooks clientProcessorHooks = brokerClient.getClientProcessorHooks();
        this.requestMessageClientProcessorHook = clientProcessorHooks.getRequestMessageClientProcessorHook();
    }

    @Override
    public String interest() {
        return RequestMessage.class.getName();
    }
}

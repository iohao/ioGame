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
package com.iohao.game.bolt.broker.client.processor;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;
import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.protocol.SyncRequestMessage;
import com.iohao.game.bolt.broker.core.aware.BrokerClientAware;
import com.iohao.game.bolt.broker.core.aware.UserProcessorExecutorAware;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.common.processor.hook.ClientProcessorHooks;
import com.iohao.game.bolt.broker.core.common.processor.hook.RequestMessageClientProcessorHook;
import lombok.Setter;

import java.util.concurrent.Executor;

/**
 * 通常这个消息处理器是用来处理，游戏逻辑服与同类型多个游戏逻辑服通信请求的
 *
 * @author 渔民小镇
 * @date 2022-08-20
 */
public class SyncRequestMessageClientProcessor extends SyncUserProcessor<SyncRequestMessage>
        implements BrokerClientAware, UserProcessorExecutorAware {

    BrokerClient brokerClient;
    RequestMessageClientProcessorHook requestMessageClientProcessorHook;
    @Setter
    Executor userProcessorExecutor;

    @Override
    public Object handleRequest(BizContext bizCtx, SyncRequestMessage request) throws Exception {

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
        var flowContext = barSkeleton
                // 业务框架 flow 上下文 工厂
                .getFlowContextFactory()
                // 创建 flow 上下文
                .createFlowContext()
                // 设置请求参数
                .setRequest(request)
                // 不需要业务框架来发送消息，由消息处理器来发送
                .setExecuteActionAfter(false);

        // 动态属性添加
        flowContext.option(FlowAttr.brokerClientContext, brokerClient);
        flowContext.option(FlowAttr.logicServerId, brokerClient.getId());
        flowContext.option(FlowAttr.logicServerTag, brokerClient.getTag());

        /*
         * 如果是同步的，就不支持线程编排了；
         * 使用默认线程就可以，通常这个消息处理器是用来处理
         * （游戏逻辑服与同类型多个游戏逻辑服通信请求的）
         */
        barSkeleton.handle(flowContext);

        // action 方法返回值是 void 的，不做处理
        ActionCommand actionCommand = flowContext.getActionCommand();
        if (actionCommand.getActionMethodReturnInfo().isVoid()) {
            return null;
        }

        return flowContext.getResponse();
    }

    @Override
    public String interest() {
        return SyncRequestMessage.class.getName();
    }

    @Override
    public void setBrokerClient(BrokerClient brokerClient) {
        this.brokerClient = brokerClient;

        ClientProcessorHooks clientProcessorHooks = brokerClient.getClientProcessorHooks();
        this.requestMessageClientProcessorHook = clientProcessorHooks.getRequestMessageClientProcessorHook();
    }

    @Override
    public Executor getExecutor() {
        return this.userProcessorExecutor;
    }
}

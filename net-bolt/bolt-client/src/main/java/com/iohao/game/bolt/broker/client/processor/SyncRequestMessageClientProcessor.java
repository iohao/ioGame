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
package com.iohao.game.bolt.broker.client.processor;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;
import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.protocol.SyncRequestMessage;
import com.iohao.game.bolt.broker.core.aware.BrokerClientAware;
import com.iohao.game.bolt.broker.core.aware.UserProcessorExecutorAware;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.common.processor.hook.ClientProcessorHooks;
import com.iohao.game.bolt.broker.core.common.processor.hook.RequestMessageClientProcessorHook;

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
        FlowContext flowContext = barSkeleton
                // 业务框架 flow 上下文 工厂
                .getFlowContextFactory()
                // 创建 flow 上下文
                .createFlowContext();

        // 设置请求参数
        flowContext.setRequest(request);
        // 不需要业务框架来发送消息，由消息处理器来发送
        flowContext.setExecuteActionAfter(false);

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

    @Override
    public void setUserProcessorExecutor(Executor executor) {
        this.userProcessorExecutor = executor;
    }

    @Override
    public Executor getUserProcessorExecutor() {
        return this.userProcessorExecutor;
    }
}

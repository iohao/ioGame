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

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.bolt.broker.core.aware.BrokerClientAware;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import com.iohao.game.bolt.broker.core.common.processor.hook.ClientProcessorHooks;
import com.iohao.game.bolt.broker.core.common.processor.hook.RequestMessageClientProcessorHook;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端请求处理器
 * <pre>
 *     通过业务框架把请求派发给指定的业务类来处理
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Slf4j
public class RequestMessageClientProcessor extends AsyncUserProcessor<RequestMessage> implements BrokerClientAware {

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

        if (BrokerGlobalConfig.requestResponseLog) {
            log.info("逻辑服处理请求 --- {} - {}", brokerClient.getAppName(), brokerClient.getId());
        }

        // 业务框架 flow 上下文
        var flowContext = new FlowContext().setRequest(request);

        // 动态属性添加
        flowContext.option(FlowAttr.asyncContext, asyncCtx);
        flowContext.option(FlowAttr.brokerClientContext, brokerClient);
        flowContext.option(FlowAttr.logicServerId, brokerClient.getId());
        flowContext.option(FlowAttr.logicServerTag, brokerClient.getTag());

        // 得到逻辑服对应的业务框架
        BarSkeleton barSkeleton = brokerClient.getBarSkeleton();

        this.requestMessageClientProcessorHook.processLogic(barSkeleton, flowContext);
    }

    @Override
    public void setBrokerClient(BrokerClient brokerClient) {
        this.brokerClient = brokerClient;

        ClientProcessorHooks clientProcessorHooks = brokerClient.getClientProcessorHooks();
        this.requestMessageClientProcessorHook = clientProcessorHooks.getRequestMessageClientProcessorHook();
    }

    /**
     * 指定感兴趣的请求数据类型，该 UserProcessor 只对感兴趣的请求类型的数据进行处理；
     * 假设 除了需要处理 MyRequest 类型的数据，还要处理 java.lang.String 类型，有两种方式：
     * 1、再提供一个 UserProcessor 实现类，其 interest() 返回 java.lang.String.class.getName()
     * 2、使用 MultiInterestUserProcessor 实现类，可以为一个 UserProcessor 指定 List<String> multiInterest()
     *
     * @return 自定义处理器
     */
    @Override
    public String interest() {
        return RequestMessage.class.getName();
    }
}

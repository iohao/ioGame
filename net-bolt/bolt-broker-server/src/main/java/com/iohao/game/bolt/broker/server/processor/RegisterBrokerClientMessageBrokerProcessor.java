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
package com.iohao.game.bolt.broker.server.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.iohao.game.bolt.broker.cluster.BrokerClusterManager;
import com.iohao.game.bolt.broker.cluster.BrokerRunModeEnum;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.bolt.broker.core.message.BrokerClusterMessage;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.kit.BrokerPrintKit;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 模块注册
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Slf4j
public class RegisterBrokerClientMessageBrokerProcessor extends AsyncUserProcessor<BrokerClientModuleMessage> implements BrokerServerAware {
    @Setter
    BrokerServer brokerServer;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, BrokerClientModuleMessage brokerClientModuleMessage) {

        String remoteAddress = bizCtx.getRemoteAddress();
        brokerClientModuleMessage.setAddress(remoteAddress);

        // 注册到负载器中
        BalancedManager balancedManager = brokerServer.getBalancedManager();
        balancedManager.register(brokerClientModuleMessage);

        // 发送网关集群消息给客户端 （逻辑服）
        if (brokerServer.getBrokerRunMode() == BrokerRunModeEnum.CLUSTER) {
            this.sendClusterMessage(bizCtx);
        }

        print(brokerClientModuleMessage);
    }


    private void sendClusterMessage(BizContext bizCtx) {

        BrokerClusterManager brokerClusterManager = brokerServer.getBrokerClusterManager();
        BrokerClusterMessage brokerClusterMessage = brokerClusterManager.getBrokerClusterMessage();

        if (BrokerGlobalConfig.openLog) {
            log.info("broker（游戏网关）: [{}] --  集群数量[{}] - 详细：[{}]"
                    , this.brokerServer.getPort()
                    , brokerClusterMessage.count()
                    , brokerClusterMessage);
        }

        try {
            this.brokerServer.getRpcServer().oneway(bizCtx.getConnection(), brokerClusterMessage);
        } catch (RemotingException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void print(BrokerClientModuleMessage brokerClientModuleMessage) {
        int port = this.brokerServer.getPort();
        if (BrokerGlobalConfig.openLog) {
            log.info("模块注册信息 --- 网关port: [{}] --- {}", port, brokerClientModuleMessage);
        }

        BrokerPrintKit.print(this.brokerServer);
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
        return BrokerClientModuleMessage.class.getName();
    }
}

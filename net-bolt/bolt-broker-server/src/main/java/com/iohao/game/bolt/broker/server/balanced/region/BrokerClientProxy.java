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
package com.iohao.game.bolt.broker.server.balanced.region;

import com.alibaba.fastjson2.annotation.JSONField;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcServer;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.bolt.broker.core.client.BrokerClientType;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.common.kit.ToJson;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

/**
 * 模块信息代理
 * <pre>
 *     这里的模块指的是逻辑服信息
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Getter
@Setter
@ToString
@SuppressWarnings("unchecked")
public class BrokerClientProxy implements ToJson {
    /** 逻辑服唯一标识 */
    final String id;
    final int idHash;
    /** 逻辑服名 */
    final String name;
    /** 逻辑服地址 */
    final String address;
    /**
     * 逻辑服标签 （tag 相当于归类）
     * <pre>
     *     用于逻辑服的归类
     *     假设逻辑服： 战斗逻辑服 启动了两台或以上，为了得到启动连接的逻辑服，我们可以通过 tag 在后台查找
     *     相同的逻辑服一定要用相同的 tag
     *
     *     注意，如果没设置这个值，会使用 this.name 的值
     * </pre>
     */
    final String tag;

    /** 逻辑服类型 */
    final BrokerClientType brokerClientType;

    /** 消息发送超时时间 */
    int timeoutMillis = BrokerGlobalConfig.timeoutMillis;
    List<Integer> cmdMergeList;
    @JSONField(serialize = false)
    final RpcServer rpcServer;

    public BrokerClientProxy(BrokerClientModuleMessage brokerClientModuleMessage, RpcServer rpcServer) {
        this.id = brokerClientModuleMessage.getId();
        this.idHash = brokerClientModuleMessage.getIdHash();
        this.name = brokerClientModuleMessage.getName();
        this.address = brokerClientModuleMessage.getAddress();
        this.tag = brokerClientModuleMessage.getTag();
        this.brokerClientType = brokerClientModuleMessage.getBrokerClientType();
        this.cmdMergeList = brokerClientModuleMessage.getCmdMergeList();

        this.rpcServer = rpcServer;
    }

    public <T> T invokeSync(RequestMessage requestMessage) throws RemotingException, InterruptedException {
        return (T) rpcServer.invokeSync(address, requestMessage, timeoutMillis);
    }

    public void oneway(Object request) throws RemotingException, InterruptedException {
        rpcServer.oneway(address, request);
    }

    public Object invokeSync(ResponseMessage responseMessage) throws RemotingException, InterruptedException {
        return rpcServer.invokeSync(address, responseMessage, timeoutMillis);
    }

    public <T> T invokeSync(Object message) throws RemotingException, InterruptedException {
        return (T) rpcServer.invokeSync(address, message, timeoutMillis);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof BrokerClientProxy that)) {
            return false;
        }

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

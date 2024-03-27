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
package com.iohao.game.bolt.broker.server.balanced.region;

import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcServer;
import com.iohao.game.bolt.broker.core.client.BrokerClientType;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

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
public class BrokerClientProxy {
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
    final RpcServer rpcServer;
    final int withNo;

    /** 状态 */
    int status;

    /** 消息发送超时时间 */
    int timeoutMillis = IoGameGlobalConfig.timeoutMillis;
    List<Integer> cmdMergeList;

    public BrokerClientProxy(BrokerClientModuleMessage brokerClientModuleMessage, RpcServer rpcServer) {
        this.id = brokerClientModuleMessage.getId();
        this.idHash = brokerClientModuleMessage.getIdHash();
        this.name = brokerClientModuleMessage.getName();
        this.address = brokerClientModuleMessage.getAddress();
        this.tag = brokerClientModuleMessage.getTag();
        this.brokerClientType = brokerClientModuleMessage.getBrokerClientType();
        this.cmdMergeList = brokerClientModuleMessage.getCmdMergeList();
        this.rpcServer = rpcServer;
        this.withNo = brokerClientModuleMessage.getWithNo();
        this.status = brokerClientModuleMessage.getStatus();
    }

    public void oneway(Object request) throws RemotingException, InterruptedException {
        rpcServer.oneway(address, request);
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
        return id.hashCode();
    }
}

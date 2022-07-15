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
package com.iohao.game.action.skeleton.protocol;

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.CmdInfoFlyweightFactory;
import com.iohao.game.action.skeleton.core.CmdKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

/**
 * 元信息
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
@Accessors(chain = true)
public class HeadMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = -472575113683576693L;

    /** userId */
    long userId;
    /** 目标路由 */
    int cmd;
    /** 目标子路由 */
    int subCmd;
    /**
     * 合并两个参数,分别存放在 [高16 和 低16]
     * <pre>
     *     例如 cmd = 600; subCmd = 700;
     *     merge 的结果: 39322300
     *     那么merge对应的二进制是: 0000 0010 0101 1000 0000 0010 1011 1100
     * </pre>
     */
    int cmdMerge;

    /**
     * 来源逻辑服 client id
     * <pre>
     *     比如是对外服发起的请求，这个来源就是对外服的 clientId
     *     clientId 指的是 服务器的唯一id
     *
     *     see {@link com.iohao.game.common.kit.MurmurHash3}
     * </pre>
     */
    int sourceClientId;

    /**
     * 目标逻辑服 endPointClientId
     * <pre>
     *     用于指定请求由哪个服务器处理
     *
     *     比如两个象棋服（游戏逻辑服） A，B
     *     玩家李雷和韩梅梅在对局时，如果在 象棋服A 玩的，那么之后的请求都要分配给 象棋服A 来处理。
     *
     *     endPointClientId 指的是 服务器的唯一id
     *
     *     see {@link com.iohao.game.common.kit.MurmurHash3}
     * </pre>
     */
    int endPointClientId;

    /**
     * rpc type
     * <pre>
     *     see : {@link com.alipay.remoting.rpc.RpcCommandType}
     *
     *     在 bolt 中， 调用方使用
     *     （{@link com.alipay.remoting.rpc.RpcServer#oneway}
     *     或 {@link com.alipay.remoting.rpc.RpcClient#oneway}）的 oneway 方法
     *
     *     则 AsyncContext.sendResponse 无法回传响应
     *     原因可阅读 {@link com.alipay.remoting.rpc.protocol.RpcRequestProcessor#sendResponseIfNecessary} 源码。
     *
     *
     *     业务框架保持与 bolt 的风格一至使用 RpcCommandType
     *     不同的是业务框架会用 RpcCommandType 区别使用什么方式来发送响应。
     *
     *     如果 rpcCommandType != RpcCommandType.REQUEST_ONEWAY ,
     *     就使用 {@link com.alipay.remoting.AsyncContext#sendResponse} 来发送响应
     *     具体发送逻辑可读 {@link com.iohao.game.action.skeleton.core.flow.interal.DefaultActionAfter} 源码
     *
     * </pre>
     */
    byte rpcCommandType;

    /**
     * json 扩展字段
     * <pre>
     *     开发者有特殊业务可以通过这个字段来扩展元信息，该字段的信息会跟随每一个请求；
     *
     *     框架只会在玩家没有登录的时候，临时的使用了一下这个字段来存放 netty 的 channelId。
     *     原因是不想为了这一次的使用，而特意声明一个变量来存放 netty 的 channelId。
     *     简单点说就是在玩家没有登录时，开发者是不能使用该字段的。
     *     只登录了的玩家，框架才不会占用该字段来存放 netty 的 channelId。
     *
     *     解释：
     *     框架存放 netty 的 channelId 是为了能在对外服查找到对应的连接，
     *     一但玩家登录后，将会使用玩家的 userId 而不是 channelId 来查找对应的连接（channel），
     *     因为 channelId 的字符串实在太长了，每次将该值传输到逻辑服会小小的影响性能。
     *     出于这个原因就这样约定使用了。
     * </pre>
     */
    String extJsonField;

    public HeadMetadata setCmdInfo(CmdInfo cmdInfo) {
        this.cmd = cmdInfo.getCmd();
        this.subCmd = cmdInfo.getSubCmd();
        this.cmdMerge = cmdInfo.getCmdMerge();

        return this;
    }

    public CmdInfo getCmdInfo() {
        return CmdInfoFlyweightFactory.me().getCmdInfo(this.cmd, this.subCmd);
    }

    public HeadMetadata setCmdMerge(int cmdMerge) {
        this.cmdMerge = cmdMerge;
        this.cmd = CmdKit.getCmd(cmdMerge);
        this.subCmd = CmdKit.getSubCmd(cmdMerge);

        return this;
    }
}

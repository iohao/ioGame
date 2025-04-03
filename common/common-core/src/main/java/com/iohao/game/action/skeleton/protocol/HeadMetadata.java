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
package com.iohao.game.action.skeleton.protocol;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.flow.internal.DefaultActionAfter;
import com.iohao.game.action.skeleton.kit.ExecutorSelectEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

/**
 * Meta information
 * <p>
 * 元信息
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public final class HeadMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = -472575113683576693L;

    /** userId */
    long userId;

    /**
     * 合并两个参数,分别存放在 [高16 和 低16]
     * <pre>
     *     cmd 目标路由
     *     subCmd 目标子路由
     *
     *     例如 cmd = 600; subCmd = 700;
     *     merge 的结果: 39322300
     *     那么merge对应的二进制是: 0000 0010 0101 1000 0000 0010 1011 1100
     *
     *     see {@link CmdInfo}
     * </pre>
     */
    int cmdMerge;

    /**
     * Source logic server client ID
     * <p>
     * If the request is initiated by an external server,
     * this value should be the external server's clientId (a server's unique identifier).
     * <p>
     * 来源逻辑服 client id
     * <pre>
     *     比如是对外服发起的请求，这个来源就是对外服的 clientId
     *     clientId 指的是 服务器的唯一id
     *
     *     see {@link com.iohao.game.common.kit.HashKit}
     * </pre>
     */
    int sourceClientId;

    /**
     * Target logic server endPointClientId
     * Used to specify which server should process the request
     * <p>
     * For example, with two chess servers (game logic servers) A and B:
     * When players Player1 and Player2 are in a match, if they started playing on Chess Server A,
     * then all subsequent requests must be assigned to Chess Server A for processing.
     * <p>
     * endPointClientId refers to the server's unique identifier.
     * <p>
     * see {@link com.iohao.game.common.kit.HashKit}
     * <p>
     * 目标逻辑服 endPointClientId
     * <pre>
     *     用于指定请求由哪个服务器处理
     *
     *     比如两个象棋服（游戏逻辑服） A，B
     *     玩家李雷和韩梅梅在对局时，如果在 象棋服A 玩的，那么之后的请求都要分配给 象棋服A 来处理。
     *
     *     endPointClientId 指的是 服务器的唯一id
     *
     *     see {@link com.iohao.game.common.kit.HashKit}
     * </pre>
     */
    int endPointClientId;

    /**
     * rpc type
     * <pre>
     *     see : com.alipay.remoting.rpc.RpcCommandType
     *
     *     在 bolt 中， 调用方使用 com.alipay.remoting.rpc.RpcServer或 com.alipay.remoting.rpc.RpcClient 的 oneway 方法，
     *     则 AsyncContext.sendResponse 无法回传响应；原因可阅读 com.alipay.remoting.rpc.protocol.RpcRequestProcessor#sendResponseIfNecessary 源码。
     *
     *     业务框架保持与 bolt 的风格一至，使用 RpcCommandType。不同的是，业务框架会用 RpcCommandType 区别使用什么方式来发送响应。
     *
     *     如果 rpcCommandType != RpcCommandType.REQUEST_ONEWAY，就使用 com.alipay.remoting.AsyncContext#sendResponse 来发送响应。
     *     具体发送逻辑可读 {@link DefaultActionAfter} 源码
     * </pre>
     */
    byte rpcCommandType;

    /**
     * Extended field. Developers can use this field to extend meta-information for special business needs.
     * The data in this field will be included with every request.
     * <p>
     * 扩展字段，开发者有特殊业务可以通过这个字段来扩展元信息，该字段的信息会跟随每一个请求。
     */
    byte[] attachmentData;

    /**
     * netty channelId
     * <p>
     * The framework stores Netty's channelId to allow the external server to look up the corresponding connection.
     * <p>
     * After a player logs in, the framework will use the player's userId to locate the corresponding connection (channel) instead of the channelId,
     * because the channelId string is too long, and transmitting this value to the logic server each time would slightly impact performance.
     * <p>
     * Once the player logs in, the framework will no longer use this property.
     * However, if needed, developers can repurpose this field for their own use.
     * <pre>
     *     框架存放 netty 的 channelId 是为了能在对外服查找到对应的连接。
     *     当玩家登录后，框架将会使用玩家的 userId 来查找对应的连接（channel），而不是 channelId ；
     *     因为 channelId 的字符串太长了，每次将该值传输到逻辑服会小小的影响性能。
     *
     *     当玩家登录后，框架将不会使用该属性了；开发者如果有需要，可以把这个字段利用起来。
     * </pre>
     */
    String channelId;

    /** 消息标记号；由前端请求时设置，服务器响应时会携带上 */
    int msgId;

    /**
     * Framework's internal fields. Future changes are likely to be significant, so developers should refrain from using them.
     * <p>
     * 框架自用字段。将来变化可能较大，开发者请不要使用。
     */
    int stick;

    /**
     * The IDs of multiple game logic servers bound to the player
     * <pre>
     *     All requests related to this game logic server will be routed to the bound game logic server for processing.
     *     Even if multiple game logic servers of the same type are running, requests will still be directed to the originally bound server.
     * </pre>
     * <p>
     * 玩家绑定的多个游戏逻辑服 id
     * <pre>
     *     所有与该游戏逻辑服相关的请求都将被分配给已绑定的游戏逻辑服处理。
     *     即使启动了多个同类型的游戏逻辑服，该请求仍将被分配给已绑定的游戏逻辑服处理。
     * </pre>
     */
    int[] bindingLogicServerIds;
    /**
     * Framework's internal fields. Future changes are likely to be significant, so developers should refrain from using them.
     * <p>
     * 框架自用字段。将来变化可能较大，开发者请不要使用。
     */
    int cacheCondition;
    /**
     * Custom data, a field reserved specifically for developers.
     * Developers can use this field to pass custom data.
     * The field is entirely user-defined—the framework will neither process nor validate its contents.
     * Developers can leverage it to transmit any data, including custom objects.
     * <p>
     * 自定义数据，专为开发者预留的一个字段，开发者可以利用该字段来传递自定义数据。
     * 该字段由开发者自己定义，框架不会对数据做任何处理，也不会做任何检查，开发者可以利用该字段来传递任何数据，包括自定义对象。
     */
    byte[] customData;
    ExecutorSelectEnum executorSelect;
    String traceId;
    /**
     * Framework's internal fields. Future changes are likely to be significant, so developers should refrain from using them.
     * <p>
     * 框架自用字段。将来变化可能较大，开发者请不要使用。
     */
    byte[] userProcessorExecutorSelectorBytes;
    /**
     * Temporary variable
     * <p>
     * 临时变量
     */
    transient Object other;
    transient int withNo;
    /**
     * Request command type: 0 Heartbeat, 1 Business
     * <p>
     * 请求命令类型: 0 心跳，1 业务
     */
    transient int cmdCode;
    /**
     * Protocol switch: Used for protocol-level control (e.g., security encryption verification). 0: No verification
     * <p>
     * 协议开关，用于一些协议级别的开关控制，比如 安全加密校验等。 : 0 不校验
     */
    transient int protocolSwitch;
    /** 预留 inet */
    @Ignore
    transient Object inetSocketAddress;
    /** 原始的游戏对外服协议引用（开发者可自定义，这里会保存一个引用，方便特殊业务获取） */
    @Ignore
    transient Object externalMessage;

    public HeadMetadata setCmdInfo(CmdInfo cmdInfo) {
        this.cmdMerge = cmdInfo.getCmdMerge();
        return this;
    }

    public CmdInfo getCmdInfo() {
        return CmdInfo.of(this.cmdMerge);
    }

    public HeadMetadata setCmdMerge(int cmdMerge) {
        this.cmdMerge = cmdMerge;
        return this;
    }

    /**
     * Simple clone
     * <p>
     * Usage scenario （使用场景）
     * <pre>
     *     Can be used when communicating with other game logic servers.
     *     The method assigns necessary player attributes to HeadMetadata.
     *
     *     （与其他游戏逻辑服通信时可以使用，方法中给 HeadMetadata 赋值了玩家的必要属性）
     *     userId、attachmentData、channelId、bindingLogicServerIds、customData
     *     traceId、executorSelect
     * </pre>
     * Unassigned by default. Set manually when required. （以下属性不会赋值，如有需要，请自行赋值）
     * <pre>
     *     cmdMerge
     *     sourceClientId
     *     endPointClientId
     *     rpcCommandType
     *     msgId
     * </pre>
     *
     * @return HeadMetadata
     */
    public HeadMetadata cloneHeadMetadata() {

        HeadMetadata headMetadata = new HeadMetadata();
        headMetadata.userId = this.userId;
        headMetadata.attachmentData = this.attachmentData;
        headMetadata.channelId = this.channelId;
        headMetadata.bindingLogicServerIds = this.bindingLogicServerIds;
        headMetadata.customData = this.customData;
        headMetadata.traceId = this.traceId;
        headMetadata.executorSelect = this.executorSelect;

        return headMetadata;
    }

    public HeadMetadata cloneAll() {
        HeadMetadata headMetadata = cloneHeadMetadata();
        headMetadata.cmdMerge = this.cmdMerge;
        headMetadata.sourceClientId = this.sourceClientId;
        headMetadata.endPointClientId = this.endPointClientId;
        headMetadata.rpcCommandType = this.rpcCommandType;
        headMetadata.msgId = this.msgId;

        headMetadata.stick = this.stick;
        headMetadata.cacheCondition = this.cacheCondition;

        headMetadata.other = this.other;
        headMetadata.withNo = this.withNo;
        headMetadata.cmdCode = this.cmdCode;
        headMetadata.protocolSwitch = this.protocolSwitch;
        headMetadata.inetSocketAddress = this.inetSocketAddress;
        headMetadata.externalMessage = this.externalMessage;

        return headMetadata;
    }

    @SuppressWarnings("unchecked")
    public <T> T getExternalMessage() {
        return (T) this.externalMessage;
    }
}

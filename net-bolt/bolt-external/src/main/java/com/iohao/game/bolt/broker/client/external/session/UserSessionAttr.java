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
package com.iohao.game.bolt.broker.client.external.session;

import com.iohao.game.action.skeleton.protocol.processor.EndPointLogicServerMessage;
import io.netty.util.AttributeKey;

/**
 * 扩展属性相关
 *
 * @author 渔民小镇
 * @date 2022-01-11
 */
public interface UserSessionAttr {
    /** false : 没有进行身份验证 */
    AttributeKey<Boolean> verifyIdentity = AttributeKey.valueOf("verifyIdentity");
    /** 用户 session，与channel是 1:1 的关系 */
    AttributeKey<UserSession> userSession = AttributeKey.valueOf("userSession");
    /**
     * 给用户绑定逻辑服
     * <pre>
     *     之后与该逻辑服有关的请求，都会分配给这个逻辑服来处理。
     *     意思是无论启动了多少个同类型的逻辑服，都会给到这个逻辑服来处理。
     *
     *     see {@link com.iohao.game.common.kit.HashKit#hash32(String)}
     *     see {@link EndPointLogicServerMessage#addLogicServerId(String)}
     * </pre>
     */
    AttributeKey<Integer> endPointLogicServerId = AttributeKey.valueOf("endPointLogicServerId");
    /** 元信息 */
    AttributeKey<byte[]> attachment = AttributeKey.valueOf("attachment");
}
/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
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
     *     see {@link com.iohao.game.common.kit.MurmurHash3#hash32(String)}
     *     see {@link EndPointLogicServerMessage#getLogicServerId()}
     * </pre>
     */
    AttributeKey<Integer> endPointLogicServerId = AttributeKey.valueOf("endPointLogicServerId");
}
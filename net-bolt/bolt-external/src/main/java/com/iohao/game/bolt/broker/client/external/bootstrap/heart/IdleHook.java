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
package com.iohao.game.bolt.broker.client.external.bootstrap.heart;

import com.iohao.game.bolt.broker.client.external.session.UserSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 心跳钩子事件回调
 * <pre>
 *     关于心跳相关的可以参考这里：
 *     https://www.yuque.com/iohao/game/uueq3i
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-03-13
 */
public interface IdleHook {
    /**
     * 心跳事件回调
     * <pre>
     *     这里只需要做你的业务就可以了，比如通知房间内的其他玩家，该用户下线了。
     * </pre>
     *
     * @param ctx         ctx
     * @param event       event
     * @param userSession 当前触发的用户
     * @return true : 通知游戏框架关闭 ctx （ctx.close()）
     */
    default boolean callback(ChannelHandlerContext ctx, IdleStateEvent event, UserSession userSession) {
        return false;
    }
}

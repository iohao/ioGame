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

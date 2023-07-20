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
package com.iohao.game.external.core.hook;

import com.iohao.game.external.core.session.UserSession;

/**
 * 心跳相关
 * <pre>
 *     参考 <a href="https://www.yuque.com/iohao/game/uueq3i">心跳设置与心跳钩子-文档</a>
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-02-18
 */
public interface IdleHook<IdleEvent> {
    /**
     * 心跳事件回调
     * <pre>
     *     这里只需要做你的业务就可以了，比如通知房间内的其他玩家，该用户下线了。
     * </pre>
     *
     * @param userSession userSession
     * @param event       event
     * @return true 断开玩家连接
     */
    boolean callback(UserSession userSession, IdleEvent event);
}

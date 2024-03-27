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
package com.iohao.game.action.skeleton.core.commumication;

import com.iohao.game.action.skeleton.core.BarMessageKit;
import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;

import java.util.Collection;

/**
 * 广播通讯上下文
 * <pre>
 *     see:
 *     <a href="https://www.yuque.com/iohao/game/qv4qfo#nJage">广播、推送</a>
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-18
 */
public interface BroadcastContext {
    /**
     * 广播消息给指定用户列表
     *
     * @param responseMessage 消息
     * @param userIdList      指定用户列表 (如果为 null 或 empty 就不会触发)
     */
    void broadcast(ResponseMessage responseMessage, Collection<Long> userIdList);

    /**
     * 广播消息给单个用户
     *
     * @param responseMessage 消息
     * @param userId          userId
     */
    void broadcast(ResponseMessage responseMessage, long userId);

    /**
     * 全服广播
     *
     * @param responseMessage 消息
     */
    void broadcast(ResponseMessage responseMessage);

    /**
     * 全服广播
     *
     * @param cmdInfo 广播到此路由
     * @param bizData 业务数据
     */
    default void broadcast(CmdInfo cmdInfo, Object bizData) {
        ResponseMessage responseMessage = BarMessageKit.createResponseMessage(cmdInfo, bizData);
        this.broadcast(responseMessage);
    }

    /**
     * 广播消息给指定用户列表
     *
     * @param cmdInfo    广播到此路由
     * @param bizData    业务数据
     * @param userIdList 指定用户列表
     */
    default void broadcast(CmdInfo cmdInfo, Object bizData, Collection<Long> userIdList) {
        ResponseMessage responseMessage = BarMessageKit.createResponseMessage(cmdInfo, bizData);
        this.broadcast(responseMessage, userIdList);
    }

    /**
     * 广播消息给单个用户
     *
     * @param cmdInfo 广播到此路由
     * @param bizData 业务数据
     * @param userId  userId
     */
    default void broadcast(CmdInfo cmdInfo, Object bizData, long userId) {
        ResponseMessage responseMessage = BarMessageKit.createResponseMessage(cmdInfo, bizData);
        this.broadcast(responseMessage, userId);
    }
}

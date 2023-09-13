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
package com.iohao.game.external.core.session;

import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.kit.attr.AttrOptionDynamic;
import com.iohao.game.external.core.hook.UserHook;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * UserSession 管理器
 *
 * @author 渔民小镇
 * @date 2023-02-18
 */
public interface UserSessions<SessionContext, Session extends UserSession> extends AttrOptionDynamic {

    /**
     * 加入到 session 管理
     *
     * @param sessionContext sessionContext
     */
    Session add(SessionContext sessionContext);

    /**
     * 获取 UserSession
     *
     * @param sessionContext sessionContext
     * @return SessionContext
     */
    Session getUserSession(SessionContext sessionContext);

    /**
     * 获取 UserSession
     *
     * @param userId userId
     * @return UserSession
     */
    Session getUserSession(long userId);

    /**
     * getUserSession
     *
     * @param userChannelId userChannelId
     * @return userSession
     */
    Session getUserSession(UserChannelId userChannelId);

    /**
     * 如果 UserSession 存在，则使用该值执行给定操作，否则不执行任何操作。
     *
     * @param userId   userId
     * @param consumer 如果 UserSession 存在，则要执行的动作
     */
    default void ifPresent(long userId, Consumer<Session> consumer) {
        Session userSession = this.getUserSession(userId);
        if (Objects.nonNull(userSession)) {
            consumer.accept(userSession);
        }
    }

    /**
     * 如果 UserSession 存在，则使用该值执行给定操作，否则不执行任何操作。
     *
     * @param userIdList userIdList 不能为 null
     * @param consumer   如果 UserSession 存在，则要执行的动作
     */
    default void ifPresent(Collection<Long> userIdList, Consumer<Session> consumer) {
        // 不做 null 判断了，让调用方保证
        userIdList.stream()
                .map(this::getUserSession)
                .filter(Objects::nonNull)
                .forEach(consumer);
    }

    /**
     * true 用户存在
     *
     * @param userId 用户id
     * @return true 用户存在
     */
    boolean existUserSession(long userId);

    /**
     * 设置 channel 的 userId
     *
     * @param userChannelId userChannelId
     * @param userId        userId
     * @return true 设置成功
     */
    boolean settingUserId(UserChannelId userChannelId, long userId);

    /**
     * 移除 UserSession
     *
     * @param userSession userSession
     */
    void removeUserSession(Session userSession);

    /**
     * 根据 userId 移除 UserSession ，在移除前发送一个消息
     * <pre>
     *     玩家存在时会触发
     * </pre>
     *
     * @param userId userId
     * @param msg    msg
     */
    void removeUserSession(long userId, Object msg);

    /**
     * userHook
     *
     * @param userHook userHook
     */
    void setUserHook(UserHook userHook);

    /**
     * 当前在线人数
     *
     * @return 当前在线人数
     */
    int countOnline();

    /**
     * 全员消息广播
     * 消息类型 ExternalMessage
     *
     * @param msg 消息
     */
    void broadcast(Object msg);

    /**
     * 遍历所有玩家
     *
     * @param consumer consumer
     */
    void forEach(Consumer<Session> consumer);
}

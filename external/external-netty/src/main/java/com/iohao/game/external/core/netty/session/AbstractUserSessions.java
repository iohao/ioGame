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
package com.iohao.game.external.core.netty.session;

import com.iohao.game.bolt.broker.core.aware.CmdRegionsAware;
import com.iohao.game.common.kit.attr.AttrOptions;
import com.iohao.game.core.common.cmd.CmdRegions;
import com.iohao.game.external.core.config.ExternalJoinEnum;
import com.iohao.game.external.core.hook.UserHook;
import com.iohao.game.external.core.session.UserChannelId;
import com.iohao.game.external.core.session.UserSession;
import com.iohao.game.external.core.session.UserSessionOption;
import com.iohao.game.external.core.session.UserSessions;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;
import org.jctools.maps.NonBlockingHashMapLong;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * UserSessions 抽象父类
 *
 * @author 渔民小镇
 * @date 2023-05-28
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
abstract class AbstractUserSessions<ChannelHandlerContext, Session extends UserSession>
        implements UserSessions<ChannelHandlerContext, Session>, CmdRegionsAware {

    @Getter
    final AttrOptions options = new AttrOptions();
    /**
     * key : 玩家 id
     * value : UserSession
     */
    final NonBlockingHashMapLong<Session> userIdMap = new NonBlockingHashMapLong<>();

    /**
     * key : userChannelId
     * value : UserSession
     */
    final Map<UserChannelId, Session> userChannelIdMap = new NonBlockingHashMap<>();

    @Setter
    CmdRegions cmdRegions;

    @Setter
    UserHook userHook;

    @Override
    public boolean existUserSession(long userId) {
        return this.userIdMap.containsKey(userId);
    }

    @Override
    public Session getUserSession(long userId) {
        return this.userIdMap.get(userId);
    }

    @Override
    public Session getUserSession(UserChannelId userChannelId) {
        return this.userChannelIdMap.get(userChannelId);
    }

    @Override
    public void removeUserSession(long userId, Object msg) {
        this.ifPresent(userId, userSession -> {
            ChannelFuture channelFuture = userSession.writeAndFlush(msg);
            channelFuture.addListener((ChannelFutureListener) future -> {
                // 回调 UserSessions 中移除对应的玩家
                this.removeUserSession(userSession);
            });
        });
    }

    @Override
    public void forEach(Consumer<Session> consumer) {
        this.userChannelIdMap.values().forEach(consumer);
    }

    /**
     * 上线通知。注意：只有进行身份验证通过的，才会触发此方法
     *
     * @param userSession userSession
     */
    void userHookInto(UserSession userSession) {
        if (Objects.isNull(this.userHook)) {
            return;
        }

        this.userHook.into(userSession);
    }

    /**
     * 离线通知。注意：只有进行身份验证通过的，才会触发此方法
     *
     * @param userSession userSession
     */
    void userHookQuit(UserSession userSession) {
        if (Objects.isNull(userHook)) {
            return;
        }

        this.userHook.quit(userSession);
    }

    void settingDefault(UserSession userSession) {
        // 保存连接方式
        ExternalJoinEnum externalJoinEnum = this.option(UserSessionOption.externalJoin);
        userSession.option(UserSessionOption.externalJoin, externalJoinEnum);
    }
}

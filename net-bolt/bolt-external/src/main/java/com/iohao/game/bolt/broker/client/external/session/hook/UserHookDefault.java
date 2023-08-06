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
package com.iohao.game.bolt.broker.client.external.session.hook;

import com.iohao.game.bolt.broker.client.external.session.UserSession;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import com.iohao.game.common.consts.IoGameLogName;
import lombok.extern.slf4j.Slf4j;

/**
 * UserHookDefault
 *
 * @author 渔民小镇
 * @date 2022-03-14
 */
@Slf4j(topic = IoGameLogName.CommonStdout)
public class UserHookDefault implements UserHook {
    @Override
    public void into(UserSession userSession) {
        long userId = userSession.getUserId();
        log.info("玩家上线 userId: {} -- {}", userId, userSession.getUserChannelId());
        log.info("当前在线玩家数量： {}", UserSessions.me().countOnline());
    }

    @Override
    public void quit(UserSession userSession) {
        long userId = userSession.getUserId();
        log.info("玩家退出 userId: {} -- {}", userId, userSession.getUserChannelId());
        log.info("当前在线玩家数量： {}", UserSessions.me().countOnline());
    }
}

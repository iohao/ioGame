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
package com.iohao.game.external.core.netty.micro.join;

import com.iohao.game.common.kit.PresentKit;
import com.iohao.game.external.core.ExternalCoreSetting;
import com.iohao.game.external.core.hook.IdleHook;
import com.iohao.game.external.core.hook.internal.IdleProcessSetting;
import com.iohao.game.external.core.micro.MicroBootstrap;
import com.iohao.game.external.core.micro.join.ExternalJoinSelector;
import com.iohao.game.external.core.netty.DefaultExternalCoreSetting;
import com.iohao.game.external.core.netty.SettingOption;
import com.iohao.game.external.core.netty.handler.SocketCmdAccessAuthHandler;
import com.iohao.game.external.core.netty.handler.SocketIdleHandler;
import com.iohao.game.external.core.netty.handler.SocketRequestBrokerHandler;
import com.iohao.game.external.core.netty.handler.SocketUserSessionHandler;
import com.iohao.game.external.core.netty.hook.DefaultSocketIdleHook;
import com.iohao.game.external.core.netty.micro.SocketMicroBootstrap;
import com.iohao.game.external.core.netty.session.SocketUserSessions;
import com.iohao.game.external.core.session.UserSessions;

import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2023-05-31
 */
abstract class SocketExternalJoinSelector implements ExternalJoinSelector {
    @Override
    public void defaultSetting(ExternalCoreSetting coreSetting) {
        DefaultExternalCoreSetting setting = (DefaultExternalCoreSetting) coreSetting;

        // microBootstrap；如果开发者没有手动赋值，则根据当前连接方式生成
        MicroBootstrap microBootstrap = setting.getMicroBootstrap();
        PresentKit.ifNull(microBootstrap, () -> setting.setMicroBootstrap(new SocketMicroBootstrap()));

        // UserSessions 管理器；如果开发者没有手动赋值，则根据当前连接方式生成
        UserSessions<?, ?> userSessions = setting.getUserSessions();
        PresentKit.ifNull(userSessions, () -> setting.setUserSessions(new SocketUserSessions()));

        // IdleHook 心跳钩子；长连接方式开启了心跳，强制给一个心跳钩子
        IdleProcessSetting idleProcessSetting = setting.getIdleProcessSetting();
        if (Objects.nonNull(idleProcessSetting)) {
            IdleHook<Object> idleHook = idleProcessSetting.getIdleHook();
            PresentKit.ifNull(idleHook, () -> idleProcessSetting.setIdleHook(new DefaultSocketIdleHook()));

            // 心跳钩子 Handler
            setting.ifNull(SettingOption.socketIdleHandler, SocketIdleHandler::new);
        }

        // pipelineCustom Handler
        setting.ifNull(SettingOption.socketUserSessionHandler, SocketUserSessionHandler::new);
        setting.ifNull(SettingOption.socketCmdAccessAuthHandler, SocketCmdAccessAuthHandler::new);
        setting.ifNull(SettingOption.socketRequestBrokerHandler, SocketRequestBrokerHandler::new);
    }
}

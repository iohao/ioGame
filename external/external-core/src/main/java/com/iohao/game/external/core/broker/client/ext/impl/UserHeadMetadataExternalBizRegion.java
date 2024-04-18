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
package com.iohao.game.external.core.broker.client.ext.impl;

import com.iohao.game.action.skeleton.core.exception.MsgException;
import com.iohao.game.action.skeleton.protocol.BarMessage;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.external.RequestCollectExternalMessage;
import com.iohao.game.core.common.client.ExternalBizCodeCont;
import com.iohao.game.external.core.broker.client.ext.ExternalBizRegion;
import com.iohao.game.external.core.broker.client.ext.ExternalBizRegionContext;
import com.iohao.game.external.core.session.UserSession;

import java.io.Serializable;

/**
 * 从用户（玩家）所在游戏对外服中获取用户自身的数据
 *
 * <pre>
 *     因为用户（玩家）的数据是存在 UserSession 中的，see {@link UserSession#employ(BarMessage)}，通过该扩展，我们能获取这些数据。
 *     employ 中的数据包括：userId、用户所绑定的游戏逻辑服、元信息 ...等，更具体的请阅读源码。
 *
 *     使用场景：
 *         在模拟玩家请求时，需要用到玩家的元信息、已绑定游戏逻辑服...等相关信息时，可以从这里（UserHeadMetadataExternalBizRegion）获取。
 *         如果需要业务的场景不复杂（也就是不需要玩家元信息、已绑定游戏逻辑服...等相关信息时），建议使用模拟玩家请求（常规的跨服调用）。
 *
 *         实际中，使用常规的模拟玩家请求就能满足大部分业务场景的需求了。
 *
 *     注意事项：
 *         需要玩家是在线的，也就是接入了其中一个游戏对外服的。
 *
 *     其他参考：
 *         模拟玩家请求 <a href="https://www.yuque.com/iohao/game/uwxeg2uk86mu27mp">GM 后台与逻辑服交互 - 文档</a>
 *
 *     使用方式
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-04-18
 */
public final class UserHeadMetadataExternalBizRegion implements ExternalBizRegion {
    @Override
    public int getBizCode() {
        return ExternalBizCodeCont.userHeadMetadata;
    }

    @Override
    public Serializable request(ExternalBizRegionContext regionContext) throws MsgException {
        // 检测用户是否存在
        ExternalBizRegionKit.checkUserExist(regionContext);

        RequestCollectExternalMessage request = regionContext.getRequestCollectExternalMessage();

        HeadMetadata headMetadata = request.getData();

        long userId = request.getUserId();
        var userSessions = regionContext.getUserSessions();
        UserSession userSession = userSessions.getUserSession(userId);

        // 给 message（RequestMessage） 加上一些 user 自身的数据
        userSession.employ(headMetadata);

        return headMetadata;
    }
}

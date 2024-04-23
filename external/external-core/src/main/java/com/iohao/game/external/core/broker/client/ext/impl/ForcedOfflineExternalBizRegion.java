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

import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.protocol.BarMessage;
import com.iohao.game.action.skeleton.protocol.external.RequestCollectExternalMessage;
import com.iohao.game.core.common.client.ExternalBizCodeCont;
import com.iohao.game.external.core.broker.client.ext.ExternalBizRegion;
import com.iohao.game.external.core.broker.client.ext.ExternalBizRegionContext;
import com.iohao.game.external.core.message.ExternalCodecKit;

import java.io.Serializable;

/**
 * 强制用户（玩家）下线
 * <pre>
 *     对外服业务扩展
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-02-21
 */
public final class ForcedOfflineExternalBizRegion implements ExternalBizRegion {
    final BarMessage response;

    public ForcedOfflineExternalBizRegion() {
        // 强制玩家下线 状态码
        response = ExternalCodecKit.createResponse();
        response.setResponseStatus(ActionErrorEnum.forcedOffline.getCode());
        response.setValidatorMsg(ActionErrorEnum.forcedOffline.getMsg());
    }

    @Override
    public int getBizCode() {
        return ExternalBizCodeCont.forcedOffline;
    }

    @Override
    public Serializable request(ExternalBizRegionContext regionContext) {
        RequestCollectExternalMessage request = regionContext.getRequestCollectExternalMessage();

        long userId = request.getUserId();

        // 发送强制下线消息
        var userSessions = regionContext.getUserSessions();
        // 据 userId 移除 UserSession ，在移除前发送一个消息
        userSessions.removeUserSession(userId, response);

        return null;
    }
}

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
package com.iohao.game.external.core.broker.client.ext.impl;

import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.core.exception.MsgException;
import com.iohao.game.action.skeleton.protocol.external.RequestCollectExternalMessage;
import com.iohao.game.core.common.client.Attachment;
import com.iohao.game.core.common.client.ExternalBizCodeCont;
import com.iohao.game.external.core.broker.client.ext.ExternalBizRegion;
import com.iohao.game.external.core.broker.client.ext.ExternalBizRegionContext;
import com.iohao.game.external.core.session.UserSessionOption;

import java.io.Serializable;

/**
 * 元信息
 *
 * @author 渔民小镇
 * @date 2023-02-21
 */
public final class AttachmentExternalBizRegion implements ExternalBizRegion {
    @Override
    public int getBizCode() {
        return ExternalBizCodeCont.attachment;
    }

    @Override
    public Serializable request(ExternalBizRegionContext regionContext) throws MsgException {

        RequestCollectExternalMessage request = regionContext.getRequestCollectExternalMessage();

        Attachment attachment = request.getData();

        long userId = attachment.getUserId();

        // true 用户存在游戏对外服中
        var userSessions = regionContext.getUserSessions();
        boolean existUser = userSessions.existUserSession(userId);
        ActionErrorEnum.dataNotExist.assertTrue(existUser);

        byte[] bytes = DataCodecKit.encode(attachment);

        userSessions.ifPresent(userId, userSession -> userSession.option(UserSessionOption.attachment, bytes));

        return null;
    }
}

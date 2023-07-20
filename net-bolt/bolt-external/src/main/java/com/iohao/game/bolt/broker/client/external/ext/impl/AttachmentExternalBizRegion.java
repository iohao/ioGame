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
package com.iohao.game.bolt.broker.client.external.ext.impl;

import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.core.exception.MsgException;
import com.iohao.game.action.skeleton.protocol.external.RequestCollectExternalMessage;
import com.iohao.game.bolt.broker.client.external.ext.ExternalBizRegion;
import com.iohao.game.bolt.broker.client.external.ext.ExternalBizRegionContext;
import com.iohao.game.bolt.broker.client.external.session.UserSession;
import com.iohao.game.bolt.broker.client.external.session.UserSessionAttr;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import com.iohao.game.core.common.client.Attachment;
import com.iohao.game.core.common.client.ExternalBizCodeCont;

import java.io.Serializable;

/**
 * @author 渔民小镇
 * @date 2022-12-11
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
        boolean existUser = UserSessions.me().existUserSession(userId);
        ActionErrorEnum.dataNotExist.assertTrue(existUser);

        byte[] bytes = DataCodecKit.encode(attachment);

        UserSession userSession = UserSessions.me().getUserSession(userId);
        userSession.attr(UserSessionAttr.attachment, bytes);

        return null;
    }
}

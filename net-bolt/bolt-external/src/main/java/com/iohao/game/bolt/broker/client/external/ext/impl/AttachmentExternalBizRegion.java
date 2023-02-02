/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

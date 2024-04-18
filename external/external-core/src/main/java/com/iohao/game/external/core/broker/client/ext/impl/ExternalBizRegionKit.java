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
import com.iohao.game.action.skeleton.protocol.external.RequestCollectExternalMessage;
import com.iohao.game.external.core.broker.client.ext.ExternalBizRegionContext;
import lombok.experimental.UtilityClass;

/**
 * @author 渔民小镇
 * @date 2024-04-18
 */
@UtilityClass
class ExternalBizRegionKit {
    /**
     * 检测用户是否存在，如果不存在就抛异常
     *
     * @param regionContext regionContext
     */
    public void checkUserExist(ExternalBizRegionContext regionContext) {
        RequestCollectExternalMessage request = regionContext.getRequestCollectExternalMessage();
        // 检测用户是否存在
        long userId = request.getUserId();
        var userSessions = regionContext.getUserSessions();
        boolean existUser = userSessions.existUserSession(userId);
        ActionErrorEnum.dataNotExist.assertTrue(existUser);
    }
}

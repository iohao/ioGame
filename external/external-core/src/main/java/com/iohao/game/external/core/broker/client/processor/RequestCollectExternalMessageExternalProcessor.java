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
package com.iohao.game.external.core.broker.client.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.core.exception.MsgException;
import com.iohao.game.action.skeleton.protocol.external.RequestCollectExternalMessage;
import com.iohao.game.action.skeleton.protocol.external.ResponseCollectExternalItemMessage;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.common.consts.IoGameLogName;
import com.iohao.game.external.core.aware.UserSessionsAware;
import com.iohao.game.external.core.broker.client.ext.ExternalBizRegion;
import com.iohao.game.external.core.broker.client.ext.ExternalBizRegionContext;
import com.iohao.game.external.core.broker.client.ext.ExternalBizRegions;
import com.iohao.game.external.core.session.UserSessions;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

/**
 * 处理来自游戏逻辑服的请求，并响应结果给请求方
 *
 * @author 渔民小镇
 * @date 2023-02-21
 */
@Slf4j(topic = IoGameLogName.ExternalTopic)
public final class RequestCollectExternalMessageExternalProcessor extends AbstractAsyncUserProcessor<RequestCollectExternalMessage>
        implements UserSessionsAware {
    UserSessions<?, ?> userSessions;

    @Override
    public void setUserSessions(UserSessions<?, ?> userSessions) {
        this.userSessions = userSessions;
    }

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, RequestCollectExternalMessage request) {

        int bizCode = request.getBizCode();

        // 通过业务码得到对应的业务处理类
        ExternalBizRegion externalBizRegion = ExternalBizRegions.getExternalRegion(bizCode);

        // 通常是开发者忘记配置对应的处理类
        if (Objects.isNull(externalBizRegion)) {
            log.error("{} - 游戏对外服对应的业务类不存在", bizCode);

            // 对外服业务处理不存在
            var itemMessage = new ResponseCollectExternalItemMessage();
            itemMessage.setError(ActionErrorEnum.classNotExist);
            // 返回结果给请求端
            asyncCtx.sendResponse(itemMessage);

            return;
        }

        ResponseCollectExternalItemMessage itemMessage = new ResponseCollectExternalItemMessage();

        ExternalBizRegionContext context = new ExternalBizRegionContext();
        context.setRequestCollectExternalMessage(request);
        context.setUserSessions(this.userSessions);

        try {
            Serializable data = externalBizRegion.request(context);
            itemMessage.setData(data);
        } catch (Throwable e) {
            if (e instanceof MsgException msgException) {
                itemMessage.setError(msgException);
            } else {
                // 不是自定义的异常才打印 error 信息
                log.error(e.getMessage(), e);
                itemMessage.setError(ActionErrorEnum.systemOtherErrCode);
            }
        }

        // 返回结果给请求端
        asyncCtx.sendResponse(itemMessage);
    }

    @Override
    public String interest() {
        return RequestCollectExternalMessage.class.getName();
    }


}

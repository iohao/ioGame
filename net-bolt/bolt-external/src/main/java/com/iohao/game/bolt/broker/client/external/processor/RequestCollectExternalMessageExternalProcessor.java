/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.bolt.broker.client.external.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.core.exception.MsgException;
import com.iohao.game.action.skeleton.protocol.external.RequestCollectExternalMessage;
import com.iohao.game.action.skeleton.protocol.external.ResponseCollectExternalItemMessage;
import com.iohao.game.bolt.broker.client.external.ext.ExternalBizRegion;
import com.iohao.game.bolt.broker.client.external.ext.ExternalBizRegionContext;
import com.iohao.game.bolt.broker.client.external.ext.ExternalBizRegions;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

/**
 * 处理来自游戏逻辑服的请求，并响应结果给请求方
 *
 * @author 渔民小镇
 * @date 2022-07-27
 */
@Slf4j
public class RequestCollectExternalMessageExternalProcessor extends AsyncUserProcessor<RequestCollectExternalMessage> {

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, RequestCollectExternalMessage request) {

        int bizCode = request.getBizCode();
        // 通过业务码得到对应的业务处理类
        ExternalBizRegion externalBizRegion = ExternalBizRegions.me().getExternalRegion(bizCode);

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

        try {

            ExternalBizRegionContext context = new ExternalBizRegionContext();
            context.setRequestCollectExternalMessage(request);
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

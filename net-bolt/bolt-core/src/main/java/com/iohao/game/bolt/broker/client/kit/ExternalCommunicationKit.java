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
package com.iohao.game.bolt.broker.client.kit;

import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.external.RequestCollectExternalMessage;
import com.iohao.game.action.skeleton.protocol.external.ResponseCollectExternalItemMessage;
import com.iohao.game.bolt.broker.core.client.BrokerClientHelper;
import com.iohao.game.common.kit.exception.CommonRuntimeException;
import com.iohao.game.common.kit.exception.ThrowKit;
import com.iohao.game.core.common.client.Attachment;
import com.iohao.game.core.common.client.ExternalBizCodeCont;
import lombok.experimental.UtilityClass;

import java.util.Optional;

/**
 * 这个工具只能在游戏逻辑服中使用
 *
 * @author 渔民小镇
 * @date 2022-07-27
 */
@UtilityClass
public class ExternalCommunicationKit {
    /**
     * 玩家是否在线
     *
     * @param userId userId
     * @return true 玩家在线
     */
    public boolean existUser(long userId) {
        RequestCollectExternalMessage request = new RequestCollectExternalMessage()
                // 根据业务码，调用游戏对外服与业务码对应的业务实现类 （ExistUserExternalBizRegion）
                .setBizCode(ExternalBizCodeCont.existUser)
                .setUserId(userId);

        return BrokerClientHelper
                // 【游戏逻辑服】与【游戏对外服】通讯上下文
                .getInvokeExternalModuleContext()
                .invokeExternalModuleCollectMessage(request)
                // 只要有一条数据存在，就表示正确的
                .anySuccess();
    }

    /**
     * 强制指定玩家下线，让玩家与游戏对外服断开连接
     *
     * @param userId 需要强制下线的 userId
     */
    public void forcedOffline(long userId) {
        RequestCollectExternalMessage request = new RequestCollectExternalMessage()
                // 根据业务码，调用游戏对外服与业务码对应的业务实现类 （ForcedOfflineExternalBizRegion）
                .setBizCode(ExternalBizCodeCont.forcedOffline)
                .setUserId(userId);

        /*
         * 强制玩家下线
         * 实现类 ForcedOfflineExternalBizRegion
         * 因为不需要任何的返回值，所以我们只需要调用一下方法就好了
         */
        BrokerClientHelper
                // 【游戏逻辑服】与【游戏对外服】通讯上下文
                .getInvokeExternalModuleContext()
                .invokeExternalModuleCollectMessage(request);
    }

    /**
     * 设置元信息到游戏对外服
     * <pre>
     *     之后所有 action 的 FlowContext 中会携带上这个元信息对象，
     *     不建议在元信息保存过多的信息，因为会每次传递。
     * </pre>
     *
     * @param attachment  元信息
     * @param flowContext flowContext
     */
    public void setAttachment(Attachment attachment, FlowContext flowContext) {
        flowContext.updateAttachment(attachment);
    }

    /**
     * 给请求添加一些 user 自身所具备的数据，这些数据来自于用户所在游戏对外服
     * <pre>
     *     将用户元信息、所绑定的游戏逻辑服设置到 RequestMessage headMetadata 中。
     *
     *     注意事项：只有玩家在线才能从其对应的游戏对外服中获取数据。
     * </pre>
     *
     * @param requestMessage 请求（通常是模拟的用户请求）
     * @return 用户（玩家）所在游戏对外服中的 HeadMetadata 数据，headMetadataOptional 中还包括了一些其他的信息，开发者如果有需要的可从中获取。
     */
    public Optional<HeadMetadata> employHeadMetadata(RequestMessage requestMessage) {

        long userId = Optional.ofNullable(requestMessage)
                .map(RequestMessage::getHeadMetadata)
                .map(HeadMetadata::getUserId).orElse(0L);

        if (userId <= 0) {
            throw new CommonRuntimeException("userId <= 0");
        }

        RequestCollectExternalMessage request = new RequestCollectExternalMessage()
                // 根据业务码，调用游戏对外服与业务码对应的业务实现类 （UserHeadMetadataExternalBizRegion）
                .setBizCode(ExternalBizCodeCont.userHeadMetadata)
                .setUserId(userId)
                .setData(requestMessage.getHeadMetadata())
                .setSourceClientId(requestMessage.getHeadMetadata().getSourceClientId());

        Optional<HeadMetadata> headMetadataOptional = BrokerClientHelper
                // 【游戏逻辑服】与【游戏对外服】通讯上下文
                .getInvokeExternalModuleContext()
                .invokeExternalModuleCollectMessage(request)
                .optionalAnySuccess()
                .map(ResponseCollectExternalItemMessage::getData);

        headMetadataOptional.ifPresent(externalHeadMetadata -> {
            // 将用户元信息、所绑定的游戏逻辑服设置到 RequestMessage headMetadata 中
            HeadMetadata headMetadata = requestMessage.getHeadMetadata();
            headMetadata.setBindingLogicServerIds(externalHeadMetadata.getBindingLogicServerIds());
            headMetadata.setAttachmentData(externalHeadMetadata.getAttachmentData());
        });

        // headMetadataOptional 中还包括了一些其他的信息，如有需要的可从中获取
        return headMetadataOptional;
    }
}

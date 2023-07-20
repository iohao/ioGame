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
package com.iohao.game.bolt.broker.client.kit;

import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.external.RequestCollectExternalMessage;
import com.iohao.game.bolt.broker.core.client.BrokerClientHelper;
import com.iohao.game.core.common.client.Attachment;
import com.iohao.game.core.common.client.ExternalBizCodeCont;
import lombok.experimental.UtilityClass;

import java.util.Objects;

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
        return BrokerClientHelper
                // 【游戏逻辑服】与【游戏对外服】通讯上下文
                .getInvokeExternalModuleContext()
                // 根据业务码，调用游戏对外服与业务码对应的业务实现类 （ExistUserExternalBizRegion）
                .invokeExternalModuleCollectMessage(ExternalBizCodeCont.existUser, userId)
                // 只要有一条数据存在，就表示正确的
                .anySuccess();
    }

    /**
     * 强制指定玩家下线，让玩家与游戏对外服断开连接
     *
     * @param userId 需要强制下线的 userId
     */
    public void forcedOffline(long userId) {
        /*
         * 强制玩家下线
         * 实现类 ForcedOfflineExternalBizRegion
         * 因为不需要任何的返回值，所以我们只需要调用一下方法就好了
         */
        BrokerClientHelper
                // 【游戏逻辑服】与【游戏对外服】通讯上下文
                .getInvokeExternalModuleContext()
                // 根据业务码，调用游戏对外服与业务码对应的业务实现类 （ForcedOfflineExternalBizRegion）
                .invokeExternalModuleCollectMessage(ExternalBizCodeCont.forcedOffline, userId);
    }

    /**
     * 设置元信息到游戏对外服
     * <pre>
     *     之后所有 action 的 FlowContext 中会携带上这个元信息对象，
     *     不建议在元信息保存过多的信息，因为会每次传递。
     * </pre>
     * <pre>
     *     将在下个大版本中移除，
     *     请使用 {@link ExternalCommunicationKit#setAttachment(Attachment, FlowContext)} 代替，此方法性能相对高些，
     *     因为指定了要访问的游戏对外服id
     * </pre>
     *
     * @param attachment 元信息
     */
    @Deprecated
    public void setAttachment(Attachment attachment) {
        // 不做 null 判断，只做个 userId 的检测
        long userId = attachment.getUserId();

        if (userId <= 0) {
            throw new RuntimeException("userId error");
        }

        BrokerClientHelper
                // 【游戏逻辑服】与【游戏对外服】通讯上下文
                .getInvokeExternalModuleContext()
                // 根据业务码，调用游戏对外服与业务码对应的业务实现类 （AttachmentDataExternalBizRegion）
                .invokeExternalModuleCollectMessage(ExternalBizCodeCont.attachment, attachment);
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
        // 不做 null 判断，只做个 userId 的检测
        long userId = attachment.getUserId();

        if (userId <= 0) {
            throw new RuntimeException("userId <= 0");
        }

        // 得到游戏对外服 id
        RequestMessage request = flowContext.getRequest();
        HeadMetadata headMetadata = request.getHeadMetadata();
        int sourceClientId = headMetadata.getSourceClientId();

        var requestCollectExternalMessage = new RequestCollectExternalMessage()
                // 根据业务码，调用游戏对外服与业务码对应的业务实现类 （AttachmentDataExternalBizRegion）
                .setBizCode(ExternalBizCodeCont.attachment)
                // 元信息
                .setData(attachment)
                // 指定游戏对外服
                .setSourceClientId(sourceClientId);

        BrokerClientHelper
                // 【游戏逻辑服】与【游戏对外服】通讯上下文
                .getInvokeExternalModuleContext()
                .invokeExternalModuleCollectMessage(requestCollectExternalMessage);
    }
}

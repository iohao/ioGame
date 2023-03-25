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
package com.iohao.game.bolt.broker.client.kit;

import com.iohao.game.bolt.broker.core.client.BrokerClientHelper;
import com.iohao.game.core.common.client.Attachment;
import com.iohao.game.core.common.client.ExternalBizCodeCont;
import lombok.experimental.UtilityClass;

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
     *
     * @param attachment 元信息
     */
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
}

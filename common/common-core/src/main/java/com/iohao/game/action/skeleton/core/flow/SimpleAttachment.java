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
package com.iohao.game.action.skeleton.core.flow;

import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.core.IoGameCommonCoreConfig;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;

import java.util.Objects;

/**
 * 帮助 FlowContext 得到更新、获取元信息的能力
 *
 * @author 渔民小镇
 * @date 2023-12-27
 */
interface SimpleAttachment extends SimpleCommunication {

    /**
     * 更新元信息
     * <pre>
     *     将元信息同步到玩家所在的游戏对外服中
     * </pre>
     *
     * @param attachment 元信息
     */
    default void updateAttachment(UserAttachment attachment) {
        Objects.requireNonNull(attachment);

        HeadMetadata headMetadata = this.getHeadMetadata();
        long userId = headMetadata.getUserId();

        if (userId <= 0) {
            throw new RuntimeException("userId <= 0");
        }

        // 根据业务码，调用游戏对外服与业务码对应的业务实现类 （AttachmentExternalBizRegion、ExternalBizCodeCont）
        int bizCode = IoGameCommonCoreConfig.ExternalBizCode.attachment;
        this.invokeExternalModuleCollectMessage(bizCode, attachment);
    }

    /**
     * 更新元信息
     * <pre>
     *     将元信息同步到玩家所在的游戏对外服中
     * </pre>
     */
    default void updateAttachment() {
        UserAttachment attachment = this.getAttachment();
        this.updateAttachment(attachment);
    }

    /**
     * 得到元附加信息
     * <pre>
     *     一般是在游戏对外服中设置的一些附加信息
     *     这些信息会跟随请求来到游戏逻辑服中
     * </pre>
     *
     * @param clazz clazz
     * @param <T>   t
     * @return 元附加信息
     */
    default <T extends UserAttachment> T getAttachment(Class<T> clazz) {
        HeadMetadata headMetadata = this.getHeadMetadata();
        byte[] attachmentData = headMetadata.getAttachmentData();
        return DataCodecKit.decode(attachmentData, clazz);
    }

    /**
     * 得到元附加信息
     * <p>
     * 使用参考
     * <pre>{@code
     *     public class MyFlowContext extends FlowContext {
     *         MyAttachment attachment;
     *
     *         @Override
     *         @SuppressWarnings("unchecked")
     *         public MyAttachment getAttachment() {
     *             if (Objects.isNull(attachment)) {
     *                 this.attachment = this.getAttachment(MyAttachment.class);
     *             }
     *
     *             return this.attachment;
     *         }
     *     }
     * }
     * </pre>
     *
     * @param <T> t
     * @return 元附加信息
     */
    default <T extends UserAttachment> T getAttachment() {
        throw new RuntimeException("需要子类实现");
    }
}

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
package com.iohao.game.external.core.session;


import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.protocol.BarMessage;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.common.kit.attr.AttrOptionDynamic;
import com.iohao.game.external.core.message.ExternalCodecKit;

/**
 * UserSession interface
 * <p>
 * UserSession 接口， 对应的动态属性接口 {@link UserSessionOption}
 *
 * @author 渔民小镇
 * @date 2023-02-18
 * @see UserSessionOption UserSessionOption for dynamic properties
 */
public interface UserSession extends AttrOptionDynamic {
    /**
     * active
     *
     * @return true active
     */
    boolean isActive();

    /**
     * 设置当前用户（玩家）的 id
     * <pre>
     *     当设置好玩家 id ，也表示着已经身份验证了（表示登录过了）。
     * </pre>
     *
     * @param userId userId
     */
    void setUserId(long userId);

    /**
     * 当前用户（玩家）的 id
     *
     * @return 当前用户（玩家）的 id
     */
    long getUserId();

    /**
     * authVerified
     * <p>
     * 是否进行身份验证
     *
     * @return true: logged in. （true 已经身份验证了，表示登录过了。）
     */
    boolean isVerifyIdentity();

    /**
     * UserSessionState of current player
     *
     * @return State
     */
    UserSessionState getState();

    /**
     * UserChannelId of current player
     * <p>
     * 当前用户（玩家）的 UserChannelId
     *
     * @return UserChannelId
     */
    UserChannelId getUserChannelId();

    /**
     * Adds user info to request.
     * <p>
     * Developers can extend data via HeadMetadata.setAttachmentData(byte[]), which will be forwarded to the logic server.
     * <p>
     * 给请求消息加上一些 user 自身的数据
     * <p>
     * 如果开发者要扩展数据，可通过 {@link HeadMetadata#setAttachmentData(byte[])} 字段来扩展，这些数据可以传递到逻辑服
     *
     * @param requestMessage requestMessage
     */
    void employ(BarMessage requestMessage);

    /**
     * Attach user data to HeadMetadata
     * <p>
     * 给 HeadMetadata 加上一些 user 自身的数据
     *
     * @param headMetadata HeadMetadata
     */
    void employ(HeadMetadata headMetadata);

    /**
     * writeAndFlush
     *
     * @param message message
     * @return ChannelFuture
     */
    <T> T writeAndFlush(Object message);

    /**
     * Get player IP
     *
     * @return 玩家 ip
     */
    String getIp();

    /**
     * Creates RequestMessage with user's own info.
     * <p>
     * 创建 RequestMessage，内部会将 User 自身的相关信息设置到 RequestMessage 中。
     *
     * @param cmdInfo cmdInfo
     * @return RequestMessage
     * @since 21.15
     */
    default RequestMessage ofRequestMessage(CmdInfo cmdInfo) {
        RequestMessage request = ExternalCodecKit.createRequest();
        HeadMetadata headMetadata = request.getHeadMetadata();
        headMetadata.setCmdInfo(cmdInfo);
        // 给请求消息加上一些 user 自身的数据
        this.employ(headMetadata);

        return request;
    }
}

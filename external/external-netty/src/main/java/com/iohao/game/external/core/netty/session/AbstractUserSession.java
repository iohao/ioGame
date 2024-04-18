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
package com.iohao.game.external.core.netty.session;

import com.iohao.game.action.skeleton.protocol.BarMessage;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.common.kit.attr.AttrOptions;
import com.iohao.game.core.common.cmd.CmdRegions;
import com.iohao.game.external.core.session.UserChannelId;
import com.iohao.game.external.core.session.UserSession;
import com.iohao.game.external.core.session.UserSessionOption;
import com.iohao.game.external.core.session.UserSessionState;
import io.netty.channel.Channel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashSet;

import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2023-05-28
 */
@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
abstract class AbstractUserSession implements UserSession {
    final AttrOptions options = new AttrOptions();
    Channel channel;
    /** 玩家 id */
    long userId;
    UserChannelId userChannelId;
    CmdRegions cmdRegions;
    /** 所在游戏对外服的 idHash */
    @Setter
    int externalClientId;
    /** 状态 */
    @Setter
    UserSessionState state = UserSessionState.ACTIVE;

    AbstractUserSession() {
        // false 没有进行身份验证
        this.option(UserSessionOption.verifyIdentity, false);

        // 玩家绑定的多个游戏逻辑服记录
        this.option(UserSessionOption.bindingLogicServerIdSet, new NonBlockingHashSet<>());
    }

    public void employ(BarMessage requestMessage) {
        HeadMetadata headMetadata = requestMessage.getHeadMetadata();
        this.employ(headMetadata);
    }

    @Override
    public void employ(HeadMetadata headMetadata) {
        // 设置请求用户的id
        headMetadata.setUserId(this.userId);
        headMetadata.setSourceClientId(this.externalClientId);

        this.ifPresent(UserSessionOption.externalJoin, externalJoin -> headMetadata.setStick(externalJoin.getIndex()));

        if (!this.isVerifyIdentity()) {
            // 只有没进行验证的，才给 userChannelId
            String channelId = this.userChannelId.channelId();
            // 一般指用户的 channelId （来源于对外服的 channel 长连接）
            headMetadata.setChannelId(channelId);
        }

        // 设置用户绑定的游戏逻辑服 id
        this.ifPresent(UserSessionOption.bindingLogicServerIdArray, headMetadata::setBindingLogicServerIds);

        // 如果 headMetadata 的 attachmentData 不为 null，通常是开发者在其他地方给 attachmentData 设置了值，框架就不管了。
        if (Objects.isNull(headMetadata.getAttachmentData())) {
            // 将 UserSession attachment 的值设置到 HeadMetadata attachmentData 中
            this.ifPresent(UserSessionOption.attachment, headMetadata::setAttachmentData);
        }
    }

    @Override
    public void setUserId(long userId) {
        this.userId = userId;
        this.option(UserSessionOption.verifyIdentity, true);
    }

    @Override
    public boolean isVerifyIdentity() {
        return this.optionValue(UserSessionOption.verifyIdentity, false);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof AbstractUserSession that)) {
            return false;
        }

        return Objects.equals(userChannelId, that.userChannelId);
    }

    @Override
    public int hashCode() {
        return userChannelId.hashCode();
    }
}

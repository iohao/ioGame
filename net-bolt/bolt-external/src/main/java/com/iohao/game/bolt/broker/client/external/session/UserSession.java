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
package com.iohao.game.bolt.broker.client.external.session;

import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.common.kit.SafeKit;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * 用户的唯一 session 信息
 * <pre>
 *     与 channel 是 1:1 的关系，可取到对应的 userId、channel 等信息
 *
 *     关于用户管理 UserSessions 和 UserSession 可以参考这里：
 *     https://www.yuque.com/iohao/game/wg6lk7
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-03-15
 */
@Getter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSession {

    /** user channel */
    final Channel channel;
    /** userId */
    long userId;
    /** 用户状态 */
    @Setter
    UserSessionState state;

    public UserSession(Channel channel) {
        this.channel = channel;

        // false 没有进行身份验证
        this.channel.attr(UserSessionAttr.verifyIdentity).set(false);
        // channel 中也保存 UserSession 的引用
        this.channel.attr(UserSessionAttr.userSession).set(this);

        this.state = UserSessionState.ACTIVE;
    }

    /**
     * 给请求消息加上一些 user 自身的数据
     * <pre>
     *     如果开发者要扩展数据，可通过 {@link HeadMetadata#setAttachmentData(byte[])} 字段来扩展
     *     这些数据可以传递到逻辑服
     * </pre>
     *
     * @param requestMessage 请求消息
     */
    public void employ(RequestMessage requestMessage) {
        HeadMetadata headMetadata = requestMessage.getHeadMetadata();

        if (!this.isVerifyIdentity()) {
            // 只有没进行验证的，才给 userChannelId
            String channelId = this.getChannelId();
            // 一般指用户的 channelId （来源于对外服的 channel 长连接）
            headMetadata.setChannelId(channelId);
        }

        // 设置请求用户的id
        headMetadata.setUserId(this.userId);

        if (this.hasAttr(UserSessionAttr.endPointLogicServerId)) {
            // 用户绑定逻辑服id
            Integer attr = this.attr(UserSessionAttr.endPointLogicServerId);
            int endPointLogicServerId = SafeKit.getInt(attr, 0);
            headMetadata.setEndPointClientId(endPointLogicServerId);
        }

        /*
         * 这里做个兼容。如果开发者没有额外扩展元信息的，才进入这里的逻辑。
         * 也就是说，如果开发者在其他地方给 headMetadata 的附加数据设置了值的，框架就不管了。
         */
        if (Objects.isNull(headMetadata.getAttachmentData())) {
            byte[] attachment = this.attr(UserSessionAttr.attachment);

            if (Objects.nonNull(attachment)) {
                headMetadata.setAttachmentData(attachment);
            }
        }
    }

    /**
     * 设置当前用户（玩家）的 id
     * <pre>
     *     当设置好玩家 id ，也表示着已经身份验证了（表示登录过了）。
     * </pre>
     *
     * @param userId userId
     */
    public void setUserId(long userId) {
        this.userId = userId;
        this.channel.attr(UserSessionAttr.verifyIdentity).set(true);
    }

    public UserChannelId getUserChannelId() {
        String channelId = this.getChannelId();
        return new UserChannelId(channelId);
    }

    /**
     * 获取玩家ip
     *
     * @return 获取玩家ip
     */
    public String getIp() {
        if (Boolean.FALSE.equals(isActiveChannel())) {
            return "";
        }

        InetSocketAddress inetSocketAddress = (InetSocketAddress) channel.remoteAddress();
        return inetSocketAddress.getHostString();
    }

    public <T> Attribute<T> attr(AttributeKey<T> key, T object) {
        Attribute<T> attr = this.channel.attr(key);
        attr.set(object);
        return attr;
    }

    public <T> T attr(AttributeKey<T> key) {
        Attribute<T> attr = this.channel.attr(key);
        return attr.get();
    }

    public <T> boolean hasAttr(AttributeKey<T> key) {
        return this.channel.hasAttr(key);
    }

    /**
     * 是否进行身份验证
     *
     * @return true 已经身份验证了，表示登录过了。
     */
    public boolean isVerifyIdentity() {
        return this.channel.attr(UserSessionAttr.verifyIdentity).get();
    }

    public boolean isActiveChannel() {
        return Objects.nonNull(channel) && channel.isActive();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof UserSession that)) {
            return false;
        }

        return userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    private String getChannelId() {
        return this.channel.id().asLongText();
    }
}

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

import com.iohao.game.bolt.broker.client.external.session.hook.UserHook;
import com.iohao.game.bolt.broker.client.external.session.hook.UserHookDefault;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;
import org.jctools.maps.NonBlockingHashMapLong;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;

/**
 * 用户 session 管理器
 * <pre>
 *     对所有用户UserSession的管理，统计在线用户等
 *
 *     关于用户管理 UserSessions 和 UserSession 可以参考这里：
 *     https://www.yuque.com/iohao/game/wg6lk7
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-01-11
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSessions {
    final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * key : 玩家 id
     * value : UserSession
     */
    final NonBlockingHashMapLong<UserSession> userIdMap = new NonBlockingHashMapLong<>();

    /**
     * key : userChannelId
     * value : UserSession
     */
    final Map<UserChannelId, UserSession> userChannelIdMap = new NonBlockingHashMap<>();

    /** UserHook 上线时、下线时会触发 */
    @Setter
    UserHook userHook = new UserHookDefault();

    /**
     * 获取 UserSession
     *
     * @param ctx ctx
     * @return UserSession
     */
    public UserSession getUserSession(ChannelHandlerContext ctx) throws RuntimeException {
        return this.getUserSession(ctx.channel());
    }

    /**
     * 获取 UserSession
     *
     * @param channel channel
     * @return UserSession
     */
    public UserSession getUserSession(Channel channel) throws RuntimeException {
        UserSession userSession = channel.attr(UserSessionAttr.userSession).get();

        if (Objects.isNull(userSession)) {
            throw new RuntimeException("userSession 不存在，请先加入 session 管理中，UserSessions.add");
        }

        return userSession;
    }

    /**
     * true 用户存在
     *
     * @param userId 用户id
     * @return true 用户存在
     */
    public boolean existUserSession(long userId) {
        return this.userIdMap.containsKey(userId);
    }

    /**
     * 获取 UserSession
     *
     * @param userId userId
     * @return UserSession
     */
    public UserSession getUserSession(long userId) throws RuntimeException {
        UserSession userSession = this.userIdMap.get(userId);

        if (Objects.isNull(userSession)) {
            throw new RuntimeException("userSession 不存在，请先登录在使用此方法");
        }

        return userSession;
    }

    public UserSession getUserSession(UserChannelId userChannelId) throws RuntimeException {
        return this.userChannelIdMap.get(userChannelId);
    }

    /**
     * 加入到 session 管理
     *
     * @param ctx ctx
     */
    public void add(ChannelHandlerContext ctx) {
        logIp(ctx);

        Channel channel = ctx.channel();

        if (channel.hasAttr(UserSessionAttr.userSession)) {
            return;
        }

        UserSession userSession = new UserSession(channel);

        UserChannelId userChannelId = userSession.getUserChannelId();

        this.userChannelIdMap.putIfAbsent(userChannelId, userSession);
        this.channelGroup.add(channel);
    }

    /**
     * 设置 channel 的 userId
     *
     * @param userChannelId userChannelId
     * @param userId        userId
     * @return true 设置成功
     */
    public boolean settingUserId(UserChannelId userChannelId, long userId) {

        UserSession userSession = this.getUserSession(userChannelId);
        if (Objects.isNull(userSession)) {
            return false;
        }

        if (Boolean.FALSE.equals(userSession.isActiveChannel())) {
            removeUserSession(userSession);
            return false;
        }

        // 如果 userId 不存在 map 中，将会执行 lambda 中的逻辑
        var tempUserSession = this.userIdMap.computeIfAbsent(userId, theUserId -> {
            userSession.setUserId(theUserId);
            return userSession;
        });

        if (!userSession.equals(tempUserSession)) {
            // 表示 UserSession 已经设置过 userId 了
            return false;
        }

        // 上线通知
        if (userSession.isVerifyIdentity()) {
            userHookInto(userSession);
        }

        return true;
    }

    /**
     * 移除 UserSession
     *
     * @param userSession userSession
     */
    public void removeUserSession(UserSession userSession) {

        if (Objects.isNull(userSession)) {
            return;
        }

        if (userSession.getState() == UserSessionState.DEAD) {
            return;
        }

        UserChannelId userChannelId = userSession.getUserChannelId();
        long userId = userSession.getUserId();
        Channel channel = userSession.getChannel();
        this.userIdMap.remove(userId);
        this.userChannelIdMap.remove(userChannelId);
        this.channelGroup.remove(channel);

        if (userSession.getState() == UserSessionState.ACTIVE && userSession.isVerifyIdentity()) {
            userSession.setState(UserSessionState.DEAD);
            this.userHookQuit(userSession);
        }

        // 关闭用户的连接
        channel.close();
    }

    /**
     * 当前在线人数
     *
     * @return 当前在线人数
     */
    public int countOnline() {
        return this.channelGroup.size();
    }

    /**
     * 全员消息广播
     * 消息类型 ExternalMessage
     *
     * @param msg 消息
     */
    public void broadcast(Object msg) {
        channelGroup.writeAndFlush(msg);
    }

    /**
     * 上线通知。注意：只有进行身份验证通过的，才会触发此方法
     *
     * @param userSession userSession
     */
    private void userHookInto(UserSession userSession) {
        if (Objects.isNull(userHook) || !userSession.isVerifyIdentity()) {
            return;
        }

        this.userHook.into(userSession);
    }

    /**
     * 离线通知。注意：只有进行身份验证通过的，才会触发此方法
     *
     * @param userSession userSession
     */
    private void userHookQuit(UserSession userSession) {
        if (Objects.isNull(userHook) || !userSession.isVerifyIdentity()) {
            return;
        }

        this.userHook.quit(userSession);
    }

    private void logIp(ChannelHandlerContext ctx) {
        if (log.isDebugEnabled()) {
            InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            String remoteAddress = socketAddress.getAddress().getHostAddress();

            socketAddress = (InetSocketAddress) ctx.channel().localAddress();
            String localAddress = socketAddress != null ? socketAddress.getAddress().getHostAddress() : "null";

            log.debug("localAddress::{}, remoteAddress::{}", localAddress, remoteAddress);
        }
    }

    private UserSessions() {
    }

    public static UserSessions me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final UserSessions ME = new UserSessions();
    }
}

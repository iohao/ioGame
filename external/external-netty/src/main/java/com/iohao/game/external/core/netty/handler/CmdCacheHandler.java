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
package com.iohao.game.external.core.netty.handler;

import com.iohao.game.action.skeleton.protocol.BarMessage;
import com.iohao.game.external.core.config.ExternalGlobalConfig;
import com.iohao.game.external.core.hook.cache.ExternalCmdCache;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Objects;

/**
 * 游戏对外服缓存
 *
 * @author 渔民小镇
 * @date 2023-07-02
 */
@ChannelHandler.Sharable
public final class CmdCacheHandler extends SimpleChannelInboundHandler<BarMessage> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        ExternalCmdCache externalCmdCache = ExternalGlobalConfig.externalCmdCache;
        if (Objects.isNull(externalCmdCache)) {
            // 删除自身处理器
            ctx.pipeline().remove(this);
        }

        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BarMessage message) {
        ExternalCmdCache externalCmdCache = ExternalGlobalConfig.externalCmdCache;

        BarMessage cache = externalCmdCache.getCache(message);
        if (Objects.nonNull(cache)) {
            // 从缓存中取到了数据，直接返回缓存数据
            ctx.writeAndFlush(cache);
            return;
        }

        // 交给下一个业务处理 (handler) , 下一个业务指的是你编排 handler 时的顺序
        ctx.fireChannelRead(message);
    }

    public CmdCacheHandler() {
    }

    public static CmdCacheHandler me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final CmdCacheHandler ME = new CmdCacheHandler();
    }
}

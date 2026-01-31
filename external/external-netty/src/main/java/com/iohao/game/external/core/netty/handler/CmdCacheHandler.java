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
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Objects;

/**
 * 游戏对外服缓存
 *
 * @author 渔民小镇
 * @date 2023-07-02
 */
@ChannelHandler.Sharable
public final class CmdCacheHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ExternalCmdCache externalCmdCache = ExternalGlobalConfig.externalCmdCache;
        if (Objects.isNull(externalCmdCache)) {
            // 如果没有配置缓存，移除自身处理器
            ctx.pipeline().remove(this);
        }

        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof BarMessage message) {
            ExternalCmdCache externalCmdCache = ExternalGlobalConfig.externalCmdCache;

            if (externalCmdCache != null) {
                BarMessage cache = externalCmdCache.getCache(message);
                if (Objects.nonNull(cache)) {
                    // 从缓存中取到了数据，直接返回缓存数据
                    ctx.writeAndFlush(cache);
                    return;
                }
            }

            ctx.fireChannelRead(message);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    public CmdCacheHandler() {
    }

    public static CmdCacheHandler me() {
        return Holder.ME;
    }

    private static class Holder {
        static final CmdCacheHandler ME = new CmdCacheHandler();
    }
}

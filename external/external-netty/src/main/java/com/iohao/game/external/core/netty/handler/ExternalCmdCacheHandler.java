/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.external.core.netty.handler;

import com.iohao.game.external.core.config.ExternalGlobalConfig;
import com.iohao.game.external.core.hook.cache.ExternalCmdCache;
import com.iohao.game.external.core.message.ExternalMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

/**
 * 游戏对外服缓存
 *
 * @author 渔民小镇
 * @date 2023-07-02
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExternalCmdCacheHandler extends SimpleChannelInboundHandler<ExternalMessage> {

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
    protected void channelRead0(ChannelHandlerContext ctx, ExternalMessage message) {
        ExternalCmdCache externalCmdCache = ExternalGlobalConfig.externalCmdCache;

        ExternalMessage cache = externalCmdCache.getCache(message);
        if (Objects.nonNull(cache)) {
            // 从缓存中取到了数据，直接返回缓存数据
            ctx.writeAndFlush(cache);
            return;
        }

        // 交给下一个业务处理 (handler) , 下一个业务指的是你编排 handler 时的顺序
        ctx.fireChannelRead(message);
    }


    public ExternalCmdCacheHandler() {

    }

    public static ExternalCmdCacheHandler me() {
    	return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final ExternalCmdCacheHandler ME = new ExternalCmdCacheHandler();
    }
}

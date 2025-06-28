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
package com.iohao.game.external.core.netty.handler.check;

import com.iohao.game.external.core.config.ExternalGlobalConfig;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author 渔民小镇
 * @date 2025-06-28
 * @since 21.29
 */
public final class TcpProtocolSanityCheckHandler extends ByteToMessageDecoder {
    private boolean closed = false;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {

        if (closed || in.readableBytes() < 4) {
            return;
        }

        in.markReaderIndex();
        int length = in.readInt();
        in.resetReaderIndex();

        if (length <= 0 || length > ExternalGlobalConfig.CoreOption.packageMaxSize) {
            closed = true;
            ctx.close();
        } else {
            ctx.pipeline().remove(this);
        }
    }
}

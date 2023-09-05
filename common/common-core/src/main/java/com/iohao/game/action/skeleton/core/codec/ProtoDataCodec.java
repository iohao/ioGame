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
package com.iohao.game.action.skeleton.core.codec;

import com.iohao.game.common.consts.CommonConst;
import com.iohao.game.common.kit.ProtoKit;

import java.util.Objects;

/**
 * 业务参数的 proto 编解码器
 *
 * @author 渔民小镇
 * @date 2022-05-18
 */
@SuppressWarnings("unchecked")
public final class ProtoDataCodec implements DataCodec {
    @Override
    public byte[] encode(Object data) {
        return ProtoKit.toBytes(data);
    }

    @Override
    public <T> T decode(final byte[] data, Class<?> dataClass) {

        if (Objects.isNull(data)) {
            return (T) ProtoKit.parseProtoByte(CommonConst.EMPTY_BYTES, dataClass);
        }

        return (T) ProtoKit.parseProtoByte(data, dataClass);
    }

    @Override
    public String codecName() {
        return "j-protobuf";
    }
}

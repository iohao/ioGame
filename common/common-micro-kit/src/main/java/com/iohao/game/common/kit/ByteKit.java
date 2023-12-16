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
package com.iohao.game.common.kit;

import lombok.experimental.UtilityClass;

/**
 * @author 渔民小镇
 * @date 2023-12-16
 */
@UtilityClass
class ByteKit {
    public long toLong(byte[] bytes) {
        long values = 0;

        for (int i = 0; i < Long.BYTES; i++) {
            values <<= Byte.SIZE;
            values |= (bytes[i] & 0xff);
        }

        return values;
    }

    public byte[] toBytes(long value) {
        return new byte[]{
                (byte) (value >> 56 & 0xff),
                (byte) (value >> 48 & 0xff),
                (byte) (value >> 40 & 0xff),
                (byte) (value >> 32 & 0xff),
                (byte) (value >> 24 & 0xff),
                (byte) (value >> 16 & 0xff),
                (byte) (value >> 8 & 0xff),
                (byte) (value & 0xff)
        };
    }

    public byte[] toBytes(int intValue) {
        return new byte[]{
                (byte) ((intValue >> 24) & 0xFF),
                (byte) ((intValue >> 16) & 0xFF),
                (byte) ((intValue >> 8) & 0xFF),
                (byte) (intValue & 0xFF)
        };
    }

    public int toInt(byte[] bytes) {
        return bytes[3] & 0xFF |
                (bytes[2] & 0xFF) << 8 |
                (bytes[1] & 0xFF) << 16 |
                (bytes[0] & 0xFF) << 24;
    }
}

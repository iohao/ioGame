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
package com.iohao.game.common.kit;

import lombok.experimental.UtilityClass;

/**
 * @author 渔民小镇
 * @date 2024-08-10
 * @since 21.15
 */
@UtilityClass
public class ByteKit {
    /**
     * 将 long 值转换为 byte 数组
     *
     * @param value value
     * @return byte[]
     */
    public byte[] toBytes(long value) {
        byte[] b = new byte[8];
        b[7] = (byte) (value & 0xff);
        b[6] = (byte) (value >> 8 & 0xff);
        b[5] = (byte) (value >> 16 & 0xff);
        b[4] = (byte) (value >> 24 & 0xff);
        b[3] = (byte) (value >> 32 & 0xff);
        b[2] = (byte) (value >> 40 & 0xff);
        b[1] = (byte) (value >> 48 & 0xff);
        b[0] = (byte) (value >> 56 & 0xff);
        return b;
    }

    /**
     * get long value
     *
     * @param array byte[]
     * @return long
     */
    public long getLong(byte[] array) {
        return ((((long) array[0] & 0xff) << 56)
                | (((long) array[1] & 0xff) << 48)
                | (((long) array[2] & 0xff) << 40)
                | (((long) array[3] & 0xff) << 32)
                | (((long) array[4] & 0xff) << 24)
                | (((long) array[5] & 0xff) << 16)
                | (((long) array[6] & 0xff) << 8)
                | (((long) array[7] & 0xff)));
    }
}

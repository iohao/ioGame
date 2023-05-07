/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General  License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General  License for more details.
 *
 * You should have received a copy of the GNU General  License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.common.kit;

import java.util.Objects;

/**
 * 获取安全的值, 保证返回的不是 null
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public class SafeKit {
    public static int getInt(Integer value) {
        return getInt(value, 0);
    }

    public static int getInt(Integer value, int defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static long getLong(Long value) {
        return getLong(value, 0);
    }

    public static long getLong(Long value, long defaultValue) {
        return Objects.isNull(value) ? defaultValue : value;
    }

    public static boolean getBoolean(Boolean value) {
        return getBoolean(value, false);
    }

    public static boolean getBoolean(Boolean value, boolean defaultValue) {
        return Objects.isNull(value) ? defaultValue : value;
    }
}

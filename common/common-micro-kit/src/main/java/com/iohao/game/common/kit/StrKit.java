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

import com.iohao.game.common.kit.hutool.AdapterHuUtils;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * @author 渔民小镇
 * @date 2022-05-28
 */
@UtilityClass
public class StrKit {

    public String format(@NonNull CharSequence template, @NonNull Map<?, ?> map) {
        return AdapterHuUtils.format(template, map);
    }

    public String format(@NonNull CharSequence template, Object... params) {
        return AdapterHuUtils.format(template, params);
    }

    /**
     * <p>字符串是否为空，空的定义如下：</p>
     * <ol>
     *     <li>{@code null}</li>
     *     <li>空字符串：{@code ""}</li>
     * </ol>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrKit.isEmpty(null)     // true}</li>
     *     <li>{@code StrKit.isEmpty("")       // true}</li>
     *     <li>{@code StrKit.isEmpty(" \t\n")  // false}</li>
     *     <li>{@code StrKit.isEmpty("abc")    // false}</li>
     * </ul>
     *
     * <p>建议：</p>
     * <ul>
     *     <li>该方法建议用于工具类或任何可以预期的方法参数的校验中。</li>
     * </ul>
     *
     * @param str 被检测的字符串
     * @return 是否为空
     */
    public boolean isEmpty(String str) {
        return str == null || str.isEmpty() || str.isBlank();
    }

    public boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }


    /**
     * <p>字符串是否为非空白，非空白的定义如下： </p>
     * <ol>
     *     <li>不为 {@code null}</li>
     *     <li>不为空字符串：{@code ""}</li>
     * </ol>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrKit.isNotEmpty(null)     // false}</li>
     *     <li>{@code StrKit.isNotEmpty("")       // false}</li>
     *     <li>{@code StrKit.isNotEmpty(" \t\n")  // true}</li>
     *     <li>{@code StrKit.isNotEmpty("abc")    // true}</li>
     * </ul>
     *
     * <p>建议：该方法建议用于工具类或任何可以预期的方法参数的校验中。</p>
     *
     * @param str 被检测的字符串
     * @return 是否为非空
     */
    public boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }

        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }
}

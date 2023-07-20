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
package com.iohao.game.common.kit.attr;

import org.jctools.maps.NonBlockingHashMap;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * 动态属性的选项载体
 * <pre>
 *     see {@link AttrOptionDynamic}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-01-31
 */
public class AttrOptions implements Serializable {
    @Serial
    private static final long serialVersionUID = 9042891580724596100L;

    final Map<AttrOption<?>, Object> options = new NonBlockingHashMap<>();

    /**
     * 获取选项值。
     * <p>
     * 如果选项不存在，返回默认值。
     *
     * @param option 选项值
     * @return 如果option不存在，则默认的option值。
     */
    @SuppressWarnings("unchecked")
    public <T> T option(AttrOption<T> option) {
        Object value = options.get(option);
        if (Objects.isNull(value)) {
            value = option.defaultValue();
        }

        return (T) value;
    }

    /**
     * 设置一个具有特定值的新选项。
     * <p>
     * 使用 null 值删除前一个设置的 {@link AttrOption}。
     *
     * @param option 选项值
     * @param value  选项值, null 用于删除前一个 {@link AttrOption}.
     * @return this
     */
    public <T> AttrOptions option(AttrOption<T> option, T value) {
        if (Objects.isNull(value)) {
            options.remove(option);
            return this;
        }

        options.put(option, value);
        return this;
    }
}

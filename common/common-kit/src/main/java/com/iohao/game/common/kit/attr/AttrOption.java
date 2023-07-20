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

import java.io.Serializable;
import java.util.Objects;

/**
 * 动态属性的属性项
 * <pre>
 *     see {@link AttrOptionDynamic}
 * </pre>
 *
 * @param <T> t
 * @author 渔民小镇
 * @date 2022-01-31
 */
public record AttrOption<T>(String name, T defaultValue) implements Serializable {
    /**
     * 初始化 一个 AttrOption
     *
     * @param name name
     * @param <T>  t
     * @return AttrOption
     */
    public static <T> AttrOption<T> valueOf(String name) {
        return new AttrOption<>(name, null);
    }

    /**
     * 初始化 一个 AttrOption
     *
     * @param name         name
     * @param defaultValue 默认值
     * @param <T>          t
     * @return AttrOption
     */
    public static <T> AttrOption<T> valueOf(String name, T defaultValue) {
        return new AttrOption<>(name, defaultValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AttrOption<?> that = (AttrOption<?>) o;

        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}

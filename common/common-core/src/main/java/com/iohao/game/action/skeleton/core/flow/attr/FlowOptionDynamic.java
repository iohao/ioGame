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
package com.iohao.game.action.skeleton.core.flow.attr;

import java.util.Map;
import java.util.Objects;

/**
 * 动态属性接口
 *
 * @author 渔民小镇
 * @date 2022-03-15
 */
public interface FlowOptionDynamic {
    /**
     * 获取动态成员属性
     *
     * @return 动态成员属性
     */
    Map<FlowOption<?>, Object> getOptions();

    /**
     * 是否有选项值。
     *
     * @param option option key
     * @return true 存在
     */
    default boolean hasOption(FlowOption<?> option) {
        return this.getOptions().containsKey(option);
    }

    /**
     * 在动态属性中 获取选项值。
     *
     * @param option 选项值
     * @return value
     */
    @SuppressWarnings("unchecked")
    default <T> T option(FlowOption<T> option) {
        return (T) this.getOptions().get(option);
    }

    /**
     * 在动态属性中设置值
     *
     * @param option option
     * @param value  设置的值，如果是 null 用于删除前一个
     * @param <T>    t
     * @return 前一个值
     */
    @SuppressWarnings("unchecked")
    default <T> T option(FlowOption<T> option, T value) {

        if (Objects.isNull(value)) {
            return (T) this.getOptions().remove(option);
        }

        return (T) this.getOptions().put(option, value);
    }
}

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

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 动态属性 (类型明确的)
 * <pre>
 *     实现该接口的对象, 都会提供动态属性机制
 *     避免类型转换
 * </pre>
 * AttrOptionDynamic options = ...;
 * <pre>
 *     使用示例 - 获取属性 :
 *     AttrOption&lt;Long&gt; timeKey = AttrOption.valueOf("myLongValue");
 *     // set long value
 *     this.option(timeKey, 123L);
 *     // get long value
 *     long val = this.option(timeKey);
 *
 *     AttrOption&lt;Integer&gt; intKey = AttrOption.valueOf("myIntegerValue");
 *     // set int value
 *     this.option(intKey, 123);
 *     // get int value
 *     int age = this.option(intKey);
 *
 * </pre>
 * <pre>
 *     如果你使用了lombok, 推荐这种方式. 只需要在对象中新增此行代码
 *     final AttrOptions options = new AttrOptions();
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-01-31
 */
public interface AttrOptionDynamic {
    /**
     * 获取动态成员属性
     *
     * @return 动态成员属性
     */
    AttrOptions getOptions();

    /**
     * 获取选项值。
     * <p>
     * 如果选项不存在，返回默认值。
     *
     * @param option 选项值
     * @return 如果option不存在，则使用默认的 option 值。
     */
    default <T> T option(AttrOption<T> option) {
        return this.getOptions().option(option);
    }

    /**
     * 获取选项值。
     * <p>
     * 如果选项不存在，返回设定值。
     *
     * @param option 选项值
     * @param value  设定值
     * @return 如果option不存在，则默认的设定值。
     */
    default <T> T optionValue(AttrOption<T> option, T value) {
        T data = this.option(option);

        if (Objects.isNull(data)) {
            return value;
        }

        return data;
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
    default <T> AttrOptions option(AttrOption<T> option, T value) {
        return this.getOptions().option(option, value);
    }

    /**
     * 如果动态属性存在，则使用该值执行给定操作，否则不执行任何操作。
     *
     * @param option   option
     * @param consumer 只有 option 的值存在且不为 null 时，则要执行的动作
     * @param <T>      t
     */
    default <T> void ifPresent(AttrOption<T> option, Consumer<T> consumer) {
        T data = this.option(option);
        if (Objects.nonNull(data)) {
            consumer.accept(data);
        }
    }
}

/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.common.kit.attr;

import java.util.Objects;

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

}

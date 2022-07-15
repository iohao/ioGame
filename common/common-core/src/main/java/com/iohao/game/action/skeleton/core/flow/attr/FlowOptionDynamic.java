/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

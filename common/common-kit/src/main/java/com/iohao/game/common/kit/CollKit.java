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
package com.iohao.game.common.kit;


import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 集合相关工具
 *
 * @author 渔民小镇
 * @date 2022-01-14
 */
@UtilityClass
public class CollKit {
    /**
     * 分组统计
     * <pre>
     *     key is 元素下标
     *     value is 元素下标对应的数量
     * </pre>
     *
     * <pre>
     *     示例
     *     handCards: [11, 11, 11, 21, 46, 33,33, 18, 18, 18, 18]
     *
     *     得到的 map {
     *         11 : 3
     *         18 : 4
     *         21 : 1
     *         33 : 2
     *         46 : 1
     *     }
     * </pre>
     *
     * @param list 元素列表
     * @return map
     */
    public Map<Integer, Integer> groupCounting(List<Integer> list) {
        return list.stream().
                collect(
                        Collectors.groupingBy(Function.identity(), Collectors.summingInt(e -> 1))
                );
    }

    public boolean notEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

}

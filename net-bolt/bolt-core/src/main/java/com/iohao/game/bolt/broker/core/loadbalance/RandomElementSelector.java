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
package com.iohao.game.bolt.broker.core.loadbalance;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机的元素选择器
 *
 * @author 渔民小镇
 * @date 2022-05-15
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class RandomElementSelector<T> implements ElementSelector<T> {
    final List<T> elements;
    final int size;

    public RandomElementSelector(List<T> elements) {
        this.elements = elements;
        this.size = elements.size();
    }

    @Override
    public T next() {

        if (size > 1) {

            ThreadLocalRandom random = ThreadLocalRandom.current();
            T element = elements.get(random.nextInt(size));

            if (Objects.isNull(element)) {
                element = elements.get(0);
            }

            return element;

        } else if (size == 1) {
            return elements.get(0);
        }

        return null;
    }

    @Override
    public T get() {
        T next = next();

        if (Objects.isNull(next)) {
            throw new NullPointerException("RandomSelector next is null!");
        }

        return next;
    }
}

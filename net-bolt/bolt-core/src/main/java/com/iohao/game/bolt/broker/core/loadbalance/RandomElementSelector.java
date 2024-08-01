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
package com.iohao.game.bolt.broker.core.loadbalance;

import com.iohao.game.common.kit.exception.ThrowKit;
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
                element = elements.getFirst();
            }

            return element;

        } else if (size == 1) {
            return elements.getFirst();
        }

        return null;
    }

    @Override
    public T get() {
        T next = next();

        if (Objects.isNull(next)) {
            ThrowKit.ofNullPointerException("RandomSelector next is null!");
        }

        return next;
    }
}

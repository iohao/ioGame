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
package com.iohao.game.common.kit.collect;

import org.jctools.maps.NonBlockingHashMap;
import org.jctools.maps.NonBlockingHashSet;

import java.util.*;
import java.util.function.Consumer;

/**
 * 线程安全的 SetMultiMap 实现
 * <pre>
 *     value 为 set 集合实现
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-07
 */
final class NonBlockingSetMultiMap<K, V> implements SetMultiMap<K, V> {
    private final NonBlockingHashMap<K, Set<V>> map = new NonBlockingHashMap<>();

    @Override
    public Map<K, Set<V>> asMap() {
        return this.map;
    }

    @Override
    public Set<V> ofIfAbsent(K key, Consumer<Set<V>> consumer) {
        var set = this.map.get(key);

        if (Objects.isNull(set)) {
            Set<V> newValueSet = new NonBlockingHashSet<>();
            set = this.map.putIfAbsent(key, newValueSet);

            if (Objects.isNull(set)) {
                Set<V> initSet = this.map.get(key);

                // 首次初始化回调
                Optional.ofNullable(consumer).ifPresent(c -> c.accept(initSet));

                return initSet;
            }
        }

        return set;
    }

    @Override
    public Set<Map.Entry<K, Set<V>>> entrySet() {
        return this.map.entrySet();
    }
}

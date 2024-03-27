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

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * 线程安全的 ListMultiMap
 * <pre>
 *     value 为 list 集合实现
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-07
 */
final class NonBlockingListMultiMap<K, V> implements ListMultiMap<K, V> {
    private final Map<K, List<V>> map = new NonBlockingHashMap<>();

    @Override
    public Map<K, List<V>> asMap() {
        return this.map;
    }

    @Override
    public List<V> ofIfAbsent(K key, Consumer<List<V>> consumer) {
        var list = this.map.get(key);

        if (Objects.isNull(list)) {
            List<V> newValueList = new CopyOnWriteArrayList<>();
            list = this.map.putIfAbsent(key, newValueList);

            if (Objects.isNull(list)) {
                List<V> initList = this.map.get(key);

                // 首次初始化回调
                Optional.ofNullable(consumer).ifPresent(c -> c.accept(initList));

                return initList;
            }
        }

        return list;
    }
}

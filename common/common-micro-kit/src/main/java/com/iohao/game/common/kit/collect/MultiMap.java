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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author 渔民小镇
 * @date 2023-12-07
 */
interface MultiMap<K, V> {
    /**
     * 内部实现的真实 map
     *
     * @return map
     */
    Map<K, ? extends Collection<V>> asMap();

    /**
     * 根据 key 来 get 一个元素，如果不存在就创建集合
     *
     * @param key key
     * @return collection 一定不为 null
     */
    Collection<V> of(K key);

    /**
     * get 一个元素
     *
     * @param key key
     * @return collection
     */
    Collection<V> get(K key);

    /**
     * clear key 所对应的集合内的所有元素
     *
     * @param key key
     * @return clear 后的集合，一定不为 null
     */
    default Collection<V> clearAll(K key) {
        var collection = this.of(key);

        collection.clear();

        return collection;
    }

    /**
     * map 集合的数目
     *
     * @return size
     */
    default int size() {
        return this.asMap().size();
    }

    /**
     * map 所有 value 集合的汇总
     *
     * @return value size
     */
    default int sizeValue() {
        return this.asMap().values().stream()
                .mapToInt(Collection::size)
                .sum();
    }

    /**
     * 向指定 key 的集合添加元素
     *
     * @param key   key
     * @param value 元素
     * @return true if this collection changed as a result of the call
     */
    default boolean put(K key, V value) {
        var collection = this.of(key);
        return collection.add(value);
    }

    default boolean isEmpty() {
        return this.asMap().isEmpty();
    }

    default boolean containsKey(K key) {
        return this.asMap().containsKey(key);
    }

    default boolean containsValue(V value) {
        for (Collection<V> vs : this.asMap().values()) {
            if (vs.contains(value)) {
                return true;
            }
        }

        return false;
    }

    default void clear() {
        this.asMap().clear();
    }

    default Set<K> keySet() {
        return this.asMap().keySet();
    }
}

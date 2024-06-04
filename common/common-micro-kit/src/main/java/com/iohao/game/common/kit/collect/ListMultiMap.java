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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 线程安全的 ListMultiMap
 * <pre>
 *     value 为 list 集合实现
 * </pre>
 * for example
 * <pre>{@code
 * ListMultiMap<Integer, String> map = ListMultiMap.of();
 * map.put(1, "a");
 * map.put(1, "a");
 * map.put(1, "b");
 *
 * List<String> list2 = map.get(2); // is null
 * List<String> list2 = map.of(2); // is empty list
 *
 * list2.add("2 - a");
 * list2.add("2 - a");
 * map.sizeValue(); // sizeValue == 5
 *
 * map.containsValue("a"); // true
 * map.containsValue("b"); // true
 *
 * Set<Integer> keySet = this.map.keySet();
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-07
 */
public interface ListMultiMap<K, V> extends MultiMap<K, V> {
    @Override
    Map<K, List<V>> asMap();

    /**
     * 根据 key 来 get 一个元素，如果不存在就创建集合
     * <pre>
     *     get 的元素集合一定不为 null，如果不存在就新创建。
     *
     *     首次创建该 key 的集合时，会调用 consumer 并将新创建集合传入 consumer 中。
     *
     *     开发者有需要初始化的内容，可以通过 consumer 来实现
     * </pre>
     *
     * @param key      key
     * @param consumer consumer
     * @return 集合
     */
    List<V> ofIfAbsent(K key, Consumer<List<V>> consumer);

    @Override
    default List<V> of(K key) {
        return this.ofIfAbsent(key, null);
    }

    @Override
    default List<V> get(K key) {
        return asMap().get(key);
    }

    default Set<Map.Entry<K, List<V>>> entrySet() {
        return this.asMap().entrySet();
    }

    /**
     * 创建 ListMultiMap（框架内置实现）。请使用 {@link ListMultiMap#of()} 代替
     *
     * @param <K> k
     * @param <V> v
     * @return ListMultiMap
     */
    static <K, V> ListMultiMap<K, V> create() {
        return of();
    }

    /**
     * 创建 ListMultiMap（框架内置实现）
     *
     * @param <K> k
     * @param <V> v
     * @return ListMultiMap
     */
    static <K, V> ListMultiMap<K, V> of() {
        return new NonBlockingListMultiMap<>();
    }
}
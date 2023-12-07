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
package com.iohao.game.common.kit.collect;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author 渔民小镇
 * @date 2023-12-07
 */
public interface SetMultiMap<K, V> extends MultiMap<K, V> {
    default Set<V> get(K key) {
        return asMap().get(key);
    }

    @Override
    default Set<V> of(K key) {
        return this.ofIfAbsent(key, null);
    }

    Set<V> ofIfAbsent(K key, Consumer<Set<V>> consumer);

    @Override
    Map<K, Set<V>> asMap();
    
    static <K, V> SetMultiMap<K, V> create() {
        return new NonBlockingSetMultiMap<>();
    }
}

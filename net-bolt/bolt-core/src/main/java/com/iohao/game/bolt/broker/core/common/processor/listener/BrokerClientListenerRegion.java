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
package com.iohao.game.bolt.broker.core.common.processor.listener;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashSet;

import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * BrokerClient 监听管理域
 *
 * @author 渔民小镇
 * @date 2023-12-14
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrokerClientListenerRegion {
    final Set<BrokerClientListener> listeners = new NonBlockingHashSet<>();

    public boolean isEmpty() {
        return listeners.isEmpty();
    }

    public void add(BrokerClientListener listener) {
        Objects.requireNonNull(listener);
        this.listeners.add(listener);
    }

    public void remove(BrokerClientListener listener) {
        this.listeners.remove(listener);
    }

    public void clear() {
        this.listeners.clear();
    }

    public void forEach(Consumer<BrokerClientListener> consumer) {
        this.listeners.forEach(consumer);
    }
}

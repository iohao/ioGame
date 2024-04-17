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
package com.iohao.game.common.kit.beans.property;

import lombok.ToString;

/**
 * object - 属性具备监听特性。当值（引用）发生变更时，会触发监听事件。
 * <pre>{@code
 *         YourUser user = new YourUser();
 *
 *         var property = new ObjectProperty(user);
 *         // add listener monitor property object
 *         property.addListener((observable, oldValue, newValue) -> {
 *             log.info("oldValue:{}, newValue:{}", oldValue, newValue);
 *         });
 *
 *         property.set(user); // does not trigger listeners
 *
 *         YourUser user2 = new YourUser();
 *         property.set(user2); // When the value changes,listeners are triggered
 *         property.get(); // value is user2
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-04-17
 */
@ToString
public final class ObjectProperty<T> extends AbstractPropertyValueObservable<T> {
    T value;

    public ObjectProperty() {
        this(null);
    }

    public ObjectProperty(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return get();
    }

    @Override
    public void setValue(T value) {
        this.set(value);
    }

    /**
     * get current value
     *
     * @return current value
     */
    public T get() {
        this.valid = true;
        return this.value;
    }

    /**
     * set current value
     *
     * @param newValue current new value
     */
    public void set(T newValue) {
        if (this.value != newValue) {
            this.value = newValue;
            markInvalid();
        }
    }
}

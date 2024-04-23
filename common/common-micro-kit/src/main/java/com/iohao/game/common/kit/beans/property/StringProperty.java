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

import java.util.Objects;

/**
 * String - 属性具备监听特性。当值发生变更时，会触发监听事件。
 * <pre>{@code
 *         var property = new StringProperty();
 *         // add listener monitor property object
 *         property.addListener((observable, oldValue, newValue) -> {
 *             log.info("oldValue:{}, newValue:{}", oldValue, newValue);
 *         });
 *
 *         property.get(); // value is null
 *         property.set("22"); // When the value changes,listeners are triggered
 *         property.get(); // value is "22"
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-04-17
 */
@ToString
public final class StringProperty extends AbstractPropertyValueObservable<String> {
    String value;

    public StringProperty() {
        this(null);
    }

    public StringProperty(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return get();
    }

    @Override
    public void setValue(String value) {
        this.set(value);
    }

    /**
     * get current value
     *
     * @return current value
     */
    public String get() {
        this.valid = true;
        return this.value;
    }

    /**
     * set current value
     *
     * @param newValue current new value
     */
    public void set(String newValue) {
        if (!Objects.equals(this.value, newValue)) {
            this.value = newValue;
            markInvalid();
        }
    }
}

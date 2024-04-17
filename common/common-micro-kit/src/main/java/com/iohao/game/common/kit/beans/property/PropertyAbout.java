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

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author 渔民小镇
 * @date 2024-04-17
 */
abstract class ChangeHelper<T> {
    final PropertyValueObservable<T> observable;

    /**
     * 触发值变更事件
     */
    protected abstract void fireValueChangedEvent();

    ChangeHelper(PropertyValueObservable<T> observable) {
        this.observable = observable;
    }

    static <T> ChangeHelper<T> create(PropertyValueObservable<T> observable, PropertyChangeListener<? super T> listener) {
        return new PropertySingleChange<>(observable, observable.getValue(), listener);
    }

    static <T> ChangeHelper<T> create(PropertyChangeListener<? super T> listener) {
        return new PropertySingleChange<>(null, null, listener);
    }

    private static class PropertySingleChange<T> extends ChangeHelper<T> {
        final PropertyChangeListener<? super T> listener;
        T currentValue;

        PropertySingleChange(PropertyValueObservable<T> observable, T currentValue, PropertyChangeListener<? super T> listener) {
            super(observable);
            this.currentValue = currentValue;
            this.listener = listener;
        }

        @Override
        protected void fireValueChangedEvent() {
            final T oldValue = currentValue;
            currentValue = observable.getValue();
            final boolean changed = !Objects.equals(currentValue, oldValue);
            if (changed) {
                try {
                    listener.changed(observable, oldValue, currentValue);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        }

        @Override
        public final boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof PropertySingleChange<?> change)) {
                return false;
            }

            return Objects.equals(listener, change.listener);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(listener);
        }
    }
}

@FieldDefaults(level = AccessLevel.PRIVATE)
final class ChangeHelperList<T> {
    List<ChangeHelper<? super T>> list;

    void addListener(ChangeHelper<? super T> helper) {
        if (Objects.isNull(this.list)) {
            this.list = new CopyOnWriteArrayList<>();
        }

        this.list.add(helper);
    }

    void removeListener(PropertyChangeListener<? super T> listener) {
        if (Objects.isNull(this.list) || Objects.isNull(listener)) {
            return;
        }

        var helper = ChangeHelper.create(listener);
        this.list.remove(helper);
    }

    void addListener(PropertyValueObservable<T> observable, PropertyChangeListener<? super T> listener) {

        if (observable == null || listener == null) {
            throw new NullPointerException();
        }

        var helper = ChangeHelper.create(observable, listener);
        this.addListener(helper);
    }

    void fireValueChangedEvent() {
        if (Objects.isNull(this.list)) {
            return;
        }

        this.list.forEach(ChangeHelper::fireValueChangedEvent);
    }
}

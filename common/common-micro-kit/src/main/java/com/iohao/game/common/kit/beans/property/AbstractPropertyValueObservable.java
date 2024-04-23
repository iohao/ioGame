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

import java.util.Objects;

/**
 * PropertyValueObservable adapter
 *
 * @author 渔民小镇
 * @date 2024-04-17
 * @see IntegerProperty
 * @see LongProperty
 * @see StringProperty
 * @see BooleanProperty
 * @see ObjectProperty
 */
abstract class AbstractPropertyValueObservable<T> implements PropertyValueObservable<T> {
    protected boolean valid = true;
    ChangeHelperList<T> helperList;

    @Override
    public void addListener(PropertyChangeListener<? super T> listener) {
        if (Objects.isNull(this.helperList)) {
            this.helperList = new ChangeHelperList<>();
        }

        this.helperList.addListener(this, listener);
    }

    @Override
    public void removeListener(PropertyChangeListener<? super T> listener) {
        if (Objects.nonNull(this.helperList)) {
            this.helperList.removeListener(listener);
        }
    }

    protected void markInvalid() {
        if (this.valid) {
            this.valid = false;
            this.fireValueChangedEvent();
        }
    }

    private void fireValueChangedEvent() {
        if (Objects.nonNull(this.helperList)) {
            this.helperList.fireValueChangedEvent();
        }
    }
}
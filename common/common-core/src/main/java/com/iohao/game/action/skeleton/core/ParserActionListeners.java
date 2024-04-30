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
package com.iohao.game.action.skeleton.core;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author 渔民小镇
 * @date 2024-04-30
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
final class ParserActionListeners {
    final List<ParserActionListener> listeners = new CopyOnWriteArrayList<>();

    void removeParserActionListener(Class<? extends ParserActionListener> listenerClazz) {
        listeners.removeIf(parserActionListener -> Objects.equals(parserActionListener.getClass(), listenerClazz));
    }

    void addParserActionListener(ParserActionListener listener) {
        listeners.add(listener);
    }

    void onActionController(ParserActionController parserActionController, ParserListenerContext context) {
        listeners.forEach(listener -> listener.onActionController(parserActionController, context));
    }

    void onActionCommand(ParserActionCommand parserActionCommand, ParserListenerContext context) {
        listeners.forEach(listener -> listener.onActionCommand(parserActionCommand, context));
    }
}

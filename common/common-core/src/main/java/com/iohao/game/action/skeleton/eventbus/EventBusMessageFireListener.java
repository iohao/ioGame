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
package com.iohao.game.action.skeleton.eventbus;

/**
 * 监听
 *
 * @author 渔民小镇
 * @date 2023-12-24
 */
public interface EventBusMessageFireListener {

    /**
     * 事件消息没有对应的订阅者时，所触发的监听回调
     * <pre>
     *     注意，默认情况下只有调用 {@link EventBus#fire} 方法时才会检测。
     * </pre>
     *
     * @param eventBusMessage 事件消息
     * @param eventBus        eventBus
     */
    void fireEmpty(EventBusMessage eventBusMessage, EventBus eventBus);

    static EventBusMessageFireListener defaultInstance() {
        return DefaultEventBusMessageFireListener.me();
    }
}

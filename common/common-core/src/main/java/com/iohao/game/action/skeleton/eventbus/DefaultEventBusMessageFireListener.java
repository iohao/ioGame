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

import lombok.extern.slf4j.Slf4j;

/**
 * @author 渔民小镇
 * @date 2023-12-24
 */
@Slf4j
final class DefaultEventBusMessageFireListener implements EventBusMessageFireListener {
    @Override
    public void fireEmpty(EventBusMessage eventBusMessage, EventBus eventBus) {
        Object eventSource = eventBusMessage.getEventSource();
        Class<?> clazz = eventSource.getClass();
        String simpleName = clazz.getName();
        log.warn("事件源[{}]，没有配置订阅者。{}", clazz.getSimpleName(), simpleName);
    }

    static DefaultEventBusMessageFireListener me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final DefaultEventBusMessageFireListener ME = new DefaultEventBusMessageFireListener();
    }
}

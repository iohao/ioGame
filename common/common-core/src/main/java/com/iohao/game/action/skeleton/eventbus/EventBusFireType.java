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
package com.iohao.game.action.skeleton.eventbus;

/**
 * 发布事件时，所触发的类型
 *
 * @author 渔民小镇
 * @date 2023-12-24
 * @see EventBus
 * @since 21
 */
public interface EventBusFireType {
    /**
     * 当前进程
     * <pre>
     *     表示所使用的 EventBus 中的订阅者接收了事件消息。
     *
     *     举例：
     *     EventBus 是逻辑服事件总线。 EventBus、业务框架、逻辑服三者是 1:1:1 的关系。
     *
     *     假设我们有两个游戏逻辑服，分别是 UserServer 和 EmailServer；那么每个游戏逻辑服会对应一个 EventBus，
     *     假设 user EventBus 使用 fireMe 发布事件，只会被 user 上注册的订阅者接收到消息。
     *     即使其他游戏逻辑服订阅了该事件源，也不会接收到消息。
     * </pre>
     */
    int fireMe = 1;
    /** 当前进程的其他 EventBus */
    int fireLocalNeighbor = 2;
    /** 当前进程所有订阅者收了事件消息 */
    int fireLocal = 4;
    /**
     * 远程
     * <pre>
     *     表示远程有订阅者接收了事件消息。（通常指的是跨服）
     * </pre>
     */
    int fireRemote = 8;
}

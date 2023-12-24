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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * 事件消息
 *
 * @author 渔民小镇
 * @date 2023-12-24
 */
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class EventBusMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = -1661393033598374515L;
    /**
     * userId
     * <pre>
     *     通常，我们可以将 userId 看成是 threadIndex，
     *     因为该属性的主要作用是确定使用哪个线程执行器
     * </pre>
     */
    long userId;

    String traceId;
    /** 事件源 */
    Object eventSource;
    /** 其他进程的信息 */
    Set<EventBrokerClientMessage> eventBrokerClientMessageSet;
    @Getter(AccessLevel.PRIVATE)
    transient int fireType;

    /**
     * 添加已经触发的订阅者类型
     *
     * @param fireType {@link EventBusFireType}
     * @see EventBusFireType
     */
    public void addFireType(int fireType) {
        this.fireType |= fireType;
    }

    /**
     * 已经触发的订阅者类型是否存在
     *
     * @param fireType {@link EventBusFireType}
     * @return true 存在
     * @see EventBusFireType
     */
    public boolean containsFireType(int fireType) {
        return (this.fireType & fireType) == fireType;
    }

    public boolean emptyFireType() {
        return fireType == 0;
    }
}

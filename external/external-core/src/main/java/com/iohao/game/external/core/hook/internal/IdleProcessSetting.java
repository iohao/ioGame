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
package com.iohao.game.external.core.hook.internal;

import com.iohao.game.external.core.hook.IdleHook;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.TimeUnit;

/**
 * @author 渔民小镇
 * @date 2023-02-18
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class IdleProcessSetting {
    /** 心跳整体时间 */
    long idleTime = 300;
    /** 读 - 心跳时间 */
    long readerIdleTime = idleTime;
    /** 写 - 心跳时间 */
    long writerIdleTime = idleTime;
    /** all - 心跳时间 */
    long allIdleTime = idleTime;
    /** 心跳时间单位 - 默认秒单位 */
    TimeUnit timeUnit = TimeUnit.SECONDS;
    /** true : 响应心跳给客户端 */
    boolean pong = true;
    /** 心跳钩子 */
    IdleHook<?> idleHook;

    /**
     * 心跳整体时间设置包括：readerIdleTime、writerIdleTime、allIdleTime
     *
     * @param idleTime 整体时间
     * @return this
     */
    public IdleProcessSetting setIdleTime(long idleTime) {
        this.idleTime = idleTime;
        this.readerIdleTime = idleTime;
        this.writerIdleTime = idleTime;
        this.allIdleTime = idleTime;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> IdleHook<T> getIdleHook() {
        return (IdleHook<T>) idleHook;
    }
}

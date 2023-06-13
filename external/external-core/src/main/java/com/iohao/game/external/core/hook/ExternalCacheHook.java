/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.external.core.hook;

import com.iohao.game.action.skeleton.protocol.ResponseMessage;

import java.time.Duration;

/**
 * @author 渔民小镇
 * @date 2023-06-11
 */
public interface ExternalCacheHook<T> {
    T getCache(T message);

    void setExpireTime(Duration expireTime);

    Duration getExpireTime();

    default void addCmd(int cmd) {
        this.addCmd(cmd, getExpireTime());
    }

    void addCmd(int cmd, Duration expireTime);

    void addCmd(int cmd, int subCmd, Duration expireTime);

    default void addCmd(int cmd, int subCmd) {
        this.addCmd(cmd, subCmd, getExpireTime());
    }

    void put(ResponseMessage responseMessage);
}

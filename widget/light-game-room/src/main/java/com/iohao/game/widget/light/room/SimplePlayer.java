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
package com.iohao.game.widget.light.room;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;

/**
 * 玩家（内置实现）
 *
 * @author 渔民小镇
 * @date 2024-05-12
 * @since 21.8
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimplePlayer implements Player {
    @Serial
    private static final long serialVersionUID = -26338708253909097L;
    /** userId 玩家 id */
    long userId;
    /** 房间 id */
    long roomId;
    /** 用户所在位置 */
    int seat;
    /** true - 已准备 */
    boolean ready;
    /** true robot */
    boolean robot;
    /** true 模仿 robot 机制，但并不是真正的 robot，类似于 robot 托管 */
    boolean maybeRobot;
}

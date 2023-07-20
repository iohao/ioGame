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
package com.iohao.game.widget.light.room;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

/**
 * 抽象玩家
 *
 * @author 渔民小镇
 * @date 2022-03-31
 */
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractPlayer implements Serializable {
    @Serial
    private static final long serialVersionUID = -26338708253909097L;
    /** userId 玩家 id */
    long id;
    /** 用户所在位置 */
    int seat;
    /** true - 已准备 */
    boolean ready;
    /** true robot */
    boolean robot;
    /** true 模仿 robot 机制, 但并不是真正的 robot; 类似于 robot 托管 */
    boolean maybeRobot;
}

/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
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

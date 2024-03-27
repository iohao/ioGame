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
package com.iohao.game.bolt.broker.core.client.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * 逻辑服状态配置
 * <pre>
 *     注意，对于状态的处理，目前还没有提供逻辑实现。
 *
 *     状态不使用枚举是因为这样更具备灵活性。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-05-23
 */
@FieldDefaults(level = AccessLevel.PUBLIC)
public class BrokerClientStatusConfig {
    /**
     * 逻辑服状态：正常
     * <pre>
     *     当状态是【正常】时，正常状态的游戏逻辑服可以被任何玩家访问！
     * </pre>
     */
    public static int normal = 1;

    /**
     * 逻辑服状态：维护中
     * <pre>
     *     当状态是【维护中】时，不对正常玩家开放的游戏逻辑服，但绑定中的玩家还可以正常访问。
     *     如果你打算停服维护，可以将状态设置为此状态，直到没有请求可以消费后就可以去停服了！
     *
     *     使用场景：
     *     服务器维护前，将状态设置为【维护】后，正常玩家将不能访问对应的游戏逻辑服，只有绑定了游戏逻辑服的玩家可以访问。
     *     当服务器将剩余的请求都消费完成后，开发者就可以 kill 进程了。
     *
     *     白话一点就是，玩家A 绑定了游戏逻辑服B，现在我们将游戏逻辑服B 状态改成【维护】，
     *     其他玩家访问不了了，但是玩家A 可以继续访问，直到玩家A 不需要使用游戏逻辑服B了，开发者就可以把机器给停了。
     * </pre>
     */
    public static int maintenance = 2;

    /**
     * 逻辑服状态：灰度
     * <pre>
     *     当状态是【灰度】时，只有灰度玩家，或连接到灰度游戏对外服的玩家可以访问。
     *
     *     使用场景：
     *     线上测试一些新功能、调试一些在线上才会出现的特定 bug 。
     *     如果逻辑服是灰度状态的，正常玩家将不能访问灰度服务器，只有灰度玩家才能访问。
     *     我们还提供了将整个游戏对外服设置为灰度的，这样只要是连接到这个游戏对外服的玩家都可以访问灰度服务器了，是不是很方便！
     * </pre>
     */
    public static int gray = 4;

    public static int all = normal | maintenance | gray;
}

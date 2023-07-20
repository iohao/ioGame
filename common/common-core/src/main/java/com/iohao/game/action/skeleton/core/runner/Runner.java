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
package com.iohao.game.action.skeleton.core.runner;

import com.iohao.game.action.skeleton.core.BarSkeleton;

/**
 * Runner 机制
 *
 * <pre>会在逻辑服启动后触发 Runner 机制</pre>
 *
 * @author 渔民小镇
 * @date 2023-04-23
 */
public interface Runner {
    /**
     * 启动
     * <pre>
     *     框架会在逻辑服启动后调用一次
     * </pre>
     *
     * @param skeleton 业务框架
     */
    void onStart(BarSkeleton skeleton);

    /**
     * 启动
     * <pre>
     *     框架会在逻辑服启动后调用一次，会在逻辑服将信息注册到 Broker（游戏网关）后触发。
     *     可以与 Broker（游戏网关）通信了。
     *
     *     如果没有特殊需求的，使用 onStart 方法就可以了。
     * </pre>
     *
     * @param skeleton 业务框架
     */
    default void onStartAfter(BarSkeleton skeleton) {
    }

    /**
     * runner name
     *
     * @return name
     */
    default String name() {
        return this.getClass().getName();
    }
}

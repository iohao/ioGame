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
package com.iohao.game.action.skeleton.core;

import lombok.Getter;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;

/**
 * 开发时相关的配置类
 *
 * @author 渔民小镇
 * @date 2022-05-19
 */
@Deprecated
public final class DevConfig {
    /**
     * true 打印广播日志，默认不打印
     * <p>
     * see {@link BarSkeletonBuilderParamConfig#createBuilder()}
     *
     * @deprecated 请使用 {@link IoGameCommonCoreConfig#broadcastLog}
     */
    @Getter
    @Deprecated
    static boolean broadcastLog;

    /**
     * cmd 路由对应的响应数据类型信息
     * <pre>
     *     key : cmdMerge
     *     value : 路由对应的 class 信息
     *
     *     开发阶段的数据辅助信息，目前主要提供给"模拟客户端" 时使用的。
     *
     *     此 map 中，保存了：
     *     1 action 的返回值类信息；
     *     2 广播（推送）时的类信息；
     *
     * </pre>
     */
    static Map<Integer, Class<?>> cmdDataClassMap = new NonBlockingHashMap<>();

    public static Class<?> getCmdDataClass(int cmdMerge) {
        return cmdDataClassMap.get(cmdMerge);
    }

    public static void put(Integer cmdMerge, Class<?> dataClass) {
        cmdDataClassMap.putIfAbsent(cmdMerge, dataClass);
    }
}

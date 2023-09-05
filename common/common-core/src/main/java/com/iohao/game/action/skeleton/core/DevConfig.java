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

public final class DevConfig {
    /**
     * true 打印广播日志，默认不打印
     * <p>
     * see {@link BarSkeletonBuilderParamConfig#createBuilder()}
     */
    @Getter
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

    /**
     * 请使用 DevConfig.put 方法
     *
     * @return map
     */
    @Deprecated
    public static Map<Integer, Class<?>> getCmdDataClassMap() {
        return cmdDataClassMap;
    }

    private DevConfig() {

    }

    /**
     * 已经标记过期，将在下个大版本移除
     * <pre>
     *     请直接使用静态方法代替； DevConfig.xxx
     * </pre>
     *
     * @return me
     */
    @Deprecated
    public static DevConfig me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final DevConfig ME = new DevConfig();
    }
}

/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
@Getter
public class DevConfig {
    /**
     * true 打印广播日志，默认不打印
     *
     * @see {@link BarSkeletonBuilderParamConfig#createBuilder()}
     */
    boolean broadcastLog;

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
    Map<Integer, Class<?>> cmdDataClassMap = new NonBlockingHashMap<>();

    private DevConfig() {

    }

    public static DevConfig me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final DevConfig ME = new DevConfig();
    }
}

/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.action.skeleton.core.commumication;

import com.iohao.game.action.skeleton.protocol.external.ResponseCollectExternalMessage;

import java.io.Serializable;

/**
 * 内部模块通讯上下文，内部模块指的是游戏对外服
 * <pre>
 *     单个游戏逻辑服与多个游戏对外服通信请求（可跨进程）
 *
 *     为了区别与游戏逻辑服，这里没有在游戏对外服复用业务框架，
 *     而是使用一个业务码来表示路由。
 *     虽然游戏对外服也是逻辑服的一种，如果在游戏对外服中也使用业务框架，
 *     会给游戏开发者造成一种混乱，所以这里使用业务码
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-07-27
 */
public interface InvokeExternalModuleContext {

    /**
     * 【游戏逻辑服】访问多个【游戏对外服】
     * <pre>
     *     有些数据只存在于游戏对外服，但由于游戏对外服可能会有多个，特别是在分步式场景下。
     *     所以这里发起请求时，会调用多个游戏对外服来处理这个请求。
     * </pre>
     *
     * @param bizCode 业务码
     * @param data    业务参数
     * @return ResponseCollectExternalMessage
     */
    ResponseCollectExternalMessage invokeExternalModuleCollectMessage(int bizCode, Serializable data);

    /**
     * 【游戏逻辑服】访问多个【游戏对外服】
     * <pre>
     *     有些数据只存在于游戏对外服，但由于游戏对外服可能会有多个，特别是在分步式场景下。
     *     所以这里发起请求时，会调用多个游戏对外服来处理这个请求。
     * </pre>
     *
     * @param bizCode 业务码
     * @return ResponseCollectExternalMessage
     */
    default ResponseCollectExternalMessage invokeExternalModuleCollectMessage(int bizCode) {
        return this.invokeExternalModuleCollectMessage(bizCode, null);
    }
}

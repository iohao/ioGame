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
package com.iohao.game.action.skeleton.core.commumication;

import com.iohao.game.action.skeleton.protocol.external.RequestCollectExternalMessage;
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
     *     有些数据只存在于游戏对外服，但由于游戏对外服可能会有多个，特别是在分布式场景下。
     *     所以这里发起请求时，会调用多个游戏对外服来处理这个请求。
     * </pre>
     *
     * @param bizCode 业务码
     * @param data    业务参数
     * @return ResponseCollectExternalMessage 一定不为 null
     */
    ResponseCollectExternalMessage invokeExternalModuleCollectMessage(int bizCode, Serializable data);

    /**
     * 【游戏逻辑服】访问多个【游戏对外服】
     * <pre>
     *     有些数据只存在于游戏对外服，但由于游戏对外服可能会有多个，特别是在分布式场景下。
     *     所以这里发起请求时，会调用多个游戏对外服来处理这个请求。
     * </pre>
     *
     * @param bizCode 业务码
     * @return ResponseCollectExternalMessage 一定不为 null
     */
    default ResponseCollectExternalMessage invokeExternalModuleCollectMessage(int bizCode) {
        return this.invokeExternalModuleCollectMessage(bizCode, null);
    }

    /**
     * 【游戏逻辑服】访问多个【游戏对外服】
     * <pre>
     *     有些数据只存在于游戏对外服，但由于游戏对外服可能会有多个，特别是在分布式场景下。
     *     所以这里发起请求时，会调用多个游戏对外服来处理这个请求。
     * </pre>
     *
     * @param request 请求
     * @return ResponseCollectExternalMessage 一定不为 null
     */
    ResponseCollectExternalMessage invokeExternalModuleCollectMessage(RequestCollectExternalMessage request);
}

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
package com.iohao.game.action.skeleton.kit;

import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.common.kit.concurrent.executor.*;

import java.io.Serializable;

/**
 * 执行器选择枚举
 * <pre>
 *     具体阅读参考 DefaultRequestMessageClientProcessorHook 相关源码
 *
 *     可将枚举设置到 {@link HeadMetadata#setExecutorSelect(ExecutorSelectEnum)} 中。框架会在执行 action 前，根据 ExecutorSelectEnum 值来选择对应的执行器。
 *
 *     当为 null 或 userExecutor 时，使用 {@link ExecutorRegion#getUserThreadExecutorRegion()} 策略，该策略【保证线程安全】
 *     该策略可以确保同一玩家的 action 请求在同一线程执行器中执行。
 *
 *     当为 userVirtualExecutor 时，使用 {@link ExecutorRegion#getUserVirtualThreadExecutorRegion()} ()} 策略，该策略【不保证线程安全】
 *     该策略使用虚拟线程执行 action 请求，如果你能确定你的某些 action 执行较为耗时，且不需要保证线程的，可以使用该策略。
 *
 *     当为 currentThread 时，不使用任何线程执行器，而是在 netty 线程中执行 action 请求；该策略【不保证线程安全】
 *     (具体可阅读 RequestMessageClientProcessor、DefaultRequestMessageClientProcessorHook 相关源码)
 *
 *     customExecutor 则是预留给开发者的，如果框架提供的以上策略都满足不了业务的，可以考虑扩展 RequestMessageClientProcessorHook 接口
 *     关于扩展可以参考 <a href="https://www.yuque.com/iohao/game/eixd6x#KA814">ioGame 线程相关文档</a>
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-19
 * @see ExecutorRegion#getUserThreadExecutorRegion()
 * @see ExecutorRegion#getUserVirtualThreadExecutorRegion()
 */
public enum ExecutorSelectEnum implements Serializable {
    /**
     * 在用户线程执行器中执行
     *
     * @see ExecutorRegion#getUserThreadExecutorRegion()
     */
    userExecutor,
    /**
     * 在虚拟线程执行器中执行
     *
     * @see ExecutorRegion#getUserVirtualThreadExecutorRegion()
     */
    userVirtualExecutor,
    /** netty 线程中执行 action 请求 */
    currentThread,
    /** 预留给开发者的 */
    customExecutor
}

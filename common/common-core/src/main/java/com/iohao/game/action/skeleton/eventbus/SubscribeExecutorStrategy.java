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
package com.iohao.game.action.skeleton.eventbus;

import com.iohao.game.common.kit.concurrent.executor.ExecutorRegion;
import com.iohao.game.common.kit.concurrent.executor.ThreadExecutor;

/**
 * 订阅者线程执行器选择策略
 *
 * @author 渔民小镇
 * @date 2023-12-24
 * @since 21
 */
public interface SubscribeExecutorStrategy {
    /**
     * 得到对应的线程执行器
     *
     * @param subscriber      订阅者
     * @param eventBusMessage 事件消息
     * @param executorRegion  与业务框架所关联的线程执行器管理域
     * @return 线程执行器
     */
    ThreadExecutor select(Subscriber subscriber, EventBusMessage eventBusMessage, ExecutorRegion executorRegion);

    static SubscribeExecutorStrategy defaultInstance() {
        return DefaultSubscribeExecutorStrategy.me();
    }
}

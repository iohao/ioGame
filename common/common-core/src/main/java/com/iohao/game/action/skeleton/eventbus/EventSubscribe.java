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
package com.iohao.game.action.skeleton.eventbus;

import com.iohao.game.action.skeleton.eventbus.capable.EventUserId;
import com.iohao.game.common.kit.concurrent.executor.SimpleThreadExecutorRegion;
import com.iohao.game.common.kit.concurrent.executor.*;

import java.lang.annotation.*;

/**
 * 将方法标记为事件订阅者（接收事件、处理事件）
 * <pre>
 *     订阅者必须且只能有一个参数，用于接收事件源
 * </pre>
 * example
 * <pre>{@code
 *     public class YourEventBusSubscriber implements EventBusSubscriber {
 *         @EventSubscribe
 *         public void userLogin(YourEventMessage message) {
 *             log.info("event - 玩家[{}]登录", message.getUserId());
 *         }
 *     }
 *
 *     @Data
 *     public class YourEventMessage implements Serializable {
 *         final long userId;
 *         public YourEventMessage(long userId) {
 *             this.userId = userId;
 *         }
 *     }
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-24
 * @see DefaultSubscribeExecutorSelector
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventSubscribe {
    /**
     * 执行器策略选择
     * <pre>
     *     注意，只要异步发送事件时，该配置才会生效。
     * </pre>
     */
    ExecutorSelector value() default ExecutorSelector.userVirtualExecutor;

    /**
     * 线程执行器选择策略
     */
    enum ExecutorSelector {
        /**
         * [线程安全] 在用户线程执行器中执行
         * <pre>
         *     通常配合 {@link EventUserId} 使用，事件源最好能实现该接口。
         *
         *     该策略将使用 action 的线程执行器，可确保同一个 userId 在消息事件和 action 时，可避免并发问题。
         *
         *     注意，不要做耗时 io 相关操作，避免阻塞 action 的消费。
         * </pre>
         *
         * @see UserThreadExecutorRegion
         */
        userExecutor,

        /**
         * 在虚拟线程执行器中执行
         * <pre>
         *     耗时相关的操作，可选择此策略
         * </pre>
         *
         * @see UserVirtualExecutorRegion
         */
        userVirtualExecutor,

        /**
         * 预留给开发者的
         * <pre>
         *     默认提供的实现是 [线程安全] 的，这里的线程安全指的是订阅者。
         *     同一个订阅者方法会在相同的线程执行器中执行，从而确保线程安全。
         * </pre>
         *
         * @see SimpleThreadExecutorRegion
         */
        customExecutor
    }
}

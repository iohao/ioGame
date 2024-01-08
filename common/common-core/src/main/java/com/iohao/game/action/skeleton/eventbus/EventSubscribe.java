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
     * 订阅者的执行顺序（优先级）
     * <pre>
     *     值越大，执行优先级越高。
     *
     *     想要确保按顺序执行，订阅者需要使用相同的线程执行器。
     *     比如可以搭配 userExecutor、simpleExecutor 等策略来使用。
     *     这些策略通过 {@link EventBusMessage#getThreadIndex()} 来确定所使用线程执行器。
     * </pre>
     *
     * @return 执行顺序
     * @see ExecutorSelector
     */
    int order() default 0;

    /**
     * 线程执行器选择策略
     */
    enum ExecutorSelector {
        /**
         * [线程安全] 在用户线程执行器中执行
         * <pre>
         *     该策略将使用 action 的线程执行器，可确保同一用户（userId）在消费事件和消费 action 时，
         *     使用的是相同的线程执行器，以避免并发问题。
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
         * [线程安全] 在线程执行器中执行
         * <pre>
         *     该策略将使用 Subscriber.id 来确定线程执行器，可确保相同的订阅者方法在消费事件时，
         *     使用的是相同的线程执行器，以避免并发问题。
         *
         *     注意，不要做耗时 io 相关操作，避免阻塞其他订阅者的消费。
         *
         *     其他补充说明：
         *     Subscriber.id 由框架分配。该策略与 userExecutor、simpleExecutor 策略类似。
         *     userExecutor、simpleExecutor 使用 userId 来确定线程执行器，
         *     而 methodExecutor 则使用订阅者自身的 Subscriber.id 来确定线程执行器（你可以理解为按订阅者方法来划分）。
         * </pre>
         *
         * @see SimpleThreadExecutorRegion
         */
        methodExecutor,
        /**
         * [线程安全] 在线程执行器中执行
         * <pre>
         *     该策略与 userExecutor 类似，但使用的是独立的线程执行器（{@link SimpleThreadExecutorRegion}）。
         *     使用时，需要开发者设置 {@link EventBusMessage#setThreadIndex(long)} 的值（ 该值需要 > 0）。
         * </pre>
         *
         * @see SimpleThreadExecutorRegion
         */
        simpleExecutor,

        /**
         * 预留给开发者的
         */
        customExecutor
    }
}

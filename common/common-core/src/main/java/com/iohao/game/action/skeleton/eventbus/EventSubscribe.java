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

import java.lang.annotation.*;

/**
 * 订阅者注解，将方法标记为事件订阅者（接收事件、处理事件），可配置线程执行器策略与执行优先级，默认是线程安全的。
 * <pre>
 *     订阅者必须且只能有一个参数，用于接收事件源。
 *     默认是线程安全的，使用的用户线程执行器。
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
 * @see DefaultSubscribeExecutorStrategy
 * @since 21
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventSubscribe {
    /**
     * 执行器策略选择
     * <pre>
     *     注意，只有在发送异步事件时，该配置才会生效。
     * </pre>
     */
    ExecutorSelector value() default ExecutorSelector.userExecutor;

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
}

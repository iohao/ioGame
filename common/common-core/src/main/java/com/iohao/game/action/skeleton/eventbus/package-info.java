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
/**
 * <a href="https://www.yuque.com/iohao/game/gmxz33">分布式事件总线相关文档</a>，分布式事件总线与 Guava EventBus、Redis 发布订阅、MQ 等产品类似。
 * <p>
 * 使用场景与简介
 * <pre>
 *     如果使用 Redis、MQ ...等中间件，需要开发者额外的安装这些中间件，并支付所占用机器的费用；使用 Guava EventBus 则只能在当前进程中通信，无法实现跨进程。
 *     而 ioGame 提供的分布式事件总线，拥有上述两者的优点。此外，还可以有效的帮助企业节省云上 Redis、 MQ 这部分的支出。
 *     事件发布后，除了当前进程所有的订阅者能接收到，远程的订阅者也能接收到（支持跨机器、跨进程、跨不同类型的多个逻辑服）。可以代替 redis pub sub 、 MQ ，并且具备全链路调用日志跟踪，这点是中间件产品做不到的。
 * </pre>
 * 特点
 * <pre>
 *     ioGame 分布式事件总线，特点：
 *         1. 使用方式与 Guava EventBus 类似
 *         2. 具备全链路调用日志跟踪。（这点是中间件产品做不到的）
 *         3. 支持跨多个机器、多个进程通信
 *         4. 支持与多种不同类型的多个逻辑服通信
 *         5. 纯 javaSE，不依赖其他服务，耦合性低。（不需要安装任何中间件）
 *         6. 事件源和事件监听器之间通过事件进行通信，从而实现了模块之间的解耦
 *         7. 当没有任何远程订阅者时，将不会触发网络请求。（这点是中间件产品做不到的）
 * </pre>
 * 概念
 * <pre>
 *     ioGame 提供的分布式事件总线在使用上是简单的，只有 3 个概念，分别是：
 *         1. 事件源：事件源由开发者定义。
 *         2. 订阅者：订阅者由开发者定义。
 *         3. 发布事件
 * </pre>
 * 发布事件
 * <pre>
 *     在发布事件时，可控制同步和异步发送。
 *
 *     这里的【同步】指的是：发布事件时，相关订阅者执行完成后，主逻辑才会继续往下走。
 *     这里的【异步】指的是：发布事件时，主逻辑不会阻塞，相关订阅者会在其他线程中执行。
 *
 *     无论是同步或者是异步，相关订阅者在执行逻辑服时，默认是线程安全的；这是因为订阅者 {@link com.iohao.game.action.skeleton.eventbus.EventSubscribe} 默认使用的是用户线程执行器。
 * </pre>
 * 注意事项
 * <pre>
 *     如果你的逻辑服没有任何订阅者，只是发送事件，也是需要配置 EventBusRunner 的，这是因为事件总线是按需要加载的功能。
 *     ioGame 功能特性很多，但不是每个项目都需要这些功能。按需加载有很多好处，比如 email 逻辑服后续的业务不想参与任何订阅了，那么把这个 Runner 注释掉就行了。其他代码不用改，这样也不会占用资源。
 *     所以，需要将 EventBusRunner 添加到业务框架后，分布式事件总线相关功能才会生效。
 * </pre>
 * <p>
 * for example，下面示例展示了分布式事件总线的开启、注册订阅者、定义事件源、发布事件
 * <pre>{@code
 * // 通过业务框架的 addRunner 方法来扩展分布式事件总线相关内容 （Runner 扩展机制），我们将 UserLoginEventMessage、EmailEventBusSubscriber 注册到 EventBus 中。
 * public class MyLogicStartup extends AbstractBrokerClientStartup {
 *     ... ...省略部分代码
 *
 *     @Override
 *     public BarSkeleton createBarSkeleton() {
 *         // 业务框架构建器
 *         var builder = ...
 *
 *         // 开启分布式事件总线。逻辑服添加 EventBusRunner，用于处理 EventBus 相关业务
 *         builder.addRunner(new EventBusRunner() {
 *             @Override
 *             public void registerEventBus(EventBus eventBus, BarSkeleton skeleton) {
 *                 // 注册订阅者
 *                 eventBus.register(new EmailEventBusSubscriber());
 *                 eventBus.register(new UserEventBusSubscriber());
 *             }
 *         });
 *
 *         return builder.build();
 *     }
 * }
 *
 * // 事件源由开发者定义。事件源是业务数据载体等，其主要目的是用于业务数据的传输。
 * @Data
 * public class UserLoginEventMessage implements Serializable {
 *     final long userId;
 *
 *     public UserLoginEventMessage(long userId) {
 *         this.userId = userId;
 *     }
 * }
 *
 * // 订阅者由开发者定义。
 * // 我们在 EmailEventBusSubscriber、UserEventBusSubscriber 类中，分别提供了 UserLoginEventMessage 事件源的订阅者 mail、userLogin。
 * // 当有 UserLoginEventMessage 相关的事件触发，订阅者都能接收到事件。别忘记，当前介绍的是分布式事件总线；所以，即使订阅者在不同的进程中，也能接收到事件。
 * // 另外，值得称赞的是，如果没有任何远程订阅者，将不会触发网络请求。简单的说，事件发布后，当其他进程（其他机器）没有相关订阅者时，只会在内存中传递事件给当前进程的相关订阅者。所以，可以将该通讯方式当作 guava EventBus 来使用。
 * @EventBusSubscriber
 * public class EmailEventBusSubscriber {
 *     @EventSubscribe
 *     public void mail(UserLoginEventMessage message) {
 *         long userId = message.getUserId();
 *         log.info("event - 玩家[{}]登录，发放 email 奖励", userId);
 *     }
 * }
 *
 * @EventBusSubscriber
 * public class UserEventBusSubscriber {
 *     @EventSubscribe
 *     public void userLogin(UserLoginEventMessage message) {
 *         log.info("event - 玩家[{}]登录，记录登录时间", message.getUserId());
 *     }
 * }
 *
 * // 发布事件
 * @ActionController(UserCmd.cmd)
 * public class UserAction {
 *     @ActionMethod(UserCmd.fireEvent)
 *     public void fireEventUser(FlowContext flowContext)  {
 *         long userId = flowContext.getUserId();
 *         // 事件源
 *         var message = new UserLoginEventMessage(userId);
 *         // 发布事件
 *         flowContext.fire(message);
 *         // 事件发布后，会被 UserEventBusSubscriber、EmailEventBusSubscriber 接收。
 *     }
 * }
 *
 * }</pre>
 *
 * @author 渔民小镇
 * @date 2024-06-06
 * @see com.iohao.game.action.skeleton.core.runner.Runner
 * @see com.iohao.game.action.skeleton.eventbus.EventBus
 * @see com.iohao.game.action.skeleton.eventbus.EventBusRunner
 */
package com.iohao.game.action.skeleton.eventbus;
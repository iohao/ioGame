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
 * 业务框架 - <a href="https://www.yuque.com/iohao/game/dpwe6r6sqwwtrh1q">Runner 扩展机制</a>，该机制类似于 Spring CommandLineRunner 的启动项，它能够在逻辑服务器启动之后调用一次 Runner 接口实现类，让开发者能够通过实现 Runner 接口来扩展自身的系统。
 * <p>
 * Runner 机制，会在逻辑服与 Broker（游戏网关）建立连接之前、之后分别触发一次对应的方法。
 * <p>
 * for example
 * <pre>{@code
 * // 路由访问权限以 Runner 机制扩展
 * public class ExternalAccessAuthenticationRunner implements Runner {
 *     @Override
 *     public void onStart(BarSkeleton skeleton) {
 *
 *         var accessAuthenticationHook = ExternalGlobalConfig.accessAuthenticationHook;
 *         // 表示登录才能访问业务方法
 *         accessAuthenticationHook.setVerifyIdentity(true);
 *         // 添加不需要登录（身份验证）也能访问的业务方法 (action)
 *         accessAuthenticationHook.addIgnoreAuthenticationCmd(1, 1);
 *         // 添加不需要登录（身份验证）也能访问的主路由（范围）
 *         accessAuthenticationHook.addIgnoreAuthenticationCmd(2);
 *
 *         // 拒绝主路由为 10 的访问请求
 *         accessAuthenticationHook.addRejectionCmd(10);
 *         // 拒绝主路由为 11、子路由为 1 的访问请求
 *         accessAuthenticationHook.addRejectionCmd(11, 1);
 *     }
 * }
 *
 * // MyRunner
 * public class MyRunner implements Runner {
 *     @Override
 *     public void onStart(BarSkeleton skeleton) {
 *         ... ... 省略部分代码
 *     }
 * }
 *
 * // 游戏对外服
 * public class MyExternalServer extends ExternalBrokerClientStartup {
 *     @Override
 *     public BarSkeleton createBarSkeleton() {
 *         // 游戏对外服不需要业务框架，这里给个空的
 *         BarSkeletonBuilder builder = BarSkeleton.newBuilder();
 *
 *         // 路由访问权限以 Runner 机制扩展
 *         builder.addRunner(new ExternalAccessAuthenticationRunner());
 *         // MyRunner
 *         builder.addRunner(new MyRunner());
 *
 *         return builder.build();
 *     }
 * }
 * }</pre>
 *
 * @author 渔民小镇
 * @date 2024-06-05
 * @see com.iohao.game.action.skeleton.core.runner.Runner
 */
package com.iohao.game.action.skeleton.core.runner;
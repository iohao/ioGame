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
package com.iohao.game.action.skeleton.core.runner;

import com.iohao.game.action.skeleton.core.BarSkeleton;

/**
 * Runner 机制，会在逻辑服与 Broker（游戏网关）建立连接之前（onStart）、之后（onStartAfter）分别触发一次。<a href="https://www.yuque.com/iohao/game/dpwe6r6sqwwtrh1q">相关文档</a>
 * <pre>
 *     1.在逻辑服与 Broker（游戏网关）建立连接之前调用一次，触发 {@link Runner#onStart(BarSkeleton)} 方法。
 *     2.在逻辑服将信息注册到 Broker（游戏网关）后调用一次，触发 {@link Runner#onStartAfter(BarSkeleton)} 方法。
 * </pre>
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
 * // 游戏对外服
 * public class MyExternalServer extends ExternalBrokerClientStartup {
 *     @Override
 *     public BarSkeleton createBarSkeleton() {
 *         // 游戏对外服不需要业务框架，这里给个空的
 *         BarSkeletonBuilder builder = BarSkeleton.newBuilder();
 *
 *         // 路由访问权限以 Runner 机制扩展
 *         builder.addRunner(new ExternalAccessAuthenticationRunner());
 *
 *         return builder.build();
 *     }
 * }
 * }
 * </pre>
 * for example
 * <pre>{@code
 * BarSkeletonBuilder builder = ...
 *
 * builder.addRunner(new Runner() {
 *   @Override
 *   public void onStart(BarSkeleton skeleton) {
 *       log.info("在逻辑服与 Broker（游戏网关）建立连接之前调用一次");
 *   }
 *
 *   @Override
 *   public void onStartAfter(BarSkeleton skeleton) {
 *       log.info("在逻辑服与 Broker（游戏网关）建立连接之后调用一次");
 *   }
 * });
 *
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-04-23
 */
public interface Runner {
    /**
     * 在逻辑服与 Broker（游戏网关）建立连接之前调用一次。
     * <pre>
     *     此时还不能与 Broker（游戏网关）通信。
     * </pre>
     *
     * @param skeleton 业务框架
     */
    void onStart(BarSkeleton skeleton);

    /**
     * 在逻辑服与 Broker（游戏网关）建立连接之后调用一次。
     * <pre>
     *     可以与 Broker（游戏网关）通信了。
     *     如果没有特殊需求的，使用 onStart 方法就可以了。
     * </pre>
     *
     * @param skeleton 业务框架
     */
    default void onStartAfter(BarSkeleton skeleton) {
    }

    /**
     * runner name
     *
     * @return name
     */
    default String name() {
        return this.getClass().getName();
    }
}
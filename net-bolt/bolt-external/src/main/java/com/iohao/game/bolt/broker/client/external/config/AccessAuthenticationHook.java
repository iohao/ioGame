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
package com.iohao.game.bolt.broker.client.external.config;

import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.bolt.broker.client.external.session.UserSession;

/**
 * 访问验证钩子接口
 * <pre>
 *     可以通过这个接口来控制玩家是否可以访问业务方法
 *
 *     文档参考
 *     https://www.yuque.com/iohao/game/tywkqv#qEvtB
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-07-27
 */
public interface AccessAuthenticationHook {

    /**
     * 表示登录才能访问业务方法
     *
     * @param verifyIdentity true 需要登录才能访问业务方法
     * @return me
     */
    AccessAuthenticationHook setVerifyIdentity(boolean verifyIdentity);

    /**
     * 添加需要忽略的路由，这些忽略的路由不需要登录也能访问
     *
     * @param cmd    cmd
     * @param subCmd subCmd
     * @return me
     */
    default AccessAuthenticationHook addIgnoreAuthenticationCmd(int cmd, int subCmd) {
        int cmdMerge = CmdKit.merge(cmd, subCmd);
        return this.addIgnoreAuthenticationCmdMerge(cmdMerge);
    }

    /**
     * 添加需要忽略的路由，这些忽略的路由不需要登录也能访问
     * <pre>
     *     将在下个大版本中移除，由 {@link AccessAuthenticationHook#addIgnoreAuthenticationCmd(int, int)} 代替
     * </pre>
     *
     * @param cmdMerge 路由
     * @return me
     */
    @Deprecated
    AccessAuthenticationHook addIgnoreAuthenticationCmdMerge(int cmdMerge);

    /**
     * 添加需要忽略的主路由，这些忽略的主路由不需要登录也能访问
     *
     * @param cmd 主路由
     * @return me
     */
    default AccessAuthenticationHook addIgnoreAuthenticationCmd(int cmd) {
        return this;
    }

    /**
     * 访问验证
     * <pre>
     *     通过的验证，可以访问游戏逻辑服的业务方法
     * </pre>
     *
     * @param userSession userSession
     * @param cmdMerge    路由
     * @return true 通过访问验证
     */
    boolean pass(UserSession userSession, int cmdMerge);

    /**
     * 添加拒绝访问的主路由，这些主路由不能由外部直接访问
     * <pre>
     *     这里的外部指的是玩家
     * </pre>
     *
     * @param cmd 主路由
     * @return me
     */
    default AccessAuthenticationHook addRejectionCmd(int cmd) {
        return this;
    }

    /**
     * 添加拒绝访问的路由，这些路由不能由外部直接访问
     * <pre>
     *     这里的外部指的是玩家
     * </pre>
     *
     * @param cmd    主路由
     * @param subCmd 子路由
     * @return me
     */
    default AccessAuthenticationHook addRejectionCmd(int cmd, int subCmd) {
        return this;
    }

    /**
     * 拒绝访问的路由
     * <pre>
     *     当为 true 时，玩家不能访问此路由地址
     * </pre>
     *
     * @param cmdMerge 路由
     * @return true 表示玩家不能访问此路由
     */
    default boolean reject(int cmdMerge) {
        return false;
    }
}

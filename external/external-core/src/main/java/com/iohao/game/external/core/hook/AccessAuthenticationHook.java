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
package com.iohao.game.external.core.hook;

import com.iohao.game.external.core.session.UserSession;

/**
 * 路由访问权限的控制
 * <pre>
 *     参考 <a href="https://www.yuque.com/iohao/game/nap5y8p5fevhv99y">路由访问权限的控制-文档</a>
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-02-19
 */
public interface AccessAuthenticationHook {

    /**
     * 表示登录才能访问业务方法
     *
     * @param verifyIdentity true 需要登录才能访问业务方法
     */
    void setVerifyIdentity(boolean verifyIdentity);

    /**
     * 添加需要忽略的路由，这些忽略的路由不需要登录也能访问
     *
     * @param cmd    cmd
     * @param subCmd subCmd
     */
    void addIgnoreAuthCmd(int cmd, int subCmd);

    /**
     * 添加需要忽略的主路由，这些忽略的主路由不需要登录也能访问
     *
     * @param cmd 主路由
     */
    void addIgnoreAuthCmd(int cmd);

    /**
     * 移除需要忽略的路由
     *
     * @param cmd    cmd
     * @param subCmd subCmd
     */
    void removeIgnoreAuthCmd(int cmd, int subCmd);

    /**
     * 移除需要忽略的路由
     *
     * @param cmd cmd
     */
    void removeIgnoreAuthCmd(int cmd);

    /**
     * 访问验证
     * <pre>
     *     通过的验证，可以访问游戏逻辑服的业务方法
     * </pre>
     *
     * @param loginSuccess true 表示玩家登录成功 {@link UserSession#isVerifyIdentity()}
     * @param cmdMerge     路由
     * @return true 通过访问验证
     */
    boolean pass(boolean loginSuccess, int cmdMerge);

    /**
     * 添加拒绝访问的主路由，这些主路由不能由外部直接访问
     * <pre>
     *     这里的外部指的是玩家
     * </pre>
     *
     * @param cmd 主路由
     */
    void addRejectionCmd(int cmd);

    /**
     * 添加拒绝访问的路由，这些路由不能由外部直接访问
     * <pre>
     *     这里的外部指的是玩家
     * </pre>
     *
     * @param cmd    主路由
     * @param subCmd 子路由
     */
    void addRejectionCmd(int cmd, int subCmd);

    /**
     * 移除拒绝访问的路由
     *
     * @param cmd    主路由
     * @param subCmd 子路由
     */
    void removeRejectCmd(int cmd, int subCmd);

    /**
     * 移除拒绝访问的路由
     *
     * @param cmd 主路由
     */
    void removeRejectCmd(int cmd);

    /**
     * 拒绝访问的路由
     * <pre>
     *     当为 true 时，玩家不能访问此路由地址
     * </pre>
     *
     * @param cmdMerge 路由
     * @return true 表示玩家不能访问此路由
     */
    boolean reject(int cmdMerge);

    /**
     * 清除所有的忽略的路由和拒绝路由数据配置
     */
    void clear();
}

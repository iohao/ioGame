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
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashSet;

import java.util.Set;

/**
 * @author 渔民小镇
 * @date 2022-07-27
 */
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class DefaultAccessAuthenticationHook implements AccessAuthenticationHook {

    /** 需要忽略的路由，可以添加到这 set 中 */
    final Set<Integer> cmdMergeSet = new NonBlockingHashSet<>();
    /** 需要忽略的主路由，可以添加到这 set 中 */
    final Set<Integer> cmdSet = new NonBlockingHashSet<>();

    /** 需要拒绝的路由 */
    final Set<Integer> rejectionCmdMergeSet = new NonBlockingHashSet<>();
    /** 需要拒绝的主路由 */
    final Set<Integer> rejectionCmdSet = new NonBlockingHashSet<>();

    /** true 表示请求业务方法需要先登录，默认不需要登录 */
    @Setter
    boolean verifyIdentity;

    @Override
    @Deprecated
    public AccessAuthenticationHook addIgnoreAuthenticationCmdMerge(int cmdMerge) {
        this.cmdMergeSet.add(cmdMerge);
        return this;
    }

    @Override
    public AccessAuthenticationHook addIgnoreAuthenticationCmd(int cmd, int subCmd) {
        int cmdMerge = CmdKit.merge(cmd, subCmd);
        this.cmdMergeSet.add(cmdMerge);
        return this;
    }

    @Override
    public AccessAuthenticationHook addIgnoreAuthenticationCmd(int cmd) {
        this.cmdSet.add(cmd);
        return this;
    }

    @Override
    public boolean pass(UserSession userSession, int cmdMerge) {

        if (!this.verifyIdentity) {
            // 表示不需要登录，就可以访问所有的业务方法（action）
            return true;
        }

        // 已经【登录】的玩家，可以直接访问业务方法
        return userSession.isVerifyIdentity()
                // 在忽略的【路由】范围内的，可以直接访问业务方法（不需要登录）
                || this.cmdMergeSet.contains(cmdMerge)
                // 在忽略的【主路由】范围内的，可以直接访问业务方法（不需要登录）
                || this.cmdSet.contains(CmdKit.getCmd(cmdMerge))
                ;
    }

    @Override
    public AccessAuthenticationHook addRejectionCmd(int cmd) {
        this.rejectionCmdSet.add(cmd);
        return this;
    }

    @Override
    public AccessAuthenticationHook addRejectionCmd(int cmd, int subCmd) {
        int cmdMerge = CmdKit.merge(cmd, subCmd);
        this.rejectionCmdMergeSet.add(cmdMerge);
        return this;
    }

    @Override
    public boolean reject(int cmdMerge) {
        // 在拒绝访问的【路由】范围内的，不能直接访问业务方法
        return this.rejectionCmdMergeSet.contains(cmdMerge)
                // 在拒绝访问的【主路由】范围内的，不能直接访问业务方法
                || this.rejectionCmdSet.contains(CmdKit.getCmd(cmdMerge));
    }
}

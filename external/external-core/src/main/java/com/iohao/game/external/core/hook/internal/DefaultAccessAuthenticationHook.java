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
package com.iohao.game.external.core.hook.internal;

import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.external.core.hook.AccessAuthenticationHook;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashSet;

import java.util.Set;

/**
 * @author 渔民小镇
 * @date 2023-02-19
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultAccessAuthenticationHook implements AccessAuthenticationHook {
    /**
     * 需要忽略的路由，可以添加到这 set 中
     */
    final Set<Integer> cmdMergeSet = new NonBlockingHashSet<>();
    /**
     * 需要忽略的主路由，可以添加到这 set 中
     */
    final Set<Integer> cmdSet = new NonBlockingHashSet<>();

    /**
     * 需要拒绝的路由
     */
    final Set<Integer> rejectionCmdMergeSet = new NonBlockingHashSet<>();
    /**
     * 需要拒绝的主路由
     */
    final Set<Integer> rejectionCmdSet = new NonBlockingHashSet<>();

    /**
     * true 表示请求业务方法需要先登录，默认不需要登录
     */
    @Setter
    boolean verifyIdentity;

    @Override
    public void addIgnoreAuthCmd(int cmd, int subCmd) {
        int cmdMerge = CmdKit.merge(cmd, subCmd);
        this.cmdMergeSet.add(cmdMerge);
    }

    @Override
    public void addIgnoreAuthCmd(int cmd) {
        this.cmdSet.add(cmd);
    }

    @Override
    public void removeIgnoreAuthCmd(int cmd, int subCmd) {
        int cmdMerge = CmdKit.merge(cmd, subCmd);
        this.cmdMergeSet.remove(cmdMerge);
    }

    @Override
    public void removeIgnoreAuthCmd(int cmd) {
        this.cmdSet.remove(cmd);
    }

    @Override
    public boolean pass(boolean loginSuccess, int cmdMerge) {

        if (!this.verifyIdentity) {
            // 表示不需要登录，就可以访问所有的业务方法（action）
            return true;
        }

        // 已经【登录】的玩家，可以直接访问业务方法
        return loginSuccess
                // 在忽略的【路由】范围内的，可以直接访问业务方法（不需要登录）
                || this.cmdMergeSet.contains(cmdMerge)
                // 在忽略的【主路由】范围内的，可以直接访问业务方法（不需要登录）
                || this.cmdSet.contains(CmdKit.getCmd(cmdMerge));
    }

    @Override
    public void addRejectionCmd(int cmd) {
        this.rejectionCmdSet.add(cmd);
    }

    @Override
    public void addRejectionCmd(int cmd, int subCmd) {
        int cmdMerge = CmdKit.merge(cmd, subCmd);
        this.rejectionCmdMergeSet.add(cmdMerge);
    }

    @Override
    public void removeRejectCmd(int cmd, int subCmd) {
        int cmdMerge = CmdKit.merge(cmd, subCmd);
        this.rejectionCmdMergeSet.remove(cmdMerge);
    }

    @Override
    public void removeRejectCmd(int cmd) {
        this.rejectionCmdSet.remove(cmd);
    }

    @Override
    public boolean reject(int cmdMerge) {
        // 在拒绝访问的【路由】范围内的，不能直接访问业务方法
        return this.rejectionCmdMergeSet.contains(cmdMerge)
                // 在拒绝访问的【主路由】范围内的，不能直接访问业务方法
                || this.rejectionCmdSet.contains(CmdKit.getCmd(cmdMerge));
    }

    @Override
    public void clear() {
        this.cmdSet.clear();
        this.cmdMergeSet.clear();
        this.rejectionCmdSet.clear();
        this.rejectionCmdMergeSet.clear();
    }
}

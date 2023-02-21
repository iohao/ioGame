/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.bolt.broker.client.external.config;

import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.bolt.broker.client.external.session.UserSession;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingSetInt;

/**
 * @author 渔民小镇
 * @date 2022-07-27
 */
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class DefaultAccessAuthenticationHook implements AccessAuthenticationHook {

    /** 需要忽略的路由，可以添加到这 set 中 */
    final NonBlockingSetInt cmdMergeSet = new NonBlockingSetInt();
    /** 需要忽略的主路由，可以添加到这 set 中 */
    final NonBlockingSetInt cmdSet = new NonBlockingSetInt();

    /** 需要拒绝的路由 */
    final NonBlockingSetInt rejectionCmdMergeSet = new NonBlockingSetInt();
    /** 需要拒绝的主路由 */
    final NonBlockingSetInt rejectionCmdSet = new NonBlockingSetInt();

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

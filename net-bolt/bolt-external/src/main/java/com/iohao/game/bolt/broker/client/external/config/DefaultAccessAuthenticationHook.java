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

import com.iohao.game.bolt.broker.client.external.session.UserSession;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingSetInt;

import java.util.Set;

/**
 * @author 渔民小镇
 * @date 2022-07-27
 */
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultAccessAuthenticationHook implements AccessAuthenticationHook {

    /** 如果要需要忽略的路由，可以添加到这 set 中 */
    final Set<Integer> ignoreAuthenticationCmdMerge = new NonBlockingSetInt();

    /** true 表示请求业务方法需要先登录，默认不需要登录 */
    boolean verifyIdentity;

    @Override
    public AccessAuthenticationHook addIgnoreAuthenticationCmdMerge(int cmdMerge) {
        this.ignoreAuthenticationCmdMerge.add(cmdMerge);
        return this;
    }

    @Override
    public boolean pass(UserSession userSession, int cmdMerge) {

        if (!this.verifyIdentity) {
            // 表示不需要登录，就可以访问所有的业务方法（action）
            return true;
        }

        // 如果路由在 ignoreSet 中的，那么也可以直接访问业务方法 （不需要登录）
        if (ignoreAuthenticationCmdMerge.contains(cmdMerge)) {
            return true;
        }

        if (userSession.isVerifyIdentity()) {
            // 表示登录成功的
            return true;
        }

        return false;
    }
}

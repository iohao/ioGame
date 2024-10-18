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
package com.iohao.game.bolt.broker.client.kit;

import com.iohao.game.action.skeleton.core.flow.FlowContext;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 变更用户 id
 * <pre>
 *     <a href="https://www.yuque.com/iohao/game/tywkqv">用户连接登录编写 文档</a>
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-01-19
 */
@Slf4j
@UtilityClass
public class UserIdSettingKit {
    /**
     * 设置用户的 userId
     * <pre>
     *     玩家真正的登录，只有设置了用户的 id， 才算是验证通过
     * </pre>
     *
     * @param flowContext 业务框架 flow上下文
     * @param userId      一般从数据库中获取
     * @return true 变更成功
     * @deprecated Please use {@link FlowContext#setUserId(long)} or {@link FlowContext#setUserIdAndGetResult(long)}
     */
    @Deprecated
    public boolean settingUserId(FlowContext flowContext, long userId) {
        var result = flowContext.setUserIdAndGetResult(userId);

        if (!result.success()) {
            Exception exception = result.exception();
            log.error(exception.getMessage(), exception);
        }
        
        return result.success();
    }
}

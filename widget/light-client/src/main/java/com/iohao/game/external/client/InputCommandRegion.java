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
package com.iohao.game.external.client;

import com.iohao.game.external.client.user.ClientUser;

/**
 * 命令域
 *
 * @author 渔民小镇
 * @date 2023-07-09
 */
public interface InputCommandRegion {
    /**
     * 初始化模拟命令
     */
    void initInputCommand();

    /**
     * 设置 clientUser
     * <pre>
     *     会在客户端启动时，自动赋值
     * </pre>
     *
     * @param clientUser clientUser
     */
    void setClientUser(ClientUser clientUser);

    /**
     * 玩家登录成功后的回调
     * <pre>
     *     see {@link ClientUser#callbackInputCommandRegion()}
     * </pre>
     *
     */
    default void loginSuccessCallback() {

    }
}

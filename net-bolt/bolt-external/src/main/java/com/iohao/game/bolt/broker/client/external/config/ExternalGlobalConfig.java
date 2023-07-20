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

import lombok.experimental.UtilityClass;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessage;

/**
 * @author 渔民小镇
 * @date 2022-03-22
 */
@UtilityClass
public class ExternalGlobalConfig {
    /**
     * true 表示请求业务方法需要先登录
     * <pre>
     *     将在下个大版本中移除
     *
     *     请使用 {@link  ExternalGlobalConfig#accessAuthenticationHook} 代替
     * </pre>
     */
    @Deprecated
    public boolean verifyIdentity = true;
    /**
     * 访问验证钩子接口
     * <pre>
     *     使用文档
     *     https://www.yuque.com/iohao/game/tywkqv#qEvtB
     * </pre>
     */
    public AccessAuthenticationHook accessAuthenticationHook = new DefaultAccessAuthenticationHook();
    /**
     * 协议开关，用于一些协议级别的开关控制，比如 安全加密校验等。 : 0 不校验
     * <pre>
     *     see {@link  ExternalMessage#getProtocolSwitch()}
     * </pre>
     */
    public int protocolSwitch;
}

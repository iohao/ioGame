/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.external.core.config;

import com.iohao.game.external.core.hook.AccessAuthenticationHook;
import com.iohao.game.external.core.hook.cache.ExternalCmdCache;
import com.iohao.game.external.core.hook.internal.DefaultAccessAuthenticationHook;
import com.iohao.game.external.core.message.ExternalMessage;
import lombok.experimental.UtilityClass;

/**
 * @author 渔民小镇
 * @date 2023-02-19
 */
@UtilityClass
public class ExternalGlobalConfig {
    /**
     * 访问验证钩子接口
     * <pre>
     *     使用文档
     *     https://www.yuque.com/iohao/game/tywkqv#qEvtB
     * </pre>
     */
    public AccessAuthenticationHook accessAuthenticationHook = new DefaultAccessAuthenticationHook();
    /** 游戏对外服路由缓存 */
    public ExternalCmdCache externalCmdCache;

    /**
     * 协议开关，用于一些协议级别的开关控制，比如 安全加密校验等。 : 0 不校验
     * <pre>
     *     see {@link  ExternalMessage#getProtocolSwitch()}
     * </pre>
     */
    public int protocolSwitch;

    @UtilityClass
    public class CoreOption {
        /** 默认数据包最大 1MB */
        public int packageMaxSize = 1024 * 1024;
        /** http 升级 websocket 协议地址 */
        public String websocketPath = "/websocket";
    }
}

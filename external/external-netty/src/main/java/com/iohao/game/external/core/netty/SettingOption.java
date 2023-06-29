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
package com.iohao.game.external.core.netty;

import com.iohao.game.common.kit.attr.AttrOption;
import com.iohao.game.external.core.netty.handler.SocketCmdAccessAuthHandler;
import com.iohao.game.external.core.netty.handler.SocketIdleHandler;
import com.iohao.game.external.core.netty.handler.SocketRequestBrokerHandler;
import com.iohao.game.external.core.netty.handler.SocketUserSessionHandler;

/**
 * @author 渔民小镇
 * @date 2023-06-29
 */
public interface SettingOption {
    AttrOption<SocketUserSessionHandler> socketUserSessionHandler =
            AttrOption.valueOf("SocketUserSessionHandler");

    AttrOption<SocketCmdAccessAuthHandler> socketCmdAccessAuthHandler =
            AttrOption.valueOf("SocketCmdAccessAuthHandler");

    AttrOption<SocketRequestBrokerHandler> socketRequestBrokerHandler =
            AttrOption.valueOf("SocketRequestBrokerHandler");

    AttrOption<SocketIdleHandler> socketIdleHandler = AttrOption.valueOf("SocketIdleHandler");


}

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
package com.iohao.game.external.core;

import com.iohao.game.external.core.micro.MicroBootstrap;

/**
 * 与真实玩家连接的 ExternalCore 服务器，也是通信框架屏蔽接口
 * <pre>
 *     ExternalCore 帮助开发者屏蔽各通信框架的细节，如 Netty、mina、smart-socket 等通信框。
 *     ioGame 默认提供 Netty 的实现。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-02-17
 */
public interface ExternalCore {
    /**
     * 创建与真实玩家通信的 netty 服务器
     */
    MicroBootstrap createMicroBootstrap();
}

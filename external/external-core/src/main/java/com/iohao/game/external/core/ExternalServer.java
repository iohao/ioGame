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

import com.iohao.game.external.core.broker.client.ExternalBrokerClientStartup;

/**
 * 由 {@link ExternalCore} 和 {@link ExternalBrokerClientStartup} 组成的一个整体
 * <pre>
 *     主要职责是启动 ExternalCore 和 ExternalBrokerClientStartup
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-02-18
 */
public interface ExternalServer {
    /**
     * 启动游戏对外服
     */
    void startup();
}

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
package com.iohao.game.bolt.broker.core.aware;

import com.iohao.game.bolt.broker.core.client.BrokerClientItem;

/**
 * 只要 bolt 处理器（逻辑处理器和连接器）实现了该接口，框架会调用 setBrokerClientItem 方法并赋值
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
public interface BrokerClientItemAware {
    /**
     * 设置 BoltClient
     *
     * @param brokerClientItem BoltClient
     */
    void setBrokerClientItem(BrokerClientItem brokerClientItem);
}

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
package com.iohao.game.bolt.broker.server.balanced.region;

/**
 * BrokerClientRegion 工厂
 * <pre>
 *     生产游戏逻辑服的 BrokerClientRegion 域的工厂
 *
 *     开放这个接口的原因，可以使得开发者可以实现自己的负载算法
 *
 *     框架默认的实现是随机负载 see {@link DefaultBrokerClientRegion}
 *
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-16
 */
public interface BrokerClientRegionFactory {
    /**
     * 创建游戏逻辑服的 BrokerClientRegion 域
     *
     * @param tag 逻辑服 tag
     * @return BrokerClientRegion
     */
    BrokerClientRegion createBrokerClientRegion(String tag);
}

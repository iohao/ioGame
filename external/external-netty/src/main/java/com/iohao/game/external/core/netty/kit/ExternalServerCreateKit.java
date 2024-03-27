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
package com.iohao.game.external.core.netty.kit;

import com.iohao.game.bolt.broker.core.client.BrokerAddress;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.external.core.ExternalServer;
import com.iohao.game.external.core.config.ExternalJoinEnum;
import com.iohao.game.external.core.netty.DefaultExternalServer;
import com.iohao.game.external.core.netty.DefaultExternalServerBuilder;
import lombok.experimental.UtilityClass;

/**
 * 游戏对外服创建工具类，简化游戏对外服的构建
 *
 * @author 渔民小镇
 * @date 2023-05-30
 */
@UtilityClass
public class ExternalServerCreateKit {
    /**
     * 创建游戏对外服
     *
     * @param externalCorePort 游戏对外服端口
     * @param externalJoinEnum 连接方式
     * @return 游戏对外服
     */
    public ExternalServer createExternalServer(int externalCorePort, ExternalJoinEnum externalJoinEnum) {
        return newBuilder(externalCorePort, externalJoinEnum).build();
    }

    /**
     * 创建游戏对外服构建器
     *
     * @param externalCorePort 游戏对外服端口
     * @param externalJoinEnum 连接方式
     * @return 游戏对外服构建器
     */
    public DefaultExternalServerBuilder newBuilder(
            int externalCorePort
            , ExternalJoinEnum externalJoinEnum) {

        return DefaultExternalServer
                // 游戏对外服端口；与真实玩家建立连接的端口
                .newBuilder(externalCorePort)
                // 连接方式
                .externalJoinEnum(externalJoinEnum)
                // 与 Broker （游戏网关）的连接地址 ；默认不填写也是这个值
                .brokerAddress(new BrokerAddress("127.0.0.1", IoGameGlobalConfig.brokerPort));
    }
}

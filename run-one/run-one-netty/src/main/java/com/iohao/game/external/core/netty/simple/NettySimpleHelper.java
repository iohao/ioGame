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
package com.iohao.game.external.core.netty.simple;

import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.external.core.ExternalServer;
import com.iohao.game.external.core.config.ExternalJoinEnum;
import com.iohao.game.external.core.netty.kit.ExternalServerCreateKit;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * @author 渔民小镇
 * @date 2023-02-19
 */
@UtilityClass
public class NettySimpleHelper {

    /**
     * 简单的快速启动
     * <pre>
     *     快速启动:
     *          对外服 websocket 方式连接
     *          网关服 默认端口 10200
     *          逻辑服
     * </pre>
     *
     * @param externalPort 游戏对外服端口
     * @param logicList    逻辑服列表
     */
    public void run(int externalPort, List<AbstractBrokerClientStartup> logicList) {
        run(externalPort, logicList, ExternalJoinEnum.WEBSOCKET);
    }

    /**
     * 简单的快速启动
     * <pre>
     *     快速启动:
     *          对外服 tcp 方式连接
     *          网关服 默认端口 10200
     *          逻辑服
     *
     *      包括游戏业务文档的生成
     *
     *      <a href="https://www.yuque.com/iohao/game/ywe7uc">tcp 连接示例文档</a>
     * </pre>
     *
     * @param externalPort 游戏对外服端口
     * @param logicList    逻辑服列表
     */
    public void runTcp(int externalPort, List<AbstractBrokerClientStartup> logicList) {
        run(externalPort, logicList, ExternalJoinEnum.TCP);
    }

    public void run(int externalPort
            , List<AbstractBrokerClientStartup> logicList
            , ExternalJoinEnum externalJoinEnum) {

        // netty - 游戏对外服
        ExternalServer externalServer = ExternalServerCreateKit.createExternalServer(externalPort, externalJoinEnum);

        // 简单的启动器
        new NettyRunOne()
                // 游戏对外服
                .setExternalServer(externalServer)
                // 游戏逻辑服列表
                .setLogicServerList(logicList)
                // 启动 游戏对外服、游戏网关服、游戏逻辑服
                .startup();
    }
}

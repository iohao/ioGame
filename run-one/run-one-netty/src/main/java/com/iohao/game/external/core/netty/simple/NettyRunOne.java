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
package com.iohao.game.external.core.netty.simple;

import com.iohao.game.action.skeleton.core.ActionCommandRegionGlobalCheckKit;
import com.iohao.game.action.skeleton.toy.IoGameBanner;
import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.common.consts.IoGameLogName;
import com.iohao.game.external.core.ExternalServer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * 简单的启动器： 游戏对外服、游戏网关、游戏逻辑服
 * 谐音：拳皇98 中的 round one ready go!
 * <pre>
 *     注意：
 *          这个工具只适合单机的开发或本地一体化的开发, 对于分步式不适合。
 *
 *          当然如果打算开发单体应用，这种方式是很合适的。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-02-19
 */
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j(topic = IoGameLogName.CommonStdout)
public final class NettyRunOne {
    @Getter(AccessLevel.PRIVATE)
    final InternalRunOne runOne = new InternalRunOne();
    /** broker 游戏网关 */
    BrokerServer brokerServer;
    /** true 在本地启动 broker （游戏网关） */
    boolean runBrokerServer = true;

    /**
     * 简单的快速启动，
     * <pre>
     *     游戏对外服、Broker（游戏网关）、游戏逻辑服这三部分，在一个进程中使用内存通信。
     *     【单体应用；在开发分步式时，调试更加方便】
     * </pre>
     */
    public void startup() {
        this.banner();

        // 启动 Broker（游戏网关）
        if (this.runBrokerServer) {

            if (Objects.isNull(this.brokerServer)) {
                this.brokerServer = BrokerServer.newBuilder().build();
            }

            this.brokerServer.setWithNo(this.runOne.getWithNo());
            this.runOne.execute(this.brokerServer::startup);
        }

        this.runOne.startupLogic();

        // 全局重复路由检测工具
        ActionCommandRegionGlobalCheckKit.checkGlobalExistSubCmd();
    }

    /**
     * set 游戏逻辑服列表
     *
     * @param logicServerList 游戏逻辑服列表
     * @return this
     */
    public NettyRunOne setLogicServerList(List<AbstractBrokerClientStartup> logicServerList) {
        this.runOne.setLogicServerList(logicServerList);
        return this;
    }

    /**
     * 添加游戏对外服
     *
     * @param externalServer 游戏对外服
     * @return this
     */
    public NettyRunOne setExternalServer(ExternalServer externalServer) {
        this.runOne.setExternalServer(externalServer);
        return this;
    }

    /**
     * set 游戏对外服列表
     *
     * @param externalServerList 游戏对外服列表
     * @return this
     */
    public NettyRunOne setExternalServerList(List<ExternalServer> externalServerList) {
        this.runOne.setExternalServerList(externalServerList);
        return this;
    }

    public NettyRunOne setOpenWithNo(boolean openWithNo) {
        this.runOne.setOpenWithNo(openWithNo);
        return this;
    }

    private void banner() {

        int num = 0;

        if (Objects.nonNull(this.runOne.logicServerList)) {
            num += this.runOne.logicServerList.size();
        }

        if (Objects.nonNull(this.runOne.externalServerList)) {
            num += this.runOne.externalServerList.size();
        }

        if (this.runBrokerServer) {
            num++;
        }

        IoGameBanner.me().initCountDownLatch(num);

        IoGameBanner.render();
    }
}

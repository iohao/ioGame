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
package com.iohao.game.external.core.netty.simple;

import com.iohao.game.action.skeleton.core.ActionCommandRegionGlobalCheckKit;
import com.iohao.game.action.skeleton.core.doc.BarSkeletonDoc;
import com.iohao.game.action.skeleton.toy.IoGameBanner;
import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.client.BrokerClientApplication;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.common.kit.ExecutorKit;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import com.iohao.game.external.core.ExternalServer;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

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
public final class NettyRunOne {
    static final Logger log = IoGameLoggerFactory.getLoggerCommonStdout();

    final ExecutorService executorService = ExecutorKit.newCacheThreadPool(NettyRunOne.class.toString());
    /**
     * 游戏对外服列表
     */
    List<ExternalServer> externalServerList;
    /**
     * 游戏逻辑服
     */
    List<AbstractBrokerClientStartup> logicServerList;
    /**
     * broker 游戏网关
     */
    BrokerServer brokerServer;
    /**
     * true 在本地启动 broker （游戏网关）
     */
    boolean runBrokerServer = true;

    /**
     * 简单的快速启动
     * <pre>
     *     快速启动:
     *          对外服
     *          网关服
     *          逻辑服
     * </pre>
     */
    public void startup() {
        this.banner();

        // 启动网关
        if (this.runBrokerServer) {

            if (Objects.isNull(this.brokerServer)) {
                this.brokerServer = BrokerServer.newBuilder().build();
            }

            this.executorService.execute(this.brokerServer::startup);
        }

        this.startupLogic();

        // 全局重复路由检测工具
        ActionCommandRegionGlobalCheckKit.checkGlobalExistSubCmd();
    }

    /**
     * 添加游戏对外服
     *
     * @param externalServer 游戏对外服
     * @return this
     */
    public NettyRunOne setExternalServer(ExternalServer externalServer) {
        if (Objects.isNull(this.externalServerList)) {
            this.externalServerList = new ArrayList<>();
        }

        this.externalServerList.add(externalServer);
        return this;
    }

    protected void startupLogic() {

        if (Objects.nonNull(this.logicServerList)) {
            // 启动游戏逻辑服
            this.executorService.execute(() -> this.logicServerList.forEach(BrokerClientApplication::start));
        }

        if (Objects.nonNull(this.externalServerList)) {
            // 启动游戏对外服
            this.externalServerList.forEach(externalServer -> this.executorService.execute(externalServer::startup));
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        // 生成游戏文档
        this.executorService.execute(BarSkeletonDoc.me()::buildDoc);
    }

    private void banner() {

        int num = 0;

        if (Objects.nonNull(this.logicServerList)) {
            num += this.logicServerList.size();
        }

        if (Objects.nonNull(this.externalServerList)) {
            num += this.externalServerList.size();
        }

        if (this.runBrokerServer) {
            num++;
        }

        IoGameBanner.me().initCountDownLatch(num);

        IoGameBanner.render();

    }
}

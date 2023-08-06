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

import com.iohao.game.action.skeleton.core.doc.BarSkeletonDoc;
import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.client.BrokerClientApplication;
import com.iohao.game.bolt.broker.core.GroupWith;
import com.iohao.game.common.kit.ExecutorKit;
import com.iohao.game.common.kit.HashKit;
import com.iohao.game.external.core.ExternalServer;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 渔民小镇
 * @date 2023-07-06
 */
@Slf4j
@Setter(AccessLevel.PACKAGE)
class InternalRunOne {
    final ExecutorService executorService = ExecutorKit.newCacheThreadPool("InternalRunOne");
    final int withNo = HashKit.hash32(UUID.randomUUID().toString());
    /** 游戏对外服列表 */
    List<ExternalServer> externalServerList;
    /** 逻辑服 */
    List<AbstractBrokerClientStartup> logicServerList;
    boolean openWithNo = true;

    void execute(Runnable command) {
        this.executorService.execute(command);
    }

    void startupLogic() {
        // 启动游戏逻辑服
        if (Objects.nonNull(this.logicServerList)) {

            this.logicServerList.forEach(logicServer -> {
                logicServer.setWithNo(this.getWithNo());
                this.executorService.execute(() -> BrokerClientApplication.start(logicServer));
            });
        }

        // 启动游戏对外服
        if (Objects.nonNull(this.externalServerList)) {
            this.externalServerList.forEach(externalServer -> {
                if (externalServer instanceof GroupWith groupWith) {
                    groupWith.setWithNo(this.getWithNo());
                }

                this.executorService.execute(externalServer::startup);
            });
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        // 生成游戏文档
        executorService.execute(BarSkeletonDoc.me()::buildDoc);
    }

    /**
     * 添加游戏对外服
     *
     * @param externalServer 游戏对外服
     * @return this
     */
    void setExternalServer(ExternalServer externalServer) {
        if (Objects.isNull(this.externalServerList)) {
            this.externalServerList = new ArrayList<>();
        }

        this.externalServerList.add(externalServer);
    }

    int getWithNo() {
        return this.openWithNo ? this.withNo : 0;
    }
}

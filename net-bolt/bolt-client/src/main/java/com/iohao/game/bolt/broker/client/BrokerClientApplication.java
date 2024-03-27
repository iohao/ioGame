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
package com.iohao.game.bolt.broker.client;

import com.alipay.remoting.rpc.RpcConfigs;
import com.iohao.game.action.skeleton.core.ActionCommandRegionGlobalCheckKit;
import com.iohao.game.action.skeleton.core.ActionCommandRegions;
import com.iohao.game.action.skeleton.toy.IoGameBanner;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.client.BrokerClientBuilder;
import lombok.experimental.UtilityClass;

/**
 * BoltBrokerClient 构建与启动
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@UtilityClass
public class BrokerClientApplication {

    /**
     * 构建并启动 BoltBrokerClient
     *
     * @param brokerClientStartup brokerClientStartup
     * @return BoltBrokerClient
     */
    public BrokerClient start(AbstractBrokerClientStartup brokerClientStartup) {

        BrokerClientBuilder brokerClientBuilder = brokerClientStartup.initConfig();

        BrokerClient brokerClient = start(brokerClientBuilder);

        brokerClientStartup.startupSuccess(brokerClient);

        IoGameBanner.render();

        return brokerClient;
    }

    public BrokerClient start(BrokerClientBuilder builder) {
        // #100
        System.setProperty(RpcConfigs.DISPATCH_MSG_LIST_IN_DEFAULT_EXECUTOR, "false");

        BrokerClient brokerClient = builder.build();
        brokerClient.init();

        experiment(builder);

        return brokerClient;
    }

    public BrokerClientBuilder initConfig(AbstractBrokerClientStartup brokerClientStartup) {
        return brokerClientStartup.initConfig();
    }

    /**
     * 实验性功能，将来可能移除的。
     */
    private void experiment(BrokerClientBuilder builder) {
        ActionCommandRegions actionCommandRegions = builder.barSkeleton().getActionCommandRegions();
        String tag = builder.tag();

        /*
         * 全局重复路由校验
         * 原计划是内置到业务框架中，但是突然想起单进程中可以启动多个相同的逻辑服
         * 所以放到这里比较合适，即使有多个游戏逻辑服，可以用 tag 来区分
         *
         * 实际上这个全局重复路由检测是可有可无的，如果遵循 COC ，是可以不需要的。
         *
         */
        ActionCommandRegionGlobalCheckKit.putActionCommandRegions(tag, actionCommandRegions);
    }
}

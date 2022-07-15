/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.bolt.broker.boot.monitor.ext;

import com.iohao.game.action.skeleton.protocol.processor.ExtResponseMessage;
import com.iohao.game.bolt.broker.boot.monitor.MonitorService;
import com.iohao.game.bolt.broker.boot.monitor.message.MonitorCollectMessage;
import com.iohao.game.bolt.broker.boot.monitor.message.ext.MonitorExternalExt;
import com.iohao.game.bolt.broker.boot.monitor.message.ext.MonitorServerInfo;
import com.iohao.game.bolt.broker.boot.monitor.message.oshi.MonitorCollect;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.client.BrokerClientType;
import com.iohao.game.bolt.broker.core.ext.ExtRegion;
import com.iohao.game.bolt.broker.core.ext.ExtRegionContext;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 逻辑服信息收集
 *
 * @author 渔民小镇
 * @date 2022-05-30
 */
@Slf4j
public class MonitorExtRegion implements ExtRegion {
    @Override
    public String getName() {
        // 这个名字要与监控逻辑服的名字对应
        return "MonitorExtServer";
    }

    @Override
    public ExtResponseMessage request(ExtRegionContext extRegionContext) {

        MonitorCollectMessage collectMessage = new MonitorCollectMessage();

        // 监控数据
        MonitorCollect monitorCollect = MonitorService.me().getMonitorCollect(extRegionContext);
        collectMessage.setMonitorCollect(monitorCollect);

        // 服务器数据（目前只表示游戏逻辑服、游戏对外服，暂时没有游戏网关）
        MonitorServerInfo monitorServerInfo = getMonitorServerMessage(extRegionContext);
        collectMessage.setMonitorServerInfo(monitorServerInfo);

        // 对外服数据
        BrokerClient brokerClient = extRegionContext.getBrokerClient();
        if (brokerClient.getBrokerClientType() == BrokerClientType.EXTERNAL) {
            monitorExternalMessage(monitorServerInfo);
        }

        // 响应数据
        ExtResponseMessage responseMessage = new ExtResponseMessage();
        responseMessage.setData(collectMessage);
        return responseMessage;
    }

    private MonitorServerInfo getMonitorServerMessage(ExtRegionContext extRegionContext) {
        BrokerClient brokerClient = extRegionContext.getBrokerClient();

        BrokerClientModuleMessage moduleMessage = brokerClient.getBrokerClientModuleMessage();
        // 当前逻辑服 action 数据，action相关的（action数量、action信息）
        List<Integer> cmdMergeList = moduleMessage.getCmdMergeList();

        return new MonitorServerInfo()
                .setId(moduleMessage.getId())
                .setTag(moduleMessage.getTag())
                .setName(moduleMessage.getName())
                .setBrokerClientType(moduleMessage.getBrokerClientType())
                .setCmdMergeList(cmdMergeList);
    }

    private void monitorExternalMessage(MonitorServerInfo monitorServerInfo) {
        // 当前在线人数
        long online = UserSessions.me().countOnline();

        MonitorExternalExt monitorExternalExt = new MonitorExternalExt();
        monitorExternalExt.setOnline(online);

        // 对外服扩展属性
        monitorServerInfo.setMonitorExternalExt(monitorExternalExt);
    }
}

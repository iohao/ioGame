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
package com.iohao.game.external.core.broker.client.processor.listener;

import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.common.processor.listener.BrokerClientListener;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.core.common.cmd.BrokerClientId;
import com.iohao.game.core.common.cmd.CmdRegions;
import com.iohao.game.external.core.hook.BrokerClientExternalAttr;

/**
 * @author 渔民小镇
 * @date 2023-12-14
 */
public class CmdRegionBrokerClientListener implements BrokerClientListener {

    @Override
    public void onlineLogic(BrokerClientModuleMessage otherModuleMessage, BrokerClient client) {
        CmdRegions cmdRegions = client.option(BrokerClientExternalAttr.cmdRegions);
        // 游戏逻辑服的路由数据
        cmdRegions.loading(otherModuleMessage);
    }

    @Override
    public void offlineLogic(BrokerClientModuleMessage otherModuleMessage, BrokerClient client) {
        CmdRegions cmdRegions = client.option(BrokerClientExternalAttr.cmdRegions);

        String id = otherModuleMessage.getId();
        int idHash = otherModuleMessage.getIdHash();
        BrokerClientId brokerClientId = new BrokerClientId(idHash, id);
        // 游戏逻辑服的路由数据
        cmdRegions.unLoading(brokerClientId);
    }

    public static CmdRegionBrokerClientListener me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final CmdRegionBrokerClientListener ME = new CmdRegionBrokerClientListener();
    }
}

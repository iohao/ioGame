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
package com.iohao.game.bolt.broker.core.common.processor.listener;

import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * 打印其他进程逻辑服的上线与下线信息
 *
 * @author 渔民小镇
 * @date 2024-08-19
 * @since 21.15
 */
@Slf4j
public final class SimplePrintBrokerClientListener implements BrokerClientListener {
    @Override
    public void onlineExternal(BrokerClientModuleMessage otherModuleMessage, BrokerClient client) {
        if (client.getWithNo() == otherModuleMessage.getWithNo()) {
            return;
        }

        // 其他游戏对外服上线监听。已经在线上的，或者有新上线的游戏对外服都会触发此方法。
        log.info("【上线监听】其他进程的游戏对外服信息 {}", otherModuleMessage);
    }

    @Override
    public void offlineExternal(BrokerClientModuleMessage otherModuleMessage, BrokerClient client) {
        if (client.getWithNo() == otherModuleMessage.getWithNo()) {
            return;
        }

        // 其他游戏对外服下线监听
        log.info("【下线监听】其他进程的游戏对外服信息 {}", otherModuleMessage);
    }

    @Override
    public void onlineLogic(BrokerClientModuleMessage otherModuleMessage, BrokerClient client) {
        if (client.getWithNo() == otherModuleMessage.getWithNo()) {
            return;
        }

        // 其他游戏逻辑服在线监听。已经在线上的，或者有新上线的游戏逻辑服都会触发此方法
        log.info("【上线监听】其他进程的游戏逻辑服信息 {}", otherModuleMessage);
    }

    @Override
    public void offlineLogic(BrokerClientModuleMessage otherModuleMessage, BrokerClient client) {
        if (client.getWithNo() == otherModuleMessage.getWithNo()) {
            return;
        }

        // 其他游戏逻辑服下线监听
        log.info("【下线监听】其他进程的游戏逻辑服信息 {}", otherModuleMessage);
    }

    private SimplePrintBrokerClientListener() {
    }

    public static SimplePrintBrokerClientListener me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final SimplePrintBrokerClientListener ME = new SimplePrintBrokerClientListener();
    }
}

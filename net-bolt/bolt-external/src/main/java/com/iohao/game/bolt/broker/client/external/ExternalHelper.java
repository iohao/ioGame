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
package com.iohao.game.bolt.broker.client.external;

import com.iohao.game.action.skeleton.core.commumication.BrokerClientContext;
import com.iohao.game.bolt.broker.core.client.BrokerClient;

/**
 * 对外服 BrokerClient 的引用持有
 *
 * @author 渔民小镇
 * @date 2022-05-18
 */
public class ExternalHelper {
    BrokerClient brokerClient;

    public BrokerClientContext getBrokerClient() {
        return this.brokerClient;
    }

    private ExternalHelper() {
    }

    public static ExternalHelper me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final ExternalHelper ME = new ExternalHelper();
    }
}

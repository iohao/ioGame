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
package com.iohao.game.bolt.broker.client.external.bootstrap.initializer;

import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalChannelInitializerCallback;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalJoinEnum;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.function.Supplier;

/**
 * ExternalChannelInitializerCallback 工厂
 *
 * @author 渔民小镇
 * @date 2022-03-13
 */
public class ExternalChannelInitializerCallbackFactory {
    private final EnumMap<ExternalJoinEnum, Supplier<ExternalChannelInitializerCallback>> map = new EnumMap<>(ExternalJoinEnum.class);

    /**
     * 创建 ExternalChannelInitializerCallback，Channel 初始化的业务编排 (自定义业务编排)
     *
     * @param externalJoinEnum externalJoinEnum
     * @return ExternalChannelInitializerCallback
     */
    public ExternalChannelInitializerCallback createExternalChannelInitializerCallback(ExternalJoinEnum externalJoinEnum) {

        if (!this.map.containsKey(externalJoinEnum)) {
            throw new RuntimeException("externalJoinEnum expected: " + Arrays.toString(ExternalJoinEnum.values()));
        }

        Supplier<ExternalChannelInitializerCallback> supplier = this.map.get(externalJoinEnum);

        // 连接方式
        ExternalChannelInitializerCallback channelInitializerCallback = supplier.get();

        return channelInitializerCallback;
    }

    public void putExternalChannelInitializerCallback(ExternalJoinEnum externalJoinEnum, Supplier<ExternalChannelInitializerCallback> supplier) {
        this.map.put(externalJoinEnum, supplier);
    }

    private ExternalChannelInitializerCallbackFactory() {
        // put tcp socket
        this.putExternalChannelInitializerCallback(ExternalJoinEnum.TCP, ExternalChannelInitializerCallbackTcp::new);
        // put websocket
        this.putExternalChannelInitializerCallback(ExternalJoinEnum.WEBSOCKET, ExternalChannelInitializerCallbackWebsocket::new);
    }

    public static ExternalChannelInitializerCallbackFactory me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final ExternalChannelInitializerCallbackFactory ME = new ExternalChannelInitializerCallbackFactory();
    }
}

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
package com.iohao.game.bolt.broker.core.client;

import com.iohao.game.action.skeleton.core.commumication.*;
import com.iohao.game.common.kit.attr.AttrOptionDynamic;
import com.iohao.game.common.kit.attr.AttrOptions;
import lombok.Getter;

/**
 * 游戏逻辑服 BrokerClient 的引用持有
 * <pre>
 *     会在 {@link BrokerClientBuilder#build()} 时赋值
 *
 *     对于多个 BrokerClient 的引用管理，可以参考 {@link BrokerClients}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-15
 */
public class BrokerClientHelper implements AttrOptionDynamic {
    @Getter
    final AttrOptions options = new AttrOptions();

    BrokerClient brokerClient;

    public BrokerClientContext getBrokerClient() {
        return this.brokerClient;
    }

    public BroadcastContext getBroadcastContext() {
        if (this.brokerClient == null) {
            return null;
        }

        return this.brokerClient.getBroadcastContext();
    }

    public BroadcastOrderContext getBroadcastOrderContext() {
        if (this.brokerClient == null) {
            return null;
        }

        return this.brokerClient.getBroadcastOrderContext();
    }

    public ProcessorContext getProcessorContext() {
        if (this.brokerClient == null) {
            return null;
        }

        return this.brokerClient.getProcessorContext();
    }

    public InvokeModuleContext getInvokeModuleContext() {
        if (this.brokerClient == null) {
            return null;
        }

        return this.brokerClient.getInvokeModuleContext();
    }

    private BrokerClientHelper() {

    }

    public static BrokerClientHelper me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final BrokerClientHelper ME = new BrokerClientHelper();
    }
}

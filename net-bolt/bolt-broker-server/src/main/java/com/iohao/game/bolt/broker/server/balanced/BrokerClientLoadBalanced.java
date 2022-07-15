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
package com.iohao.game.bolt.broker.server.balanced;

import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;

/**
 * 逻辑服管理器
 * <pre>
 *     注意：
 *     对外服中与 broker 建立连接的的 boltBrokerClient ，所以对外服也属于是逻辑服的一种
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
public interface BrokerClientLoadBalanced {
    /**
     * 注册逻辑服
     *
     * @param brokerClientProxy brokerClientProxy
     */
    void register(BrokerClientProxy brokerClientProxy);

    /**
     * 删除逻辑服
     *
     * @param brokerClientProxy brokerClientProxy
     */
    void remove(BrokerClientProxy brokerClientProxy);
}

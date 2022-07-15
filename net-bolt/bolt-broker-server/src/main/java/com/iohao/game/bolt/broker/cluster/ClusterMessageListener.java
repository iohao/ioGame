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
package com.iohao.game.bolt.broker.cluster;

import com.iohao.game.bolt.broker.core.message.BrokerClusterMessage;

/**
 * 集群消息通知
 *
 * @author 渔民小镇
 * @date 2022-05-15
 */
public interface ClusterMessageListener {

    /**
     * 只要有变动，就通知逻辑服
     * 对外服和游戏逻辑服两边都要通知到
     *
     * @param brokerClusterMessage 集群信息
     */
    void inform(BrokerClusterMessage brokerClusterMessage);
}

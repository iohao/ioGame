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

import com.iohao.game.common.kit.ToJson;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * broker （游戏网关）
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class Broker implements ToJson {
    /**
     * 服务器唯一标识
     * <pre>
     *     如果没设置，会随机分配一个
     *
     *     逻辑服的模块id，标记不同的逻辑服模块。
     *     开发者随意定义，只要确保每个逻辑服的模块 id 不相同就可以
     * </pre>
     */
    String id;
    final long startedAt;
    /** broker （游戏网关） ip */
    final String ip;
    /** broker （游戏网关） port */
    int port;
    /** broker （游戏网关）地址  格式 ip:port */
    String brokerAddress;
    /** cluster 格式 ip:port */
    String clusterAddress;


    public Broker(String ip) {
        this.ip = ip;
        startedAt = System.currentTimeMillis();
    }
}

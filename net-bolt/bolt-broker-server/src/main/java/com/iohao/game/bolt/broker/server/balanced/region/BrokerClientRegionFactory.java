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
package com.iohao.game.bolt.broker.server.balanced.region;

/**
 * BrokerClientRegion 工厂
 * <pre>
 *     生产游戏逻辑服的 BrokerClientRegion 域的工厂
 *
 *     开放这个接口的原因，可以使得开发者可以实现自己的负载算法
 *
 *     框架默认的实现是随机负载 see {@link DefaultBrokerClientRegion}
 *
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-16
 */
public interface BrokerClientRegionFactory {
    /**
     * 创建游戏逻辑服的 BrokerClientRegion 域
     *
     * @param tag 逻辑服 tag
     * @return BrokerClientRegion
     */
    BrokerClientRegion createBrokerClientRegion(String tag);
}

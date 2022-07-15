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
package com.iohao.game.bolt.broker.core.ext;

import com.iohao.game.action.skeleton.protocol.processor.ExtResponseMessage;
import com.iohao.game.common.kit.MurmurHash3;
import com.iohao.game.bolt.broker.core.client.BrokerClientBuilder;

/**
 * 实验性功能
 *
 * @author 渔民小镇
 * @date 2022-05-30
 */
public interface ExtRegion {
    /**
     * 一般对应扩展逻辑服的名字
     * <pre>
     *     see {@link BrokerClientBuilder#appName(String)}
     * </pre>
     *
     * @return serverName
     */
    String getName();

    /**
     * 唯一hash
     *
     * @return hash
     */
    default int getExtHash() {
        return MurmurHash3.hash32(getName());
    }

    /**
     * request
     *
     * @param extRegionContext extRegionContext
     * @return ExtResponseMessage
     */
    ExtResponseMessage request(ExtRegionContext extRegionContext);
}

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

import java.io.Serializable;

/**
 * 客户端类型
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
public enum BrokerClientType implements Serializable {
    /** 游戏逻辑服 */
    LOGIC,
    /** 游戏对外服 (真实用户连接的服务器) */
    EXTERNAL
}

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
package com.iohao.game.bolt.broker.core.message;

import java.io.Serial;
import java.io.Serializable;

/**
 * bolt RpcClient.startup 后，需要发送消息才会建立连接
 * <pre>
 *     这里发送一个空消息
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-16
 */
public class BrokerClientItemConnectMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1148652635062833923L;
}

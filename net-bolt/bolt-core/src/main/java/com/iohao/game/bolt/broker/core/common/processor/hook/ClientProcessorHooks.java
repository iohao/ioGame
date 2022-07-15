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
package com.iohao.game.bolt.broker.core.common.processor.hook;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.bolt.broker.core.client.BrokerClientBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * bolt 业务处理器的钩子管理器
 * <pre>
 *     在构建游戏逻辑服赋值
 *
 *     see {@link BrokerClientBuilder#clientProcessorHooks(ClientProcessorHooks)}
 *
 *
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-06-26
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientProcessorHooks {
    /**
     * 逻辑服业务处理钩子接口
     * <pre>
     *     通过业务框架把请求派发给指定的业务类（action）来处理
     * </pre>
     */
    RequestMessageClientProcessorHook requestMessageClientProcessorHook = BarSkeleton::handle;
}

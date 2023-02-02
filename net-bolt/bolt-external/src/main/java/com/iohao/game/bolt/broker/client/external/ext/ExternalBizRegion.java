/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.bolt.broker.client.external.ext;

import com.iohao.game.action.skeleton.core.exception.MsgException;
import com.iohao.game.bolt.broker.client.external.ext.impl.*;
import com.iohao.game.bolt.broker.client.kit.ExternalCommunicationKit;

import java.io.Serializable;

/**
 * 对外服业务扩展
 * <pre>
 *     开发者可以通过实现这个接口，向游戏逻辑服提供一些
 *     1 只存在于游戏对外服中的数据
 *     2 只有游戏对外服可以做的事
 *
 *     框架提供了两个参考实现
 *     {@link  ExistUserExternalBizRegion} 查询用户（玩家）是否在线
 *     {@link  ForcedOfflineExternalBizRegion} 强制用户（玩家）下线
 *
 *     开发者扩展完后，需要添加到 {@link ExternalBizRegions#add(ExternalBizRegion)} 才会生效
 *
 *     使用请参考 {@link  ExternalCommunicationKit}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-07-27
 */
public interface ExternalBizRegion {
    /**
     * 业务码
     * <pre>
     *     开发者扩展时，用正数的业务码
     * </pre>
     *
     * @return 业务码
     */
    int getBizCode();

    /**
     * 业务处理
     * <pre>
     *     返回的数据会存放到 ResponseCollectExternalItemMessage.data 中
     * </pre>
     *
     * @param regionContext 对外服业务处理上下文
     * @return 业务数据，如果有需要传递给请求端的数据，可以在此返回
     * @throws MsgException e
     */
    Serializable request(ExternalBizRegionContext regionContext) throws MsgException;
}

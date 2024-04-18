/*
 * ioGame
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.iohao.game.external.core.broker.client.ext;

import com.iohao.game.action.skeleton.core.exception.MsgException;
import com.iohao.game.bolt.broker.client.kit.ExternalCommunicationKit;
import com.iohao.game.external.core.broker.client.ext.impl.ExistUserExternalBizRegion;
import com.iohao.game.external.core.broker.client.ext.impl.ForcedOfflineExternalBizRegion;

import java.io.Serializable;

import com.iohao.game.core.common.client.ExternalBizCodeCont;

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
 * @date 2023-02-21
 * @see ExternalBizCodeCont
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

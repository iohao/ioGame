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
package com.iohao.game.external.core.hook.cache;

import com.iohao.game.action.skeleton.protocol.BarMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;

/**
 * 游戏对外服缓存数据查询、添加相关接口
 *
 * @author 渔民小镇
 * @date 2023-07-02
 */
public interface ExternalCmdCache {

    /**
     * 查询：从缓存中取数据
     * <pre>
     *     当从缓存中找到数据时，会复用 ExternalMessage 对象（引用不变）。
     *     将缓存数据设置到 ExternalMessage.data 中，避免一次对象的创建。
     * </pre>
     *
     * @param message message
     * @return 返回值为 null，表示缓存中没有数据
     */
    BarMessage getCache(BarMessage message);

    /**
     * 添加：将响应数据添加到缓存中
     *
     * @param responseMessage responseMessage
     */
    void addCacheData(ResponseMessage responseMessage);
}

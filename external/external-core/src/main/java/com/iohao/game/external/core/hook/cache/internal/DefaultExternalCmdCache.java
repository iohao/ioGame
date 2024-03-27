/*
 * ioGame
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.external.core.hook.cache.internal;

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.protocol.BarMessage;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.external.core.hook.cache.CmdCacheOption;
import com.iohao.game.external.core.hook.cache.ExternalCmdCache;
import com.iohao.game.external.core.hook.cache.ExternalCmdCacheSetting;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

/**
 * 框架内置的缓存默认实现类
 *
 * @author 渔民小镇
 * @date 2023-12-15
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class DefaultExternalCmdCache implements ExternalCmdCache, ExternalCmdCacheSetting {

    CmdCacheOption cmdCacheOption = CmdCacheOption.newBuilder().build();

    @Override
    public BarMessage getCache(BarMessage message) {

        HeadMetadata headMetadata = message.getHeadMetadata();

        int cmdMerge = headMetadata.getCmdMerge();
        CmdInfo cmdInfo = CmdInfo.of(cmdMerge);
        CmdCacheRegion cmdCacheRegion = CmdCacheRegions.getCmdCacheRegion(cmdInfo);

        CmdActionCache cmdCache = cmdCacheRegion.getCmdCache(cmdMerge);

        if (Objects.isNull(cmdCache)) {
            // 表示没有对路由做缓存配置
            return null;
        }

        byte[] responseCacheData = cmdCache.getCacheData(message);
        if (Objects.isNull(responseCacheData)) {
            return null;
        }

        // 当响应的缓存数据存在时，将缓存数据设置到 ExternalMessage 中，目的是复用对象，无需 new。
        message.setData(responseCacheData);

        return message;
    }

    @Override
    public void addCmd(int cmd, CmdCacheOption option) {
        CmdCacheRegion cmdCacheRegion = CmdCacheRegions.getCmdCacheRegion(cmd);
        cmdCacheRegion.setRange(true);
        cmdCacheRegion.setCmdCacheOption(option);
    }

    @Override
    public void addCmd(int cmd, int subCmd, CmdCacheOption option) {
        int cmdMerge = CmdKit.merge(cmd, subCmd);
        CmdCacheRegion cmdCacheRegion = CmdCacheRegions.getCmdCacheRegion(cmd);
        cmdCacheRegion.addCmdCache(cmdMerge, option);
    }

    @Override
    public void addCacheData(ResponseMessage responseMessage) {
        HeadMetadata headMetadata = responseMessage.getHeadMetadata();
        CmdInfo cmdInfo = headMetadata.getCmdInfo();

        CmdCacheRegion cmdCacheRegion = CmdCacheRegions.getCmdCacheRegion(cmdInfo);
        int cmdMerge = cmdInfo.getCmdMerge();
        CmdActionCache cmdCache = cmdCacheRegion.getCmdCache(cmdMerge);

        // 没有配置缓存，不做处理
        if (Objects.isNull(cmdCache)) {
            return;
        }

        cmdCache.addCacheData(responseMessage);
    }
}

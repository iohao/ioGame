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
import com.iohao.game.common.kit.MoreKit;
import lombok.experimental.UtilityClass;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.Objects;

/**
 * 缓存域管理
 *
 * @author 渔民小镇
 * @date 2023-12-15
 */
@UtilityClass
final class CmdCacheRegions {
    /**
     * <pre>
     *     key : cmd
     *     value : CmdCacheRegion
     * </pre>
     */
    final Map<Integer, CmdCacheRegion> cmdCacheRegionMap = new NonBlockingHashMap<>();

    CmdCacheRegion getCmdCacheRegion(CmdInfo cmdInfo) {
        int cmd = cmdInfo.getCmd();
        return getCmdCacheRegion(cmd);
    }

    CmdCacheRegion getCmdCacheRegion(int cmd) {

        CmdCacheRegion cmdCacheRegion = cmdCacheRegionMap.get(cmd);

        // 无锁化
        if (Objects.isNull(cmdCacheRegion)) {
            CmdCacheRegion cacheRegion = new CmdCacheRegion();
            return MoreKit.firstNonNull(cmdCacheRegionMap.putIfAbsent(cmd, cacheRegion), cacheRegion);
        }

        return cmdCacheRegion;
    }
}

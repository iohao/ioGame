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

import com.iohao.game.common.kit.concurrent.TaskKit;
import com.iohao.game.external.core.hook.cache.CmdCacheOption;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 缓存域，同一主路由下的。
 *
 * @author 渔民小镇
 * @date 2023-12-15
 */
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
final class CmdCacheRegion {
    /**
     * 路由数据缓存对象
     * <pre>
     *     key : cmdMerge
     *     value : CmdCache
     * </pre>
     */
    final Map<Integer, CmdActionCache> cmdCacheMap = new NonBlockingHashMap<>();

    /** 是否开启范围缓存 */
    boolean range;

    CmdCacheOption cmdCacheOption;

    /**
     * 得到 CmdMergeCache，如果不存在则表示没有做相关的缓存配置
     *
     * @param cmdMerge cmdMerge
     * @return CmdMergeCache
     */
    CmdActionCache getCmdCache(int cmdMerge) {

        CmdActionCache cmdActionCache = this.cmdCacheMap.get(cmdMerge);

        if (Objects.nonNull(cmdActionCache)) {
            return cmdActionCache;
        }

        // 如果开启了范围缓存，即使没有显示的配置，也会生成缓存对象
        if (range) {
            cmdActionCache = addCmdCache(cmdMerge, this.cmdCacheOption);
        }

        return cmdActionCache;
    }

    CmdActionCache addCmdCache(int cmdMerge, CmdCacheOption cmdCacheOption) {

        CmdActionCache cmdActionCache = new CmdActionCache(cmdCacheOption);
        cmdActionCache = this.cmdCacheMap.putIfAbsent(cmdMerge, cmdActionCache);

        if (Objects.isNull(cmdActionCache)) {
            cmdActionCache = this.cmdCacheMap.get(cmdMerge);
            // 缓存过期时间检测
            extractedExpire(cmdActionCache);
        }

        return cmdActionCache;
    }

    private void extractedExpire(CmdActionCache cmdActionCache) {
        TaskKit.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) {
                // 开启过期检测
                cmdActionCache.expireMonitor();

                CmdCacheOption option = cmdActionCache.getCmdCacheOption();
                long delay = option.getExpireCheckTime().getSeconds();

                TaskKit.newTimeout(this, delay, TimeUnit.SECONDS);
            }
        }, 3, TimeUnit.SECONDS);
    }
}

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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.time.Duration;
import java.util.Objects;

/**
 * 游戏对外服缓存配置
 *
 * @author 渔民小镇
 * @date 2023-07-02
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class CmdCacheOption {
    /** 过期时间 */
    final Duration expireTime;
    /** cmdActionCache 内的缓存数量 */
    final int cacheLimit;
    /** 缓存过期检测时间周期 */
    final Duration expireCheckTime;

    private CmdCacheOption(Duration expireTime, int cacheLimit, Duration expireCheckTime) {
        this.expireTime = expireTime;
        this.cacheLimit = cacheLimit;
        this.expireCheckTime = expireCheckTime;
    }

    public static CmdCacheOption.Builder newBuilder() {
        return new Builder();
    }

    @Setter
    @Accessors(chain = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public final static class Builder {
        /** 过期时间 */
        Duration expireTime = Duration.ofHours(1);

        /**
         * 缓存数量（同一个 action 的缓存数量上限）
         * <pre>
         *     因为游戏对外服缓存支持对应条件与缓存数据关联，所以这里有必要做个缓存数据上限，目的是防止客户端恶意制造无效的查询条件
         * </pre>
         */
        int cacheLimit = 256;

        /**
         * 缓存过期检测时间
         * <pre>
         *     间隔多久做一次缓存过期检测
         *
         *     默认是每 5 分钟做一次缓存数据的检测
         *
         *     注意事项：
         *         设置缓存过期检测时间，是为了避免频繁的对缓存做检测，所以缓存的过期时间会有一些误差。
         *         误差范围值 = expireTime (+-) expireCheckTime
         *
         *         如果你想很精准的控制缓存时间，可以设置为每秒做一次检测。
         * </pre>
         */
        Duration expireCheckTime = Duration.ofMinutes(5);

        public CmdCacheOption build() {

            Objects.requireNonNull(expireTime);

            if (cacheLimit <= 0) {
                cacheLimit = 256;
            }

            return new CmdCacheOption(this.expireTime, this.cacheLimit, this.expireCheckTime);
        }
    }
}

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
package com.iohao.game.common.kit.time;

import com.iohao.game.common.kit.concurrent.TaskKit;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 日期与时间的缓存工具，当开启缓存后，可减少时间相关对象的创建，但也会损失一些精度；缓存默认是关闭的，默认情况下使用实时数据。
 * 如果对时间要求不需要很精准的，建议启用。
 * <p>
 * 开启时间与日期缓存优化
 * <pre>{@code
 *     // 开启时间日期相关的优化
 *     CacheTimeKit.enableCache();
 * }
 * </pre>
 * for example
 * <pre>{@code
 *     // 获取 LocalDate，【每分钟】更新一次，可有效减少 LocalDate 对象的创建。
 *     LocalDate localDate = CacheTimeKit.nowLocalDate();
 *
 *     // 获取 LocalDateTime，【每秒】更新一次，可有效减少 LocalDateTime 对象的创建。
 *     LocalDateTime localDateTime = CacheTimeKit.nowLocalDateTime();
 *
 *     // 获取 System.currentTimeMillis()，【每秒】更新一次
 *     long currentTimeMillis = CacheTimeKit.currentTimeMillis();
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-08-27
 * @since 21.16
 */
@UtilityClass
public final class CacheTimeKit {
    private boolean cache;

    private volatile LocalDate localDate;
    private volatile LocalDateTime localDateTime;
    private volatile long currentTimeMillis;

    /**
     * get LocalDate
     *
     * @return LocalDate
     */
    public LocalDate nowLocalDate() {
        return cache ? localDate : LocalDate.now();
    }

    /**
     * get LocalDateTime
     *
     * @return LocalDateTime
     */
    public LocalDateTime nowLocalDateTime() {
        return cache ? localDateTime : LocalDateTime.now();
    }

    /**
     * get currentTimeMillis
     *
     * @return System.currentTimeMillis()
     */
    public long currentTimeMillis() {
        return cache ? currentTimeMillis : System.currentTimeMillis();
    }

    /**
     * 开启优化缓存
     */
    public void enableCache() {
        if (!cache) {
            localDate = LocalDate.now();
            localDateTime = LocalDateTime.now();
            currentTimeMillis = System.currentTimeMillis();

            cache = true;

            TaskKit.runInterval(() -> {
                // 每秒更新一次当前时间
                localDateTime = LocalDateTime.now();
                currentTimeMillis = System.currentTimeMillis();
            }, 1, TimeUnit.SECONDS);

            TaskKit.runInterval(() -> {
                // 每分钟更新一次当前时间
                localDate = LocalDate.now();
            }, 1, TimeUnit.MINUTES);

        }
    }
}

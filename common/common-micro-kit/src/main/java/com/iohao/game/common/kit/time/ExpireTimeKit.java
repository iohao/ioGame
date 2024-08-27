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

import lombok.experimental.UtilityClass;

import java.time.LocalDate;

/**
 * 日期与时间 - 过期检查工具
 *
 * @author 渔民小镇
 * @date 2024-08-27
 * @since 21.16
 */
@UtilityClass
public final class ExpireTimeKit {

    /**
     * LocalDate EpochDay 过期检测，与当前时间做比较
     *
     * @param epochDay LocalDate epochDay
     * @return true 表示日期已经过期
     */
    public boolean expireLocalDate(long epochDay) {
        var localDate = CacheTimeKit.nowLocalDate();
        return localDate.toEpochDay() > epochDay;
    }

    /**
     * LocalDate 过期检测，与当前时间做比较
     *
     * @param localDate localDate
     * @return true 表示日期已经过期
     */
    public boolean expireLocalDate(LocalDate localDate) {
        return expireLocalDate(localDate.toEpochDay());
    }

    /**
     * 过期检测，与当前时间做比较，查看是否过期
     *
     * @param timeMillis 需要检测的时间
     * @return true milliseconds 已经过期
     */
    public boolean expireMillis(long timeMillis) {
        // 时间 - 当前时间
        return (timeMillis - CacheTimeKit.currentTimeMillis()) <= 0;
    }
}

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
package com.iohao.game.action.skeleton.toy;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 渔民小镇
 * @date 2023-01-30
 */
final class InternalMemory {

    Map<String, String> getMemoryMap() {
        Runtime run = Runtime.getRuntime();

        long totalMemory = run.totalMemory();
        long freeMemory = run.freeMemory();
        long used = totalMemory - freeMemory;

        Map<String, String> map = new LinkedHashMap<>(3);
        map.put("used", format(used));
        map.put("freeMemory", format(freeMemory));
        map.put("totalMemory", format(totalMemory));

        return map;
    }

    private String format(long value) {

        var gb = 1024L * 1024L * 1024L;
        if (value >= gb) {
            return (Math.round((double) value / gb * 100.0D) / 100.0D) + "GB";
        }

        var mb = 1024L * 1024L;
        if (value >= mb) {
            return (Math.round((double) value / mb * 100.0D) / 100.0D) + "MB";
        }

        var kb = 1024L;
        if (value >= kb) {
            return (Math.round((double) value / kb * 100.0D) / 100.0D) + "KB";
        }

        return "";
    }
}

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

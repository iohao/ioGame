/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.common.kit;

import java.util.Objects;

/**
 * 获取安全的值, 保证返回的不是 null
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public class SafeKit {
    public static int getInt(Integer value) {
        return getInt(value, 0);
    }

    public static int getInt(Integer value, int defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static long getLong(Long value) {
        return getLong(value, 0);
    }

    public static long getLong(Long value, long defaultValue) {
        return Objects.isNull(value) ? defaultValue : value;
    }

    public static boolean getBoolean(Boolean value) {
        return getBoolean(value, false);
    }

    public static boolean getBoolean(Boolean value, boolean defaultValue) {
        return Objects.isNull(value) ? defaultValue : value;
    }
}

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

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Map;

/**
 * @author 渔民小镇
 * @date 2022-12-23
 */
@UtilityClass
class InternalStringFormat {
    /** 占位符前缀 */
    final String PLACEHOLDER_PREFIX = "{";
    /**
     * 占位符后缀
     */
    final String PLACEHOLDER_SUFFIX = "}";
    final int PLACEHOLDER_PREFIX_LENGTH = PLACEHOLDER_PREFIX.length();
    final int PLACEHOLDER_SUFFIX_LENGTH = PLACEHOLDER_SUFFIX.length();

    String format(String template, Object... params) {

        int start = template.indexOf(PLACEHOLDER_PREFIX);

        if (start == -1) {
            return template;
        }

        var values = Arrays.stream(params)
                .map(String::valueOf)
                .toArray(String[]::new);

        int valueIndex = 0;

        StringBuilder builder = new StringBuilder(template);

        while (start != -1) {
            int end = builder.indexOf(PLACEHOLDER_SUFFIX);

            String replaceContent = values[valueIndex++];

            builder.replace(start, end + PLACEHOLDER_SUFFIX_LENGTH, replaceContent);

            start = builder.indexOf(PLACEHOLDER_PREFIX, start + replaceContent.length());
        }

        return builder.toString();
    }

    String format(String template, final Map<String, Object> valueMap) {

        int start = template.indexOf(PLACEHOLDER_PREFIX);

        if (start == -1) {
            return template;
        }

        StringBuilder builder = new StringBuilder(template);

        while (start != -1) {
            int end = builder.indexOf(PLACEHOLDER_SUFFIX, start);

            String placeholder = builder.substring(start + PLACEHOLDER_PREFIX_LENGTH, end);

            String replaceContent = placeholder.trim().isEmpty() ? "" : String.valueOf(valueMap.get(placeholder));

            builder.replace(start, end + PLACEHOLDER_SUFFIX_LENGTH, replaceContent);

            start = builder.indexOf(PLACEHOLDER_PREFIX, start + replaceContent.length());
        }

        return builder.toString();
    }
}

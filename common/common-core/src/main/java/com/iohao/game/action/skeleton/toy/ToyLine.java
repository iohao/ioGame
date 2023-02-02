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

/**
 * @author 渔民小镇
 * @date 2023-01-30
 */

final class ToyLine {
    String key;
    String value;

    ToyTableRegion region;
    String prefix = " ";
    String suffix = " ";
    String fill = " ";

    String render() {
        StringBuilder bodyBuilder = new StringBuilder();

        // body line key
        bodyBuilder.append(prefix);
        bodyBuilder.append(key);

        int appendNum = region.keyMaxLen - key.length();
        append(bodyBuilder, fill, appendNum);

        // body line key
        String keyValueFix = "|";
        bodyBuilder.append(keyValueFix);
        bodyBuilder.append(prefix);
        bodyBuilder.append(value);

        appendNum = region.valueMaxLen - value.length() - keyValueFix.length();
        append(bodyBuilder, fill, appendNum);

        bodyBuilder.append(suffix);
        return bodyBuilder.toString();
    }

    private void append(StringBuilder builder, String c, int num) {
        builder.append(String.valueOf(c).repeat(Math.max(0, num + 1)));
    }
}

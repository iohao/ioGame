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

import java.util.ArrayList;
import java.util.List;

/**
 * @author 渔民小镇
 * @date 2023-01-30
 */
final class ToyTableRender {
    StringBuilder headUpLine = new StringBuilder();
    StringBuilder headDownLine = new StringBuilder();
    StringBuilder lastLine = new StringBuilder();
    StringBuilder headContent = new StringBuilder();
    List<StringBuilder> bodyContentList = new ArrayList<>();

    ToyTableRender(int bodyMaxNum) {
        for (int i = 0; i < bodyMaxNum; i++) {
            this.bodyContentList.add(new StringBuilder(67));
        }
    }

    void next() {
        String nextSeparator = "##";
        this.headDownLine.append(nextSeparator);
        this.lastLine.append(nextSeparator);

        for (StringBuilder builder : this.bodyContentList) {
            builder.append(nextSeparator);
        }

        this.headContent.append(nextSeparator);
        this.headUpLine.append(nextSeparator);
    }

    void render() {
        StringBuilder builder = new StringBuilder(512);

        // table head
        builder.append("+").append(this.headUpLine).append("\n");
        builder.append("|").append(this.headContent).append("\n");
        builder.append("+").append(this.headDownLine).append("\n");

        // table body
        for (StringBuilder line : this.bodyContentList) {
            builder.append("|").append(line).append("\n");
        }

        // table last
        builder.append("+").append(this.lastLine).append("\n");
        System.out.println(builder);
    }

    void line(int keyMaxLen, int valueMaxLen) {
        tri(keyMaxLen, valueMaxLen, this.headDownLine);
        tri(keyMaxLen, valueMaxLen, this.lastLine);
        tri(keyMaxLen, valueMaxLen, this.headUpLine);
    }

    void tri(int keyMaxLen, int valueMaxLen, StringBuilder builder) {
        String c = "-";

        append(builder, c, keyMaxLen + 1);

        builder.append("+");

        append(builder, c, valueMaxLen + 1);
    }

    private void append(StringBuilder builder, String c, int num) {
        builder.append(String.valueOf(c).repeat(Math.max(0, num + 1)));
    }
}

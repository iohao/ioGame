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

import org.fusesource.jansi.Ansi;

import java.util.*;

/**
 * @author 渔民小镇
 * @date 2023-01-30
 */
final class ToyTableRegion {
    Map<String, ToyLine> lineMap = new LinkedHashMap<>();
    String head;
    String prefix = " ";
    String suffix = " ";
    String fill = " ";
    int valueMaxLen;
    int keyMaxLen;
    String separatorLine;
    ToyTable table;

    void putLine(String key, String value) {
        ToyLine line = this.lineMap.get(key);

        if (Objects.isNull(line)) {
            line = new ToyLine();
            line.key = key;
            line.value = value;
            line.region = this;

            this.lineMap.putIfAbsent(key, line);
            line = this.lineMap.get(key);

            this.counter(line);
        }
    }

    void putAll(Map<?, ?> map) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            this.putLine(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
    }

    void render() {
        var tableRender = table.tableRender;
        // 线条
        tableRender.line(keyMaxLen, valueMaxLen);

        // 表头
        String renderHead = this.renderHead();
        tableRender.headContent.append(renderHead);

        // body
        int num = 0;

        for (ToyLine line : lineMap.values()) {
            var bodyBuilder = tableRender.bodyContentList.get(num);
            num++;

            String lineRender = line.render();
            bodyBuilder.append(lineRender);
        }

        int bodyMaxNum = table.bodyMaxNum;
        for (int i = num; i < bodyMaxNum; i++) {
            // line fill empty
            var bodyBuilder = tableRender.bodyContentList.get(i);

            int appendNum = this.keyMaxLen + this.valueMaxLen
                    + this.prefix.length() + this.suffix.length()
                    + 2;

            append(bodyBuilder, fill, appendNum);
        }
    }

    String renderHead() {

        Ansi.Color color = this.table.color;
        String head = Ansi.ansi().fg(color).a(this.head).reset().toString();

        var headBuilder = new StringBuilder();
        headBuilder.append(this.prefix);
        headBuilder.append(head);

        int num = valueMaxLen + keyMaxLen
                - prefix.length() - suffix.length() - fill.length()
                - 1;

        append(headBuilder, this.fill, num);

        headBuilder.append(this.suffix);

        return headBuilder.toString();
    }

    private void append(StringBuilder builder, String c, int num) {
        builder.append(c.repeat(Math.max(0, num + 1)));
    }

    private void keyLine() {
        this.separatorLine = "-".repeat(Math.max(0, this.keyMaxLen)) + "+";
    }

    private void counter(ToyLine line) {
        if (line.key.length() > this.keyMaxLen) {
            this.keyMaxLen = line.key.length();
            this.keyLine();
        }

        if (line.value.length() > this.valueMaxLen) {
            this.valueMaxLen = line.value.length();
        }

        if (this.head.length() > this.keyMaxLen) {
            this.keyMaxLen = this.head.length();
            this.keyLine();
        }
    }
}

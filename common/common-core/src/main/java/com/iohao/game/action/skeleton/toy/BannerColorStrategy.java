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

import com.iohao.game.common.kit.RandomKit;
import org.fusesource.jansi.Ansi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * @author 渔民小镇
 * @date 2023-01-30
 */
final class BannerColorStrategy {

    static Ansi.Color anyColor() {
        List<Ansi.Color> collect = Stream.of(Ansi.Color.values())
                // 不需要黑色
                .filter(color -> color != Ansi.Color.BLUE)
                .filter(color -> color != Ansi.Color.WHITE)
                .filter(color -> color != Ansi.Color.BLACK)
                .filter(color -> color != Ansi.Color.DEFAULT)
                .toList();

        return RandomKit.randomEle(collect);
    }

    UnaryOperator<String> anyColorFun() {
        // 上色策略
        UnaryOperator<String> colorNon = s -> s;
        UnaryOperator<String> colorSingleFun = this::colorSingle;
        UnaryOperator<String> colorRandomLineFun = this::colorRandomLine;
        UnaryOperator<String> colorRandomFun = this::colorRandom;
        UnaryOperator<String> colorRandomColumnFun = this::colorRandomColumn;
        UnaryOperator<String> colorColumnFun = this::colorColumn;

        List<UnaryOperator<String>> functionList = new ArrayList<>();
        // 单色
        functionList.add(colorNon);
        functionList.add(colorSingleFun);

        // 按列随机上色
        functionList.add(colorRandomColumnFun);
        // 随机列上色
        functionList.add(colorColumnFun);
        functionList.add(colorColumnFun);
        // 随机行上色
        functionList.add(colorRandomLineFun);
        functionList.add(colorRandomLineFun);
        functionList.add(colorRandomLineFun);
        // 随机上色
        functionList.add(colorRandomFun);
        functionList.add(colorRandomFun);
        functionList.add(colorRandomFun);

        // 随机得到一个上色策略
        return RandomKit.randomEle(functionList);
    }

    private String colorRandomLine(String banner) {
        // 按行来上色
        List<Ansi.Color> colorList = listColor();

        Ansi ansi = Ansi.ansi();

        char[] array = banner.toCharArray();

        int anInt = RandomKit.randomInt(colorList.size());
        Ansi.Color color = colorList.get(anInt);

        for (char c : array) {
            ansi.fg(color).a(c);

            if (c == '\n') {
                color = RandomKit.randomEle(colorList);
            }
        }

        return ansi.reset().toString();
    }

    private String colorSingle(String banner) {
        // 上单色
        Ansi.Color color = randomColor();
        Ansi ansi = Ansi.ansi().fg(color).a(banner);
        return ansi.reset().toString();
    }

    private String colorRandom(String banner) {
        // 随机字符上色

        List<Ansi.Color> colorList = listColor();

        Ansi ansi = Ansi.ansi();
        char[] array = banner.toCharArray();

        for (char c : array) {
            Ansi.Color color = RandomKit.randomEle(colorList);
            ansi.fg(color).a(c);
        }

        return ansi.reset().toString();
    }

    private String colorRandomColumn(String banner) {
        // 达到换行的字符数量
        int widthLen = RandomKit.randomInt(1, 10);
        Ansi.Color color = randomColor();

        Ansi ansi = Ansi.ansi();

        char[] array = banner.toCharArray();
        for (int i = 0; i < array.length; i++) {
            char c = array[i];

            if (i % widthLen == 0) {
                // 换色
                color = randomColor();
                widthLen = RandomKit.randomInt(1, 10);
            }

            ansi.fg(color).a(c);
        }

        return ansi.reset().toString();
    }

    private String colorColumn(String banner) {
        Ansi ansi = Ansi.ansi();

        TheColorColumn colorColumn = TheColorColumn.create();
        List<TheColorColumn> list = new ArrayList<>();
        list.add(colorColumn);

        int lineNum = 0;

        for (char c : banner.toCharArray()) {

            if (c == '\n') {
                lineNum = 0;
                colorColumn = list.get(lineNum);
                colorColumn.reset();
            }

            if (!colorColumn.has()) {
                lineNum++;
                // 取下一个数据

                if (lineNum >= list.size()) {
                    // 增加一个颜色数据
                    colorColumn = TheColorColumn.create();
                    list.add(colorColumn);
                }

                colorColumn = list.get(lineNum);
                colorColumn.reset();
            }

            colorColumn.render(ansi, c);


        }

        return ansi.reset().toString();
    }

    private static class TheColorColumn {
        Ansi.Color color;
        int widthLen;
        int num;

        TheColorColumn(Ansi.Color color, int widthLen) {
            this.color = color;
            this.widthLen = widthLen;
            this.num = widthLen;
        }

        void reset() {
            this.num = widthLen;
        }

        static TheColorColumn create() {
            int widthLen = RandomKit.randomInt(1, 5);
            Ansi.Color color = randomColor();
            return new TheColorColumn(color, widthLen);
        }

        void render(Ansi ansi, char c) {
            this.num--;
            ansi.fg(color).a(c);
        }

        boolean has() {
            return this.num > 0;
        }
    }

    private List<Ansi.Color> listColor() {
        return Stream.of(Ansi.Color.values())
                // 不需要黑色
                .filter(color -> color != Ansi.Color.BLACK)
                .toList();
    }

    private static Ansi.Color randomColor() {
        List<Ansi.Color> collect = Stream.of(Ansi.Color.values())
                // 不需要黑色
                .filter(color -> color != Ansi.Color.BLACK)
                .toList();

        return RandomKit.randomEle(collect);
    }
}

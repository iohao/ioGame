/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
package com.iohao.game.common.kit;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数组相关工具
 *
 * @author 渔民小镇
 * @date 2022-01-14
 */
@UtilityClass
public class ArrayKit {

    /**
     * 统计数组数量
     *
     * @return 数量
     */
    public int sum(int[] cards) {
        return Arrays.stream(cards).sum();
    }

    public int[] copy(int[] cards) {
        int length = cards.length;
        int[] copyCards = new int[length];
        System.arraycopy(cards, 0, copyCards, 0, length);
        return copyCards;
    }

    public List<Integer> toList(int[] cards) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < cards.length; i++) {
            int num = cards[i];
            if (num == 0) {
                continue;
            }

            for (int j = 0; j < num; j++) {
                list.add(i);
            }
        }

        return list;
    }

    public void subtract(int[] cards, int[] beCards) {
        for (int i = 0; i < cards.length; i++) {
            cards[i] = cards[i] - beCards[i];
        }
    }

    public void plus(int[] cards, int[] beCards) {
        for (int i = 0; i < cards.length; i++) {
            cards[i] = cards[i] + beCards[i];
        }
    }

    public void plus(int[] cards, List<Integer> beCards) {
        for (Integer value : beCards) {
            cards[value]++;
        }
    }

    public List<Integer> random(int[] cards, int size) {
        List<Integer> list = toList(cards);
        list = list.subList(0, size);
        return list;
    }

    public String join(Object[] array, CharSequence delimiter) {
        return Arrays.stream(array)
                .map(Object::toString)
                .collect(Collectors.joining(delimiter));
    }

    public boolean notEmpty(Object[] array) {
        return array != null && array.length != 0;
    }

    public boolean notEmpty(byte[] array) {
        return !isEmpty(array);
    }

    public boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    public boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public <T> T random(T[] array) {
        int i = RandomKit.randomInt(array.length);
        return array[i];
    }
}

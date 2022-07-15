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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
}

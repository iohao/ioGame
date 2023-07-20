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
package com.iohao.game.common.kit.weight;

import com.iohao.game.common.kit.RandomKit;

import java.util.List;
import java.util.Objects;

/**
 * 权重工具
 *
 * @author 渔民小镇
 * @date 2022-01-02
 * @see Weight
 */
public class WeightKit {
    /**
     * 随机抽取 根据权重
     * <pre>
     *      将列表的权重值相加，随机获取该权重值
     *      在遍历列表，并累积元素的权重值。只要当前元素权重大于这个随机值，就返回元素
     *      如果没有找到大于随机权重值，那么就返回权重最大的元素
     * </pre>
     *
     * @param weights 权重值列表，如果list不为null or empty,那么返回值一定不为null
     * @param <T>     T
     * @return 权重值大的元素的获取几率会比较大
     */
    @SuppressWarnings("unchecked")
    public static <T extends Weight> T roll(List<? extends Weight> weights) {
        if (Objects.isNull(weights) || weights.isEmpty()) {
            return null;
        }

        int weightTotal = 0;
        int maxWeightValue = 0;
        Weight maxWeight = null;

        for (Weight weight : weights) {
            // 获取最大权重值的元素
            if (weight.getWeightVal() > maxWeightValue) {
                maxWeightValue = weight.getWeightVal();
                maxWeight = weight;
            }
            // 权重值累积
            weightTotal += weight.getWeightVal();
        }

        // 随机获取该权重值
        int random = RandomKit.randomInt(weightTotal + 1);
        weightTotal = 0;
        for (Weight weight : weights) {
            // 遍历列表，并累积元素的权重值
            weightTotal += weight.getWeightVal();
            // 只要当前元素权重大于这个随机值，就返回元素
            if (weightTotal > random) {
                return (T) weight;
            }
        }

        // 如果没有找到大于随机权重值，那么就返回权重最大的元素
        return (T) maxWeight;
    }
}

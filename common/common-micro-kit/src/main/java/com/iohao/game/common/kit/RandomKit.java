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
package com.iohao.game.common.kit;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * @author 渔民小镇
 * @date 2022-07-14
 */
@UtilityClass
public class RandomKit {
    final RandomGenerator generator = RandomGenerator.getDefault();

    /**
     * 获得指定范围内的随机数 [0,limit)
     *
     * @param limit 限制随机数的范围，不包括这个数
     * @return 随机数
     */
    public int randomInt(int limit) {
        return generator.nextInt(limit);
    }

    /**
     * 获得指定范围内的随机数
     *
     * @param min 最小数（包含）
     * @param max 最大数（不包含）
     * @return 随机数
     */
    public int randomInt(int min, int max) {
        return generator.nextInt(min, max);
    }

    /**
     * 获得指定范围内的随机数 [0,limit)
     *
     * @param limit 限制随机数的范围，不包括这个数
     * @return 随机数
     * @since 21.23
     */
    public long randomLong(long limit) {
        return generator.nextLong(limit);
    }

    /**
     * 获得指定范围内的随机数
     *
     * @param min 最小数（包含）
     * @param max 最大数（不包含）
     * @return 随机数
     * @since 21.23
     */
    public long randomLong(long min, long max) {
        return generator.nextLong(min, max);
    }

    /**
     * 获得指定范围内的随机数
     *
     * @param start 开始值（包含）
     * @param end   结束值（包含）
     * @return 随机数
     */
    public int random(int start, int end) {
        return start + generator.nextInt(end - start + 1);
    }

    /**
     * 获得指定范围内的随机数 (0 ~ end)
     *
     * @param end 结束值（包含）
     * @return 随机数
     */
    public int random(int end) {
        return generator.nextInt(end + 1);
    }

    /**
     * 获得指定范围内的随机数
     *
     * @param start 开始值（包含）
     * @param end   结束值（包含）
     * @return 随机数
     * @since 21.23
     */
    public long random(long start, long end) {
        return start + generator.nextLong(end - start + 1);
    }

    /**
     * 获得指定范围内的随机数 (0 ~ end)
     *
     * @param end 结束值（包含）
     * @return 随机数
     * @since 21.23
     */
    public long random(long end) {
        return generator.nextLong(end + 1);
    }

    /**
     * 随机一个 bool 值
     *
     * @return bool 值
     */
    public boolean randomBoolean() {
        return generator.nextBoolean();
    }

    public <T> T randomEle(List<T> list) {

        if (CollKit.isEmpty(list)) {
            return null;
        }

        int size = list.size();

        return size == 1
                ? list.getFirst()
                : list.get(randomInt(size));
    }

    public <T> T randomEle(T[] array) {
        Objects.requireNonNull(array);

        return array.length == 1
                ? array[0]
                : array[randomInt(array.length)];
    }

    public double nextDouble() {
        return generator.nextDouble();
    }
}

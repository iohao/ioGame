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

/**
 * Runtime 相关工具
 *
 * @author 渔民小镇
 * @date 2024-05-01
 * @since 21.7
 */
@UtilityClass
public class RuntimeKit {
    /**
     * 默认使用 Runtime.getRuntime().availableProcessors()。
     * 如果有一些特殊环境需要模拟的，可以设置该变量。
     */
    public int availableProcessors = Runtime.getRuntime().availableProcessors();

    /**
     * 数量是不大于 Runtime.getRuntime().availableProcessors() 的 2 次幂。
     * 当 availableProcessors 的值分别为 4、8、12、16、32 时，对应的数量则是 4、8、8、16、32。
     * <p>
     * for example
     * <table>
     *     <thead>
     *         <th scope="col">availableProcessors 值</th>
     *         <th scope="col">实际值</th>
     *     </thead>
     *     <tbody>
     *         <tr><td>4</td><td>4</td></tr>
     *         <tr><td>8</td><td>8</td></tr>
     *         <tr><td>12</td><th scope="row">8</th></tr>
     *         <tr><td>16</td><td>16</td></tr>
     *         <tr><td>32</td><td>32</td></tr>
     *     </tbody>
     * </table>
     * <p>
     * 另外，如果有一些特殊环境需要模拟的，可以设置该变量。
     */
    public int availableProcessors2n = availableProcessors2n();

    static int availableProcessors2n() {
        int n = RuntimeKit.availableProcessors;
        n |= (n >> 1);
        n |= (n >> 2);
        n |= (n >> 4);
        n |= (n >> 8);
        n |= (n >> 16);
        return (n + 1) >> 1;
    }
}

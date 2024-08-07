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
/**
 * 业务框架 - 内部协议 - <a href="https://www.yuque.com/iohao/game/ieimzn">装箱、拆箱包装；解决协议碎片</a>
 * 主要解决两个问题：碎片协议、使用时可自动装箱和拆箱。
 * <p>
 * for example
 * <pre>{@code
 * // 可以方便的接收与返回基础类型的数据
 * @ActionController(6)
 * public class IntAction {
 *     @ActionMethod(10)
 *     public int int2int(int value) {
 *         return value + 1;
 *     }
 *
 *     @ActionMethod(12)
 *     public List<Integer> intList2intList(List<Integer> intList) {
 *         List<Integer> list = new ArrayList<>();
 *         list.add(1);
 *         list.add(2);
 *         return list;
 *     }
 *
 *     @ActionMethod(30)
 *     public String string2string(String s) {
 *         return s + 1;
 *     }
 *
 *     @ActionMethod(32)
 *     public List<String> stringList2stringList(List<String> stringList) {
 *         List<String> list = new ArrayList<>();
 *         list.add(11L + "");
 *         list.add(22L + "");
 *         return list;
 *     }
 * }
 * }</pre>
 *
 * @author 渔民小镇
 * @date 2023-06-09
 */
package com.iohao.game.action.skeleton.protocol.wrapper;
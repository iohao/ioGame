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
 * 实验性模块：脉冲信号。
 * 注意，这是一个实验性模块，将来有可能会删除，开发者先不要使用。
 * <pre>
 *     脉冲信号模块：
 *     应用场景与发布订阅相似，但是发布评阅是一种无需反馈的方式，就是发布者发布消息后就不管之后的事情了。
 *
 *     而脉冲则比发布订阅多了接收反馈的动作，什么意思呢？
 *     脉冲生产者发送【脉冲信号请求】后，由脉冲消费者接收该请求。
 *     脉冲消费者接收请求方法执行后，会给脉冲生产者发送【脉冲信号响应】。而这一步也正是区别于发布订阅的关键所在。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-04-20
 */
package com.iohao.game.action.skeleton.pulse;
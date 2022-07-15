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
/**
 * 房间内的玩法操作业务
 * <p>
 * 玩法操作业务 - 设计模式: 策略模式 + 享元模式
 * <pre>
 *     策略模式:
 *         定义一个接口，在写两个实现类并实现这个接口，这样就可以使用一个接口，在需要的时候，在根据情况使用哪一个实现类
 *     享元模式:
 *         维护 玩法接口的实现类实例 {@link com.iohao.game.widget.light.room.operation.OperationHandler}
 *
 *         将许多"虚拟"对象的状态集中管理, 减少运行时对象实例个数，节省内存
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-03-31
 */
package com.iohao.game.widget.light.room.operation;
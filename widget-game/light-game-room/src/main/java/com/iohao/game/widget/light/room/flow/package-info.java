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
 * 创建房间时, 子游戏可以自定义流程创建逻辑 包括:
 * <pre>
 *     创建房间
 *     创建玩家
 *     进入房间
 *     规则解析
 *     游戏开始
 *
 *     只提供抽象骨架, 具体的逻辑实现由子游戏自定义
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-03-31
 */
package com.iohao.game.widget.light.room.flow;
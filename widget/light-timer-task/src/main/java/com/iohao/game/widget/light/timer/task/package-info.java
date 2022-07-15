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
 * 解决问题
 * <pre>
 *     一个任务定时器， 达到指定时间则执行业务。
 *     该定时器也支持被移除（在满足某个条件后）
 * </pre>
 * <p>
 * 功能特点
 * <pre>
 *     将来某个时间执行该任务
 *     暂停任务
 *     取消（删除）任务
 *     显示的取消任务
 * </pre>
 * <p>
 * 业务举例
 * <pre>
 *     在游戏开始前，房间创建后需要在1分钟后自动解散房间（将来某个时间执行该任务）。
 *     在房间解散前，每当有一个玩家加入房间，则解散房间延迟（暂停任务）30秒
 *     当房间人数大于5人时，则取消房间自动解散规则（显示的取消任务）
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
package com.iohao.game.widget.light.timer.task;
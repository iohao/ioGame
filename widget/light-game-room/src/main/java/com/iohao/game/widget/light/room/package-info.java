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
 * 扩展模块 - <a href="https://www.yuque.com/iohao/game/vtzbih">桌游类、房间类游戏</a>，light-game-room + 领域事件 + 内置 Kit  = 轻松搞定桌游类游戏
 * <p>
 * 介绍
 * <pre>
 * 该模块是桌游类、房间类游戏的解决方案。比较适合桌游类、房间类的游戏基础搭建，基于该模型可以做一些如，炉石传说、三国杀、斗地主、麻将 ...等类似的桌游。
 * 或者说只要是房间类的游戏，该模型都适用。比如，CS、泡泡堂、飞行棋、坦克大战 ...等。
 *
 * 如果你计划做一些桌游类的游戏，那么推荐你基于该模块做扩展。该模块遵循面向对象的设计原则，没有强耦合，可扩展性强。
 * 该模块帮助开发者屏蔽了很多重复性的工作，并可为项目中的功能模块结构、开发流程等进行清晰的组织定义，减少了后续的项目维护成本。
 * </pre>
 * <p>
 * 主要解决的问题与职责
 * <pre>
 * 桌游、房间类的游戏在功能职责上可以分为 3 大类，分别是
 * 1. 房间管理相关的
 *   a. 管理着所有的房间、查询房间列表、房间的添加、房间的删除、房间与玩家之间的关联、房间查找（通过 roomId 查找、通过 userId 查找）。
 * 2. 开始游戏流程相关的
 *   a. 通常桌游、房间类的游戏都有一些固定的流程，如创建房间、玩家进入房间、玩家退出房间、解散房间、玩家准备、开始游戏 ...等。
 *   b. 开始游戏时，需要做开始前的验证，如房间内的玩家是否符足够 ...等，当一切符合业务时，才是真正的开始游戏。
 * 3. 玩法操作相关的
 *   a. 游戏开始后，由于不同游戏之间的具体操作是不相同的。如坦克的射击，炉石的战前选牌、出牌，麻将的吃、碰、杠、过、胡，回合制游戏的普攻、防御、技能 ...等。
 *   b. 由于玩法操作的不同，所以我们的玩法操作需要是可扩展的，并用于处理具体的玩法操作。同时这种扩展方式更符合单一职责，使得我们后续的扩展与维护成本更低。
 *
 * 以上功能职责（房间管理相关、流程相关、玩法操作相关）属于相对通用的功能。如果每款游戏都重复的做这些工作，除了枯燥之外，还将浪费巨大的人力成本。
 *
 * 而当前模块则能很好的帮助开发者屏蔽这些重复性的工作，并可为项目中的功能模块结构、开发流程等进行清晰的组织定义，减少了后续的项目维护成本。
 * 更重要的是有相关文档，将来当你的团队有新进成员时，可以快速的上手。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-05-14
 * @since 21.8
 */
package com.iohao.game.widget.light.room;
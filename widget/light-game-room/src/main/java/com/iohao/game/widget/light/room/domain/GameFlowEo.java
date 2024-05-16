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
package com.iohao.game.widget.light.room.domain;

import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.widget.light.domain.event.message.Eo;

/**
 * GameFlowEo，可规避 GameFlowService 中的并发问题
 * <pre>{@code
 * // 发送领域事件
 * GameFlowContext context = GameFlowContext.of(room, flowContext);
 * new GameFlowEo(flowContext, () -> {
 *     // 进入房间
 *     this.roomService.enterRoom(context);
 * }).send();
 *
 * GameFlowContext gameFlowContext = GameFlowContext.of(room, flowContext);
 * new GameFlowEo(flowContext, () -> {
 *     // 退出房间
 *     this.roomService.quitRoom(gameFlowContext);
 * }).send();
 *
 * GameFlowContext context = GameFlowContext.of(room, flowContext);
 * new GameFlowEo(flowContext, () -> {
 *     // 开始游戏
 *     this.roomService.startGame(context);
 * }).send();
 *
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-05-12
 * @see GameFlowEventHandler
 * @since 21.8
 */
public record GameFlowEo(FlowContext flowContext, Runnable runnable) implements Eo {
    public void execute() {
        runnable.run();
    }
}

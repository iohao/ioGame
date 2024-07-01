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

import com.iohao.game.action.skeleton.core.exception.MsgExceptionKit;
import com.iohao.game.widget.light.domain.event.message.DomainEventHandler;
import com.iohao.game.widget.light.room.flow.GameFlowService;

/**
 * EventHandler，消费 GameFlowEo
 * <p>
 * 将 GameFlowEventHandler 添加到 DomainEventContext 中
 * <pre>{@code
 *         // 领域事件上下文参数
 *         DomainEventContextParam contextParam = new DomainEventContextParam();
 *         // 游戏流程相关
 *         contextParam.addEventHandler(new GameFlowEventHandler());
 *
 *         // 启动事件驱动
 *         DomainEventContext domainEventContext = new DomainEventContext(contextParam);
 *         domainEventContext.startup();
 * }
 *
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-05-12
 * @see GameFlowService
 * @see GameFlowEo
 * @since 21.8
 */
public final class GameFlowEventHandler implements DomainEventHandler<GameFlowEo> {
    @Override
    public void onEvent(GameFlowEo event, boolean endOfBatch) {
        try {
            event.execute();
        } catch (Throwable e) {
            MsgExceptionKit.onException(e, event.flowContext());
        }
    }
}

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
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.widget.light.domain.event.message.DomainEventHandler;
import com.iohao.game.widget.light.room.operation.OperationContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 玩法操作上下文领域事件，用于规避并发
 * <pre>{@code
 * // 创建玩法操作上下文
 * OperationContext operationContext = OperationContext.of(room, operationHandler)
 *     // 当前操作的玩家
 *     .setFlowContext(flowContext)
 *     // 开发者根据游戏业务定制的操作数据
 *     .setCommand(command);
 *
 * // 领域事件相关，https://www.yuque.com/iohao/game/gmfy1k
 * DomainEventPublish.send(operationContext);
 * }
 * </pre>
 * <p>
 * 将 OperationContextEventHandler 添加到 DomainEventContext 中
 * <pre>{@code
 *         // 领域事件上下文参数
 *         DomainEventContextParam contextParam = new DomainEventContextParam();
 *         // 配置领域事件 - 玩法操作相关
 *         contextParam.addEventHandler(new OperationContextEventHandler());
 *
 *         // 启动事件驱动
 *         DomainEventContext domainEventContext = new DomainEventContext(contextParam);
 *         domainEventContext.startup();
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-05-12
 * @since 21.8
 */
@Slf4j
public final class OperationContextEventHandler implements DomainEventHandler<OperationContext> {
    @Override
    public void onEvent(OperationContext operationContext, boolean endOfBatch) {
        try {
            // 玩法操作业务类，将验证与操作分离
            operationContext.execute();
        } catch (Throwable e) {
            FlowContext flowContext = operationContext.getFlowContext();
            if (flowContext == null) {
                log.error(e.getMessage(), e);
                return;
            }

            // 将异常发送给当前用户
            MsgExceptionKit.onException(e, flowContext);
        }
    }
}

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

/**
 * @author 渔民小镇
 * @date 2024-05-12
 * @since 21.8
 */
public class OperationContextEventHandler implements DomainEventHandler<OperationContext> {
    @Override
    public void onEvent(OperationContext operationContext, boolean endOfBatch) {
        try {
            // 玩法操作业务类，将验证与操作分离
            operationContext.execute();
        } catch (Throwable e) {
            FlowContext flowContext = operationContext.getFlowContext();
            MsgExceptionKit.onException(e, flowContext);
        }
    }
}

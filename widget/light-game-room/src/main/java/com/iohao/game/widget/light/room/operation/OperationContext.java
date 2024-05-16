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
package com.iohao.game.widget.light.room.operation;

import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.widget.light.room.Room;
import com.iohao.game.widget.light.room.domain.OperationContextEventHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 玩家玩法操作上下文
 * <pre>{@code
 * // 创建玩法操作上下文
 * OperationContext operationContext = OperationContext.of(room, operationHandler)
 *     // 当前操作的玩家
 *     .setFlowContext(flowContext)
 *     // 开发者根据游戏业务定制的操作数据
 *     .setCommand(command);
 * // 执行玩法操作
 * operationContext.execute();
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @see OperationContextEventHandler
 * @since 21.8
 */
@Getter
@Setter
@Accessors(chain = true)
public class OperationContext implements PlayerOperationContext {
    /** room */
    final Room room;
    /** 玩法操作业务类 */
    final OperationHandler operationHandler;
    /** 具体玩法需要操作的数据，通常由开发者根据游戏业务来定制 */
    Object command;
    /** 当前操作玩家的 FlowContext */
    FlowContext flowContext;

    OperationContext(Room room, OperationHandler operationHandler) {
        this.room = room;
        this.operationHandler = operationHandler;
    }

    /**
     * 执行玩家的玩法操作，包括验证与处理。
     */
    public void execute() {
        // 玩法操作业务类，将验证与操作分离
        this.operationHandler.verify(this);
        this.operationHandler.process(this);
    }

    /**
     * 创建 OperationContext 对象
     *
     * @param room             房间
     * @param operationHandler 玩法操作上下文
     * @return OperationContext
     */
    public static OperationContext of(Room room, OperationHandler operationHandler) {
        return new OperationContext(room, operationHandler);
    }
}

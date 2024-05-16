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

import java.util.Optional;

/**
 * 玩法操作工厂
 *
 * @author 渔民小镇
 * @date 2024-05-12
 * @since 21.8
 */
public interface OperationFactory {
    /**
     * 获取 OperationHandler（玩法操作业务类）
     *
     * @param operation 操作码
     * @return 操作码对应的业务逻辑处理类
     */
    OperationHandler getOperationHandler(int operation);

    /**
     * 获取玩家可操作的 OperationHandler（玩法操作业务类）
     *
     * @param operation 操作码
     * @return 玩法操作业务类
     */
    OperationHandler getUserOperationHandler(int operation);

    /**
     * 将操作码与 OperationHandler（玩法操作业务类）关联
     *
     * @param operation        操作码
     * @param operationHandler 玩法操作业务类
     */
    void mapping(int operation, OperationHandler operationHandler);

    /**
     * 玩家可操作的 OperationHandler。将操作码与 OperationHandler（玩法操作业务类）关联
     *
     * @param operation        操作码
     * @param operationHandler 玩法操作业务类
     */
    void mappingUser(int operation, OperationHandler operationHandler);

    /**
     * 通过操作码得到 OperationHandler Optional
     *
     * @param operation 操作码
     * @return Optional OperationHandler
     */
    Optional<OperationHandler> optionalOperationHandler(int operation);

    /**
     * 创建 OperationFactory 对象（框架提供的内置实现）
     *
     * @return OperationFactory 对象
     */
    static OperationFactory of() {
        return new SimpleOperationFactory();
    }
}
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

/**
 * 玩法操作相关服务。获取 user 的玩法操作、所有玩法操作、玩法操作工厂。
 *
 * @author 渔民小镇
 * @date 2024-05-12
 * @since 21.8
 */
public interface OperationService {
    /**
     * @return 玩法操作工厂
     */
    OperationFactory getOperationFactory();

    /**
     * 获取 OperationHandler（玩法操作业务类）
     *
     * @param operation 操作码
     * @return 操作码对应的业务逻辑处理类
     */
    default OperationHandler getOperationHandler(int operation) {
        return this.getOperationFactory().getOperationHandler(operation);
    }

    /**
     * 获取玩家可操作的 OperationHandler（玩法操作业务类）
     *
     * @param operation 操作码
     * @return 玩法操作业务类
     */
    default OperationHandler getUserOperationHandler(int operation) {
        return this.getOperationFactory().getUserOperationHandler(operation);
    }

}
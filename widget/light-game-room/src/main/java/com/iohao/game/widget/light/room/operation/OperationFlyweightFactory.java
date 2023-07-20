/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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

import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.core.exception.MsgException;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;

/**
 * 玩法操作的处理对象, 享元工厂
 *
 * @author 渔民小镇
 * @date 2022-03-31
 */
@Slf4j
@UtilityClass
public class OperationFlyweightFactory {
    /**
     * 操作处理
     * <pre>
     *     key : 操作码
     *     value : 操作码对应的业务逻辑处理类
     * </pre>
     */
    @Getter
    private final Map<Integer, OperationHandler> map = new NonBlockingHashMap<>();

    /**
     * 获取操作
     *
     * @param operation 操作码
     * @return 操作码对应的业务逻辑处理类
     * @throws MsgException e
     */
    OperationHandler getOperationHandler(int operation) throws MsgException {
        OperationHandler operationHandler = map.get(operation);

        ActionErrorEnum.classNotExist.assertNonNull(operationHandler);

        return operationHandler;
    }

    public void mapping(int operation, OperationHandler operationHandler) {
        map.put(operation, operationHandler);
    }
}

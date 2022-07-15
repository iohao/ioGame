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

    /**
     * 映射
     *
     * @param operation 操作类型
     * @param clazz     操作处理类
     */
    public void mapping(int operation, Class<? extends OperationHandler> clazz) {
        try {
            // TODO: 2022/1/14 这里是否要 spring ?
//            OperationHandler operationHandler = SpringHolder.getBean(clazz);
//            map.put(operation, operationHandler);
        } catch (Exception e) {
            log.error("操作处理注册错误: {}", clazz);
        }
    }

    public void mapping(int operation, OperationHandler operationHandler) {
        map.put(operation, operationHandler);
    }
}

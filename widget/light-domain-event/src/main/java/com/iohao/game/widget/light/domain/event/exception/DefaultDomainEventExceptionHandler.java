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
package com.iohao.game.widget.light.domain.event.exception;

import com.lmax.disruptor.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认领域事件 异常处理类
 *
 * @author 渔民小镇
 * @date 2022-01-14
 */
@Slf4j
public class DefaultDomainEventExceptionHandler implements ExceptionHandler {
    @Override
    public void handleEventException(Throwable ex, long sequence, Object event) {
        log.error("{} - {}", ex, event);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        log.error(ex.getMessage(), ex);
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        log.error(ex.getMessage(), ex);
    }
}

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
public class DefaultDomainEventExceptionHandler implements ExceptionHandler<Object> {
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

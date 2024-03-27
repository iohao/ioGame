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
package com.iohao.game.bolt.broker.core.aware;

import java.util.concurrent.Executor;

/**
 * UserProcessorExecutorAware
 * <pre>
 *
 *     只要 UserProcessor 实现了该接口，框架会调用 setProcessorExecutor 方法并赋值
 *
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-11-10
 */
public interface UserProcessorExecutorAware extends UserProcessorInNettyThreadAware {
    /**
     * set UserProcessor Executor
     *
     * @param executor Executor
     */
    void setUserProcessorExecutor(Executor executor);

    /**
     * get UserProcessor Executor
     *
     * @return Executor
     */
    Executor getUserProcessorExecutor();
}

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

import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * @author 渔民小镇
 * @date 2024-08-10
 * @since 21.15
 */
@UtilityClass
public class AwareKit {
    public void aware(Object obj) {
        // 处理 UserProcessor 所有请求
        if (obj instanceof UserProcessorExecutorAware aware && Objects.isNull(aware.getUserProcessorExecutor())) {
            // 如果开发者没有自定义 Executor，则使用框架提供的 Executor 策略
            Executor executor = IoGameGlobalConfig.getExecutor(aware);
            aware.setUserProcessorExecutor(executor);
        }

        // 处理 UserProcessor 业务数据的请求
        if (obj instanceof UserProcessorExecutorSelectorAware aware) {
            var executorSelector = IoGameGlobalConfig.getExecutorSelector();
            aware.setUserProcessorExecutorSelector(executorSelector);
        }
    }
}

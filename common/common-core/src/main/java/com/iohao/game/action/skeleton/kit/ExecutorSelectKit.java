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
package com.iohao.game.action.skeleton.kit;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.common.kit.concurrent.executor.ExecutorRegion;
import com.iohao.game.common.kit.concurrent.executor.ThreadExecutor;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.Optional;

/**
 * 线程执行器相关工具
 *
 * @author 渔民小镇
 * @date 2023-12-19
 */
@UtilityClass
public class ExecutorSelectKit {
    /**
     * 执行业务框架 （执行 action）
     *
     * @param barSkeleton 业务框架
     * @param flowContext flowContext
     * @return true 表示请求被执行
     */
    public boolean processLogic(BarSkeleton barSkeleton, FlowContext flowContext) {
        HeadMetadata headMetadata = flowContext.getHeadMetadata();
        final ThreadExecutor threadExecutor = getThreadExecutor(barSkeleton, headMetadata);

        if (Objects.isNull(threadExecutor)) {
            return false;
        }

        flowContext.option(FlowAttr.threadExecutor, threadExecutor);

        // 使用单例的 ThreadExecutorRegion 来处理，即使在同一进程中启动了多个逻辑服，也不会创建过多线程执行器，而是使用同一个。
        threadExecutor.execute(() -> {
            // 在当前线程执行器中执行业务框架
            barSkeleton.handle(flowContext);
        });

        return true;
    }

    private ThreadExecutor getThreadExecutor(BarSkeleton barSkeleton, HeadMetadata headMetadata) {
        final long executorIndex = getExecutorIndex(headMetadata);
        final ExecutorRegion executorRegion = barSkeleton.getExecutorRegion();

        final ExecutorSelectEnum executorSelect = headMetadata.getExecutorSelect();

        return switch (executorSelect) {
            case null -> executorRegion.getUserThreadExecutor(executorIndex);
            case userVirtualExecutor -> executorRegion.getUserVirtualThreadExecutor(executorIndex);
            case userExecutor -> executorRegion.getUserThreadExecutor(executorIndex);
            default -> null;
        };
    }

    public long getExecutorIndex(HeadMetadata headMetadata) {
        var userId = headMetadata.getUserId();
        if (userId != 0) {
            return userId;
        }

        // 如果没有登录，使用 channelId 计算；如果 channelId 不存在，则使用 cmd
        return Optional.ofNullable(headMetadata.getChannelId())
                .map(String::hashCode)
                .map(Math::abs)
                .orElseGet(headMetadata::getCmdMerge);
    }
}

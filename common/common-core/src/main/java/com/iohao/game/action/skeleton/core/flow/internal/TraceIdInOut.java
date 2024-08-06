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
package com.iohao.game.action.skeleton.core.flow.internal;

import com.iohao.game.action.skeleton.core.flow.ActionMethodInOut;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.common.kit.trace.TraceKit;
import org.slf4j.MDC;

import java.util.Objects;

/**
 * 业务框架插件 - <a href="https://www.yuque.com/iohao/game/xhvpqy">全链路调用日志跟踪插件</a>
 *
 * @author 渔民小镇
 * @date 2023-12-20
 */
public final class TraceIdInOut implements ActionMethodInOut {
    @Override
    public void fuckIn(FlowContext flowContext) {

        HeadMetadata headMetadata = flowContext.getHeadMetadata();
        String traceId = headMetadata.getTraceId();

        if (Objects.nonNull(traceId)) {
            MDC.put(TraceKit.traceName, traceId);
        }
    }

    @Override
    public void fuckOut(FlowContext flowContext) {
        MDC.clear();
    }
}

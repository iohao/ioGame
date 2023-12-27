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
package com.iohao.game.action.skeleton.core.flow;

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.flow.attr.FlowOptionDynamic;
import com.iohao.game.action.skeleton.kit.ExecutorSelectKit;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.common.kit.TraceKit;
import com.iohao.game.common.kit.concurrent.executor.ThreadExecutor;
import com.iohao.game.common.kit.concurrent.executor.UserVirtualExecutorRegion;
import org.slf4j.MDC;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * @author 渔民小镇
 * @date 2023-12-27
 */
public interface SimpleCommon extends FlowOptionDynamic {

    /**
     * FlowContext request HeadMetadata
     *
     * @return HeadMetadata
     */
    HeadMetadata getHeadMetadata();

    /**
     * userId
     *
     * @return userId
     */
    default long getUserId() {
        return this.getHeadMetadata().getUserId();
    }

    /**
     * 玩家对应的虚拟线程执行器
     *
     * @return 虚拟线程执行器
     */
    default Executor getVirtualExecutor() {
        // 得到用户对应的虚拟线程执行器
        final HeadMetadata headMetadata = this.getHeadMetadata();
        var executorIndex = ExecutorSelectKit.getExecutorIndex(headMetadata);

        ThreadExecutor threadExecutor = UserVirtualExecutorRegion.me().getThreadExecutor(executorIndex);
        return threadExecutor.executor();
    }

    /**
     * 使用虚拟线程执行任务
     *
     * @param command 任务
     */
    default void executeVirtual(Runnable command) {
        HeadMetadata headMetadata = this.getHeadMetadata();
        String traceId = headMetadata.getTraceId();

        if (Objects.isNull(traceId)) {
            this.getVirtualExecutor().execute(command);
            return;
        }

        this.getVirtualExecutor().execute(() -> {
            try {
                MDC.put(TraceKit.traceName, traceId);
                command.run();
            } finally {
                MDC.clear();
            }
        });
    }

    /**
     * 创建一个 request 对象，并使用当前 FlowContext HeadMetadata 部分属性。
     * <pre>
     *     HeadMetadata 对象以下属性不会赋值，如有需要，请自行赋值
     *       sourceClientId
     *       endPointClientId
     *       rpcCommandType
     *       msgId
     * </pre>
     *
     * @param cmdInfo 路由
     * @param data    业务参数
     * @return request
     */
    default RequestMessage createRequestMessage(final CmdInfo cmdInfo, final Object data) {
        HeadMetadata headMetadata = this.getHeadMetadata()
                .cloneHeadMetadata()
                .setCmdInfo(cmdInfo);

        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setHeadMetadata(headMetadata);

        if (Objects.nonNull(data)) {
            requestMessage.setData(data);
        }

        return requestMessage;
    }

    /**
     * 创建响应对象，通常用于广播
     * <pre>
     *     响应对象中的 HeadMetadata 对象，会复用当前用户的一些信息；
     * </pre>
     *
     * @param cmdInfo 路由
     * @param data    业务数据
     * @return 响应对象
     */
    default ResponseMessage createResponseMessage(CmdInfo cmdInfo, Object data) {
        Objects.requireNonNull(data);

        /*
         * 创建一个 HeadMetadata，并使用原有的一些信息；
         * 在广播时，只会给 HeadMetadata 中指定的游戏对外服广播。
         */
        HeadMetadata headMetadata = this.getHeadMetadata();

        HeadMetadata headMetadataClone = headMetadata
                .cloneHeadMetadata()
                .setCmdInfo(cmdInfo)
                .setEndPointClientId(headMetadata.getEndPointClientId())
                .setSourceClientId(headMetadata.getSourceClientId());

        // 创建一个响应对象
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setHeadMetadata(headMetadataClone);
        responseMessage.setData(data);

        return responseMessage;
    }
}

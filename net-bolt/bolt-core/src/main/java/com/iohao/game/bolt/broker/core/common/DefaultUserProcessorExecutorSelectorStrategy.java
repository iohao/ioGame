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
package com.iohao.game.bolt.broker.core.common;

import com.alipay.remoting.CustomSerializerManager;
import com.alipay.remoting.DefaultCustomSerializer;
import com.alipay.remoting.InvokeContext;
import com.alipay.remoting.rpc.RequestCommand;
import com.alipay.remoting.rpc.protocol.RpcRequestCommand;
import com.iohao.game.action.skeleton.kit.ExecutorSelectKit;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.common.kit.ByteKit;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * @author 渔民小镇
 * @date 2024-08-10
 * @since 21.15
 */
@Slf4j
final class DefaultUserProcessorExecutorSelectorStrategy extends DefaultCustomSerializer
        implements UserProcessorExecutorSelectorStrategy {

    final ProcessorSelectorThreadExecutorRegion threadExecutorRegion = new ProcessorSelectorThreadExecutorRegion();

    @Override
    public <T extends RequestCommand> boolean serializeHeader(T request, InvokeContext invokeContext) {
        if (request instanceof RpcRequestCommand command) {
            RequestMessage message = (RequestMessage) command.getRequestObject();
            HeadMetadata headMetadata = message.getHeadMetadata();

            if (Objects.isNull(headMetadata.getUserProcessorExecutorSelectorBytes())) {
                // 做一个简单的优化，避免多次序列化
                long executorIndex = ExecutorSelectKit.getExecutorIndex(headMetadata);
                headMetadata.setUserProcessorExecutorSelectorBytes(ByteKit.toBytes(executorIndex));
            }

            command.setHeader(headMetadata.getUserProcessorExecutorSelectorBytes());

            return true;
        }

        return false;
    }

    @Override
    public <T extends RequestCommand> boolean deserializeHeader(T request) {
        if (request instanceof RpcRequestCommand command) {
            byte[] header = command.getHeader();
            long executorIndex = ByteKit.getLong(header);
            command.setRequestHeader(executorIndex);

            return true;
        }

        return false;
    }

    @Override
    public Executor select(String requestClass, Object requestHeader) {
        if (Objects.isNull(requestHeader)) {
            return null;
        }

        // see RpcRequestProcessor.java:105
        long executorIndex = (long) requestHeader;
        return threadExecutorRegion.getThreadExecutor(executorIndex).executor();
    }

    private DefaultUserProcessorExecutorSelectorStrategy() {
        // 自定义序列化解析
        CustomSerializerManager.registerCustomSerializer(RequestMessage.class.getName(), this);
    }

    public static DefaultUserProcessorExecutorSelectorStrategy me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final DefaultUserProcessorExecutorSelectorStrategy ME = new DefaultUserProcessorExecutorSelectorStrategy();
    }
}

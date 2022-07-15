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
package com.iohao.game.action.skeleton.core.flow;

/**
 * ActionAfter 最后的处理
 * <pre>
 *     处理完每个action后会执行这个接口实现类(前提是你配置了)
 *     因为不知道各个NIO框架是如何写出数据到客户端的, 所以把这个问题交给用户自行处理
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
public interface ActionAfter {
    /**
     * 最后执行的方法, 一般将发送到客户端的逻辑存放到这里
     * <pre>
     * netty
     *     channelContext.writeAndFlush(msg);
     * </pre>
     *
     * @param flowContext flow 上下文
     */
    void execute(FlowContext flowContext);
}

/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.action.skeleton.core.commumication;

/**
 * 通信通道接口
 * <pre>
 *     用于对 bolt AsyncContext、netty Channel 的包装，这样可以使得业务框架与网络通信框架解耦。
 *     为将来 ioGame 实现绳量级架构的使用做准备，也为将来实现一套更符合游戏的网络通信框架做预留准备。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-12-04
 */
public interface ChannelContext {
    /**
     * 发送响应给请求端
     *
     * @param responseObject 响应对象
     */
    void sendResponse(Object responseObject);
}

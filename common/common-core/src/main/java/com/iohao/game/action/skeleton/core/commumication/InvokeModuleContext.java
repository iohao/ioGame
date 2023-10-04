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
package com.iohao.game.action.skeleton.core.commumication;

import com.iohao.game.action.skeleton.core.BarMessageKit;
import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.action.skeleton.protocol.SyncRequestMessage;
import com.iohao.game.action.skeleton.protocol.collect.ResponseCollectMessage;

/**
 * 内部模块通讯上下文，内部模块指的是游戏逻辑服
 * <pre>
 *     单个逻辑服与单个逻辑服通信请求-有响应值（可跨进程）
 *     单个逻辑服与单个逻辑服通信请求-无响应值（可跨进程）
 *     单个逻辑服与同类型多个逻辑服通信请求（可跨进程）
 * </pre>
 * 获取内部模块通讯上下文
 * <pre>{@code
 *     // 游戏逻辑服通讯上下文
 *     InvokeModuleContext invokeModuleContext = BrokerClientHelper.getInvokeModuleContext();
 * }
 * </pre>
 * 参考文档 <a href="https://www.yuque.com/iohao/game/gyxf7aykso8nb7z4">异步小技巧</a>
 * <pre>
 *     默认情况下，跨服且有返回值的 action 调用，则都是同步的；
 *     如果想要使用异步的方式，可以通过 CompletableFuture 或虚拟线程来实现。
 * </pre>
 * <p>
 * example async ： 通过 CompletableFuture 实现；
 * <pre>{@code
 *     CompletableFuture<YourMsg> future = CompletableFuture.supplyAsync(() -> {
 *         // 路由：这个路由是将要访问逻辑服的路由（表示你将要去的地方）
 *         CmdInfo cmdInfo = ...
 *         // 游戏逻辑服通讯上下文
 *         InvokeModuleContext invokeModuleContext = ...
 *         // 根据路由信息来请求其他子服务器（其他逻辑服）的数据
 *         return invokeModuleContext.invokeModuleMessageData(cmdInfo, YourMsg.class);
 *     });
 *
 *     ... 你的其他逻辑
 *     var msg = future.get();
 *     log.info("message : {} ", msg);
 * }
 * </pre>
 * <p>
 * example async ： 通过 CompletableFuture 实现的回调写法；
 * <pre>{@code
 *     CompletableFuture<YourMsg> future = CompletableFuture.supplyAsync(() -> {
 *         // 路由：这个路由是将要访问逻辑服的路由（表示你将要去的地方）
 *         CmdInfo cmdInfo = ...
 *         // 游戏逻辑服通讯上下文
 *         InvokeModuleContext invokeModuleContext = ...
 *         // 根据路由信息来请求其他子服务器（其他逻辑服）的数据
 *         return invokeModuleContext.invokeModuleMessageData(cmdInfo, YourMsg.class);
 *     }).thenAccept(msg -> {
 *         // 回调写法
 *         log.info("message : {}", msg);
 *     });
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-06-07
 */
public interface InvokeModuleContext {

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的方法，并且不需要返回值
     * <pre>
     *     异步无阻塞的方法，因为没有返回值；
     *     <a href="https://www.yuque.com/iohao/game/nelwuz#gtdrv">游戏逻辑服与单个游戏逻辑服通信请求 - 无返回值（可跨进程）</a>
     *     <a href="https://www.yuque.com/iohao/game/anguu6#cZfdx">单个逻辑服与单个逻辑服通信请求 - 无返回值（可跨进程）</a>
     * </pre>
     * example
     * <pre>{@code
     *     // 内部模块通讯上下文，内部模块指的是游戏逻辑服
     *     InvokeModuleContext invokeModuleContext = ...
     *     // 请求房间逻辑服来创建房间，并且不需要返回值
     *     // 路由、业务参数
     *     invokeModuleContext.invokeModuleVoidMessage(cmdInfo, data);
     * }
     * </pre>
     *
     * @param cmdInfo cmdInfo
     * @param data    请求参数
     */
    default void invokeModuleVoidMessage(CmdInfo cmdInfo, Object data) {
        RequestMessage requestMessage = BarMessageKit.createRequestMessage(cmdInfo, data);
        this.invokeModuleVoidMessage(requestMessage);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的方法，并且不需要返回值
     * <pre>
     *     异步无阻塞的方法，因为没有返回值；
     *     <a href="https://www.yuque.com/iohao/game/nelwuz#gtdrv">游戏逻辑服与单个游戏逻辑服通信请求 - 无返回值（可跨进程）</a>
     *     <a href="https://www.yuque.com/iohao/game/anguu6#cZfdx">单个逻辑服与单个逻辑服通信请求 - 无返回值（可跨进程）</a>
     * </pre>
     * example
     * <pre>{@code
     *     // 内部模块通讯上下文，内部模块指的是游戏逻辑服
     *     InvokeModuleContext invokeModuleContext = ...
     *     // 请求房间逻辑服来创建房间，并且不需要返回值
     *     // 路由、业务参数
     *     invokeModuleContext.invokeModuleVoidMessage(cmdInfo);
     * }
     * </pre>
     *
     * @param cmdInfo cmdInfo
     */
    default void invokeModuleVoidMessage(CmdInfo cmdInfo) {
        this.invokeModuleVoidMessage(cmdInfo, null);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的方法，并且不需要返回值
     * <pre>
     *     异步无阻塞的方法，因为没有返回值；
     *     <a href="https://www.yuque.com/iohao/game/nelwuz#gtdrv">游戏逻辑服与单个游戏逻辑服通信请求 - 无返回值（可跨进程）</a>
     *     <a href="https://www.yuque.com/iohao/game/anguu6#cZfdx">单个逻辑服与单个逻辑服通信请求 - 无返回值（可跨进程）</a>
     * </pre>
     * example
     * <pre>{@code
     *     // 内部模块通讯上下文，内部模块指的是游戏逻辑服
     *     InvokeModuleContext invokeModuleContext = ...
     *     // 请求房间逻辑服来创建房间，并且不需要返回值
     *     // 路由、业务参数
     *     invokeModuleContext.invokeModuleVoidMessage(requestMessage);
     * }
     * </pre>
     *
     * @param requestMessage requestMessage
     */
    void invokeModuleVoidMessage(RequestMessage requestMessage);

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     * <pre>
     *     相关文档
     *     <a href="https://www.yuque.com/iohao/game/nelwuz#L9TAJ">游戏逻辑服与单个游戏逻辑服通信请求 - 有返回值（可跨进程）</a>
     *     <a href="https://www.yuque.com/iohao/game/anguu6">游戏逻辑服之间的交互</a>
     *     <a href="https://www.yuque.com/iohao/game/gyxf7aykso8nb7z4">异步小技巧</a>
     * </pre>
     * example
     * <pre>{@code
     *     public void count() {
     *         // 路由：这个路由是将要访问逻辑服的路由（表示你将要去的地方）
     *         CmdInfo cmdInfo = ...
     *         YourData data = ...
     *         // 模块通讯上下文
     *         InvokeModuleContext invokeModuleContext = ...
     *         // 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     *         YourMsg msg = invokeModuleContext.invokeModuleMessageData(cmdInfo, data, YourMsg.class);
     *         log.info("message : {} ", msg);
     *     }
     * }
     * </pre>
     *
     * @param cmdInfo 路由信息
     * @param data    请求参数
     * @param clazz   response data class
     * @param <T>     t
     * @return pb 对象
     */
    default <T> T invokeModuleMessageData(CmdInfo cmdInfo, Object data, Class<T> clazz) {
        ResponseMessage responseMessage = invokeModuleMessage(cmdInfo, data);
        // 将字节解析成对象
        byte[] dataContent = responseMessage.getData();
        return DataCodecKit.decode(dataContent, clazz);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     * <pre>
     *     相关文档
     *     <a href="https://www.yuque.com/iohao/game/nelwuz#L9TAJ">游戏逻辑服与单个游戏逻辑服通信请求 - 有返回值（可跨进程）</a>
     *     <a href="https://www.yuque.com/iohao/game/anguu6">游戏逻辑服之间的交互</a>
     *     <a href="https://www.yuque.com/iohao/game/gyxf7aykso8nb7z4">异步小技巧</a>
     * </pre>
     * example
     * <pre>{@code
     *     public void count() {
     *         // 路由：这个路由是将要访问逻辑服的路由（表示你将要去的地方）
     *         CmdInfo cmdInfo = ...
     *         // 模块通讯上下文
     *         InvokeModuleContext invokeModuleContext = ...
     *         // 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     *         YourMsg msg = invokeModuleContext.invokeModuleMessageData(cmdInfo, YourMsg.class);
     *         log.info("message : {} ", msg);
     *     }
     * }
     * </pre>
     *
     * @param cmdInfo 路由信息
     * @param clazz   response data class
     * @param <T>     t
     * @return response data 解析后的数据
     */
    default <T> T invokeModuleMessageData(CmdInfo cmdInfo, Class<T> clazz) {
        return this.invokeModuleMessageData(cmdInfo, null, clazz);
    }

    /**
     * 根据 RequestMessage 来请求其他子服务器（其他逻辑服）的数据
     * <pre>
     *     相关文档
     *     <a href="https://www.yuque.com/iohao/game/nelwuz#L9TAJ">游戏逻辑服与单个游戏逻辑服通信请求 - 有返回值（可跨进程）</a>
     *     <a href="https://www.yuque.com/iohao/game/anguu6">游戏逻辑服之间的交互</a>
     *     <a href="https://www.yuque.com/iohao/game/gyxf7aykso8nb7z4">异步小技巧</a>
     * </pre>
     * example
     * <pre>{@code
     *     public void count() {
     *         RequestMessage request = ...
     *         // 模块通讯上下文
     *         InvokeModuleContext invokeModuleContext = ...
     *         // 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     *         YourMsg msg = invokeModuleContext.invokeModuleMessageData(request, YourMsg.class);
     *         log.info("message : {} ", msg);
     *     }
     * }
     * </pre>
     *
     * @param requestMessage RequestMessage
     * @param clazz          response data class
     * @param <T>            t
     * @return response data 解析后的数据
     */
    default <T> T invokeModuleMessageData(RequestMessage requestMessage, Class<T> clazz) {
        ResponseMessage responseMessage = this.invokeModuleMessage(requestMessage);

        // 将字节解析成对象
        byte[] dataContent = responseMessage.getData();
        return DataCodecKit.decode(dataContent, clazz);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     * <pre>
     *     相关文档
     *     <a href="https://www.yuque.com/iohao/game/nelwuz#L9TAJ">游戏逻辑服与单个游戏逻辑服通信请求 - 有返回值（可跨进程）</a>
     *     <a href="https://www.yuque.com/iohao/game/anguu6">游戏逻辑服之间的交互</a>
     *     <a href="https://www.yuque.com/iohao/game/gyxf7aykso8nb7z4">异步小技巧</a>
     * </pre>
     * example
     * <pre>{@code
     *     public void count() {
     *         // 路由：这个路由是将要访问逻辑服的路由（表示你将要去的地方）
     *         CmdInfo cmdInfo = ...
     *         YourData data = ...
     *         // 模块通讯上下文
     *         InvokeModuleContext invokeModuleContext = ...
     *         // 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     *         ResponseMessage responseMessage = invokeModuleContext.invokeModuleMessage(cmdInfo, data);
     *         // 得到逻辑服返回的业务数据
     *         YourMsg msg = responseMessage.getData(YourMsg.class);
     *         log.info("message : {} ", msg);
     *     }
     * }
     * </pre>
     *
     * @param cmdInfo cmdInfo
     * @param data    请求参数
     * @return ResponseMessage
     */
    default ResponseMessage invokeModuleMessage(CmdInfo cmdInfo, Object data) {
        RequestMessage requestMessage = BarMessageKit.createRequestMessage(cmdInfo, data);

        return this.invokeModuleMessage(requestMessage);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     * <pre>
     *     相关文档
     *     <a href="https://www.yuque.com/iohao/game/nelwuz#L9TAJ">游戏逻辑服与单个游戏逻辑服通信请求 - 有返回值（可跨进程）</a>
     *     <a href="https://www.yuque.com/iohao/game/anguu6">游戏逻辑服之间的交互</a>
     *     <a href="https://www.yuque.com/iohao/game/gyxf7aykso8nb7z4">异步小技巧</a>
     * </pre>
     * example
     * <pre>{@code
     *     public void count() {
     *         // 路由：这个路由是将要访问逻辑服的路由（表示你将要去的地方）
     *         CmdInfo cmdInfo = ...
     *         // 模块通讯上下文
     *         InvokeModuleContext invokeModuleContext = ...
     *         // 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     *         ResponseMessage responseMessage = invokeModuleContext.invokeModuleMessage(cmdInfo);
     *         // 得到逻辑服返回的业务数据
     *         YourMsg msg = responseMessage.getData(YourMsg.class);
     *         log.info("message : {} ", msg);
     *     }
     * }
     * </pre>
     *
     * @param cmdInfo cmdInfo
     * @return ResponseMessage
     */
    default ResponseMessage invokeModuleMessage(CmdInfo cmdInfo) {
        return this.invokeModuleMessage(cmdInfo, null);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     * <pre>
     *     相关文档
     *     <a href="https://www.yuque.com/iohao/game/nelwuz#L9TAJ">游戏逻辑服与单个游戏逻辑服通信请求 - 有返回值（可跨进程）</a>
     *     <a href="https://www.yuque.com/iohao/game/anguu6">游戏逻辑服之间的交互</a>
     *     <a href="https://www.yuque.com/iohao/game/gyxf7aykso8nb7z4">异步小技巧</a>
     * </pre>
     * example
     * <pre>{@code
     *     public void count() {
     *         RequestMessage request = ...
     *         // 模块通讯上下文
     *         InvokeModuleContext invokeModuleContext = ...
     *         // 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     *         ResponseMessage responseMessage = invokeModuleContext.invokeModuleMessage(request);
     *         // 得到逻辑服返回的业务数据
     *         YourMsg msg = responseMessage.getData(YourMsg.class);
     *         log.info("message : {} ", msg);
     *     }
     * }
     * </pre>
     *
     * @param requestMessage requestMessage
     * @return ResponseMessage
     */
    ResponseMessage invokeModuleMessage(RequestMessage requestMessage);

    /**
     * 模块之间的访问，访问【同类型】的多个逻辑服
     * <pre>
     *     模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是逻辑服。
     *     假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起。
     *
     *     具体的意思可以参考文档中的说明
     *     <a href="https://www.yuque.com/iohao/game/nelwuz#gSdya">游戏逻辑服与同类型多个游戏逻辑服通信请求（可跨进程）</a>
     *     <a href="https://www.yuque.com/iohao/game/rf9rb9">请求同类型多个逻辑服通信结果</a>
     *     <a href="https://www.yuque.com/iohao/game/gyxf7aykso8nb7z4">异步小技巧</a>
     * </pre>
     * example
     * <pre>{@code
     *     public void count() {
     *         // 路由：这个路由是将要访问逻辑服的路由（表示你将要去的地方）
     *         CmdInfo cmdInfo = ...
     *         YourData data = ...
     *         // 模块通讯上下文
     *         InvokeModuleContext invokeModuleContext = ...
     *         // 根据路由信息来请求其他【同类型】的多个子服务器（其他逻辑服）数据
     *         var responseCollectMessage = invokeModuleContext.invokeModuleCollectMessage(cmdInfo, data);
     *
     *         // 每个逻辑服返回的数据集合
     *         List<ResponseCollectItemMessage> messageList = responseCollectMessage.getMessageList();
     *
     *         for (ResponseCollectItemMessage responseCollectItemMessage : messageList) {
     *             ResponseMessage responseMessage = responseCollectItemMessage.getResponseMessage();
     *             // 得到逻辑服返回的业务数据
     *             YourMsg msg = responseMessage.getData(YourMsg.class);
     *             log.info("message : {} ", msg);
     *         }
     *     }
     * }
     * </pre>
     *
     * @param cmdInfo 路由信息
     * @param data    业务数据
     * @return ResponseCollectMessage
     */
    default ResponseCollectMessage invokeModuleCollectMessage(CmdInfo cmdInfo, Object data) {
        SyncRequestMessage requestMessage = new SyncRequestMessage();

        BarMessageKit.employ(requestMessage, cmdInfo, data);

        return this.invokeModuleCollectMessage(requestMessage);
    }

    /**
     * 模块之间的访问，访问【同类型】的多个逻辑服
     * <pre>
     *     模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是逻辑服。
     *     假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起。
     *
     *     具体的意思可以参考文档中的说明
     *     <a href="https://www.yuque.com/iohao/game/nelwuz#gSdya">游戏逻辑服与同类型多个游戏逻辑服通信请求（可跨进程）</a>
     *     <a href="https://www.yuque.com/iohao/game/rf9rb9">请求同类型多个逻辑服通信结果</a>
     *     <a href="https://www.yuque.com/iohao/game/gyxf7aykso8nb7z4">异步小技巧</a>
     * </pre>
     * example
     * <pre>{@code
     *     public void count() {
     *         // 模块通讯上下文
     *         InvokeModuleContext invokeModuleContext = ...
     *         // 路由：这个路由是将要访问逻辑服的路由（表示你将要去的地方）
     *         CmdInfo cmdInfo = ...
     *         // 根据路由信息来请求其他【同类型】的多个子服务器（其他逻辑服）数据
     *         ResponseCollectMessage responseCollectMessage = invokeModuleContext.invokeModuleCollectMessage(cmdInfo);
     *
     *         // 每个逻辑服返回的数据集合
     *         List<ResponseCollectItemMessage> messageList = responseCollectMessage.getMessageList();
     *
     *         for (ResponseCollectItemMessage responseCollectItemMessage : messageList) {
     *             ResponseMessage responseMessage = responseCollectItemMessage.getResponseMessage();
     *             // 得到逻辑服返回的业务数据
     *             YourMsg msg = responseMessage.getData(YourMsg.class);
     *             log.info("message : {} ", msg);
     *        }
     *    }
     * }
     * </pre>
     *
     * @param cmdInfo 路由信息
     * @return ResponseCollectMessage
     */
    default ResponseCollectMessage invokeModuleCollectMessage(CmdInfo cmdInfo) {
        return invokeModuleCollectMessage(cmdInfo, null);
    }

    /**
     * 模块之间的访问，访问【同类型】的多个逻辑服
     * <pre>
     *     模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是逻辑服。
     *     假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。
     *     框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起。
     *
     *     具体的意思可以参考文档中的说明
     *     <a href="https://www.yuque.com/iohao/game/nelwuz#gSdya">游戏逻辑服与同类型多个游戏逻辑服通信请求（可跨进程）</a>
     *     <a href="https://www.yuque.com/iohao/game/rf9rb9">请求同类型多个逻辑服通信结果</a>
     *     <a href="https://www.yuque.com/iohao/game/gyxf7aykso8nb7z4">异步小技巧</a>
     * </pre>
     * example
     * <pre>{@code
     *     public void count() {
     *         RequestMessage request = ...
     *         // 模块通讯上下文
     *         InvokeModuleContext invokeModuleContext = ...
     *         // 根据路由信息来请求其他【同类型】的多个子服务器（其他逻辑服）数据
     *         ResponseCollectMessage responseCollectMessage = invokeModuleContext.invokeModuleCollectMessage(request);
     *
     *         // 每个逻辑服返回的数据集合
     *         List<ResponseCollectItemMessage> messageList = responseCollectMessage.getMessageList();
     *
     *         for (ResponseCollectItemMessage responseCollectItemMessage : messageList) {
     *             ResponseMessage responseMessage = responseCollectItemMessage.getResponseMessage();
     *             // 得到逻辑服返回的业务数据
     *             YourMsg msg = responseMessage.getData(YourMsg.class);
     *             log.info("message : {} ", msg);
     *         }
     *     }
     * }
     * </pre>
     *
     * @param requestMessage requestMessage
     * @return ResponseAggregationMessage
     */
    ResponseCollectMessage invokeModuleCollectMessage(RequestMessage requestMessage);
}

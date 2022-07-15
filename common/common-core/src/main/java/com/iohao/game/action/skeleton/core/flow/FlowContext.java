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

import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.core.BarMessageKit;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.commumication.BrokerClientContext;
import com.iohao.game.action.skeleton.core.commumication.InvokeModuleContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.core.flow.attr.FlowOption;
import com.iohao.game.action.skeleton.core.flow.attr.FlowOptionDynamic;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.action.skeleton.protocol.collect.ResponseCollectMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务框架 flow 上下文
 * <pre>
 *     生命周期存在于这一次的 flow 过程
 *
 *     实现了类型明确的动态属性接口 {@link FlowOptionDynamic} ，实现类只需要实现 getOptions 方法就能具有动态属性的功能。
 *     动态属性可以更方便的为 FlowContext 实现属性的扩展，以方便开发者。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-21
 */
@Setter
@Getter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class FlowContext implements FlowOptionDynamic {
    /** 动态属性 */
    final Map<FlowOption<?>, Object> options = new HashMap<>();

    /** 业务框架 */
    BarSkeleton barSkeleton;
    /** command */
    ActionCommand actionCommand;
    /** 控制器类对象 */
    Object actionController;
    /** 请求对象 */
    RequestMessage request;
    /** 响应对象 */
    ResponseMessage response;
    /** 业务方法参数 */
    Object[] methodParams;
    /** 业务方法的返回值 */
    Object methodResult;
    /** userId */
    long userId;
    /** true 业务方法有异常 */
    boolean error;

    /**
     * cmdInfo
     *
     * @param cmd    主 cmd
     * @param subCmd 子 cmd
     * @return cmdInfo
     */
    public CmdInfo getCmdInfo(int cmd, int subCmd) {
        return CmdInfo.getCmdInfo(cmd, subCmd);
    }

    public CmdInfo getCmdInfo() {
        HeadMetadata headMetadata = this.request.getHeadMetadata();
        return headMetadata.getCmdInfo();
    }

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     * <pre>
     *     相关文档
     *     https://www.yuque.com/iohao/game/anguu6
     * </pre>
     *
     * @param cmdInfo 路由信息
     * @param data    请求参数
     * @param clazz   pb class
     * @param <T>     t
     * @return pb 对象
     */
    public <T> T invokeModuleMessageData(CmdInfo cmdInfo, Object data, Class<T> clazz) {
        // 当前项目启动的服务上下文
        BrokerClientContext brokerClientContext = this.option(FlowAttr.brokerClientContext);
        InvokeModuleContext invokeModuleContext = brokerClientContext.getInvokeModuleContext();

        return invokeModuleContext.invokeModuleMessageData(cmdInfo, data, clazz);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     * <pre>
     *     相关文档
     *     https://www.yuque.com/iohao/game/anguu6
     * </pre>
     *
     * @param cmdInfo 路由信息
     * @param clazz   pb class
     * @param <T>     t
     * @return pb 对象
     */
    public <T> T invokeModuleMessageData(CmdInfo cmdInfo, Class<T> clazz) {
        return this.invokeModuleMessageData(cmdInfo, null, clazz);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     * <pre>
     *     相关文档
     *     https://www.yuque.com/iohao/game/anguu6
     * </pre>
     *
     * @param cmdInfo cmdInfo
     * @param data    请求参数
     * @return ResponseMessage
     */
    public ResponseMessage invokeModuleMessage(CmdInfo cmdInfo, Object data) {

        RequestMessage requestMessage = getRequestMessage(cmdInfo, data);
        // 当前项目启动的服务上下文
        BrokerClientContext brokerClientContext = this.option(FlowAttr.brokerClientContext);
        InvokeModuleContext invokeModuleContext = brokerClientContext.getInvokeModuleContext();

        return invokeModuleContext.invokeModuleMessage(requestMessage);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     * <pre>
     *     相关文档
     *     https://www.yuque.com/iohao/game/anguu6
     * </pre>
     *
     * @param cmdInfo cmdInfo
     * @return ResponseMessage
     */
    public ResponseMessage invokeModuleMessage(CmdInfo cmdInfo) {
        return this.invokeModuleMessage(cmdInfo, null);
    }

    /**
     * 模块之间的访问，访问【同类型】的多个逻辑服
     * <pre>
     *     模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是逻辑服。
     *     假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起。
     *
     *     具体的意思可以参考文档中的说明
     *     https://www.yuque.com/iohao/game/rf9rb9
     * </pre>
     *
     * @param cmdInfo 路由信息
     * @param data    业务数据
     * @return ResponseCollectMessage
     */
    public ResponseCollectMessage invokeModuleCollectMessage(CmdInfo cmdInfo, Object data) {
        RequestMessage requestMessage = getRequestMessage(cmdInfo, data);
        // 当前项目启动的服务上下文
        BrokerClientContext brokerClientContext = this.option(FlowAttr.brokerClientContext);
        InvokeModuleContext invokeModuleContext = brokerClientContext.getInvokeModuleContext();

        return invokeModuleContext.invokeModuleCollectMessage(requestMessage);
    }

    /**
     * 模块之间的访问，访问【同类型】的多个逻辑服
     * <pre>
     *     模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是逻辑服。
     *     假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起。
     *
     *     具体的意思可以参考文档中的说明
     *     https://www.yuque.com/iohao/game/rf9rb9
     * </pre>
     *
     * @param cmdInfo 路由信息
     * @return ResponseCollectMessage
     */
    public ResponseCollectMessage invokeModuleCollectMessage(CmdInfo cmdInfo) {
        return invokeModuleCollectMessage(cmdInfo, null);
    }

    private RequestMessage getRequestMessage(CmdInfo cmdInfo, Object data) {
        RequestMessage requestMessage = BarMessageKit.createRequestMessage(cmdInfo, data);

        /*
         * 通过 flowContext 上下文创建的 RequestMessage 把userId、extJsonField 添加上
         * 理论上内部模块通讯也很少用得上这些信息
         */
        String extJsonField = this.request.getHeadMetadata().getExtJsonField();
        requestMessage.getHeadMetadata()
                .setUserId(this.getUserId())
                .setExtJsonField(extJsonField)
        ;

        return requestMessage;
    }
}

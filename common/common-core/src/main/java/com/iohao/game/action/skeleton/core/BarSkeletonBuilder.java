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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.core.doc.ActionSendDocs;
import com.iohao.game.action.skeleton.core.doc.BarSkeletonDoc;
import com.iohao.game.action.skeleton.core.doc.ErrorCodeDocs;
import com.iohao.game.action.skeleton.core.exception.MsgExceptionInfo;
import com.iohao.game.action.skeleton.core.flow.*;
import com.iohao.game.action.skeleton.core.flow.interal.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 骨架构建器
 * <pre>
 *     关于业务框架的构建器可以参考这里：
 *
 *     <a href="https://www.yuque.com/iohao/game/qiiaq3">文档-业务框架的构建器</a>
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
@Setter
@Accessors(chain = true)
public final class BarSkeletonBuilder {
    /** BarSkeletonSetting */
    @Getter
    final BarSkeletonSetting setting = new BarSkeletonSetting();

    /** handler 列表 */
    final List<Handler> handlerList = new LinkedList<>();
    /** inout 列表 */
    final List<ActionMethodInOut> inOutList = new LinkedList<>();
    /** action class */
    final List<Class<?>> actionControllerClazzList = new LinkedList<>();
    /** action send class */
    final List<Class<?>> actionSendClazzList = new LinkedList<>();
    /** 错误码 */
    final List<MsgExceptionInfo> msgExceptionInfoList = new ArrayList<>();
    /** 推送相关的文档 */
    final ActionSendDocs actionSendDocs = new ActionSendDocs();
    /** 错误码相关的文档 */
    final ErrorCodeDocs errorCodeDocs = new ErrorCodeDocs();
    /** action工厂 */
    @SuppressWarnings("unchecked")
    ActionFactoryBean<Object> actionFactoryBean = DefaultActionFactoryBean.me();
    /** action 执行完后，最后需要做的事。 一般用于将数据发送到 Broker（游戏网关） */
    ActionAfter actionAfter = DefaultActionAfter.me();
    /** 结果包装器 */
    ActionMethodResultWrap actionMethodResultWrap = DefaultActionMethodResultWrap.me();
    /** 异常处理 */
    ActionMethodExceptionProcess actionMethodExceptionProcess = DefaultActionMethodExceptionProcess.me();
    /** InvokeActionMethod */
    ActionMethodInvoke actionMethodInvoke = DefaultActionMethodInvoke.me();
    /** ActionMethod 方法参数解析器 */
    ActionMethodParamParser actionMethodParamParser = DefaultActionMethodParamParser.me();
    /** 响应对象的创建 */
    ResponseMessageCreate responseMessageCreate = DefaultResponseMessageCreate.me();
    /** 业务框架 flow 上下文 工厂 */
    FlowContextFactory flowContextFactory = FlowContext::new;

    BarSkeletonBuilder() {
    }

    /**
     * 构建骨架, 提供了一些默认配置
     */
    public BarSkeleton build() {

        this.defaultSetting();

        var handlers = new Handler[this.handlerList.size()];
        this.handlerList.toArray(handlers);

        // 业务框架参数设置
        var barSkeleton = new BarSkeleton(handlers)
                // action 工厂
                .setActionFactoryBean(this.actionFactoryBean)
                // ActionMethod Invoke 方法回调
                .setActionMethodInvoke(this.actionMethodInvoke)
                // ActionMethod 方法参数解析器
                .setActionMethodParamParser(this.actionMethodParamParser)
                // ActionMethod 的异常处理
                .setActionMethodExceptionProcess(this.actionMethodExceptionProcess)
                // ActionMethod 结果包装器
                .setActionMethodResultWrap(this.actionMethodResultWrap)
                // action after， action 执行完后，最后需要做的事。 一般用于将数据发送到 Broker（游戏网关）
                .setActionAfter(this.actionAfter)
                // 响应对象的创建
                .setResponseMessageCreate(this.responseMessageCreate)
                // 推送相关的文档
                .setActionSendDocs(this.actionSendDocs)
                // 错误码相关的文档
                .setErrorCodeDocs(this.errorCodeDocs)
                // 业务框架 flow 上下文 工厂
                .setFlowContextFactory(this.flowContextFactory);

        // 构建推送相关的文档信息
        this.actionSendDocs.buildActionSendDoc(this.actionSendClazzList);

        // inout
        extractedInOut(barSkeleton);

        // 构建 actionMapping
        extractedActionCommand(barSkeleton);

        // 控制台打印
        PrintActionKit.print(barSkeleton, this.setting);

        BarSkeletonDoc.me().addSkeleton(barSkeleton);

        return barSkeleton;
    }

    public BarSkeletonBuilder addMsgExceptionInfo(MsgExceptionInfo msgExceptionInfo) {
        Objects.requireNonNull(msgExceptionInfo);
        this.errorCodeDocs.addMsgExceptionInfo(msgExceptionInfo);
        return this;
    }

    public BarSkeletonBuilder addActionController(Class<?> controller) {
        Objects.requireNonNull(controller);
        this.actionControllerClazzList.add(controller);
        return this;
    }

    public BarSkeletonBuilder addActionSend(Class<?> actionSend) {
        Objects.requireNonNull(actionSend);
        this.actionSendClazzList.add(actionSend);
        return this;
    }

    public BarSkeletonBuilder addHandler(Handler handler) {
        Objects.requireNonNull(handler);
        // 先进先执行
        this.handlerList.add(handler);
        return this;
    }

    /**
     * 添加 inOut
     *
     * @param inOut inOut
     * @return this
     */
    public BarSkeletonBuilder addInOut(ActionMethodInOut inOut) {
        Objects.requireNonNull(inOut);
        this.inOutList.add(inOut);
        return this;
    }

    private void extractedInOut(BarSkeleton barSkeleton) {
        var inOutManager = new InOutManager(this.setting, this.inOutList);
        barSkeleton.setInOutManager(inOutManager);
    }

    private void extractedActionCommand(BarSkeleton barSkeleton) {
        // action 命令对象解析器
        var actionCommandParser = new ActionCommandParser(setting)
                // 根据 action 类列表，来构建 ActionCommand
                .buildAction(this.actionControllerClazzList);

        var actionCommandRegions = actionCommandParser.getActionCommandRegions();

        // 将 ActionCommandRegions 命令域管理器，保存到业务框架中
        barSkeleton.setActionCommandRegions(actionCommandRegions);
    }

    private void defaultSetting() {
        // 如果没有配置 handler，那么使用默认的
        if (this.handlerList.isEmpty()) {
            this.handlerList.add(new ActionCommandHandler());
        }
    }
}
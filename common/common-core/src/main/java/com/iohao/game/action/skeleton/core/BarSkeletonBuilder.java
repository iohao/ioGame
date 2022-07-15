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
import com.iohao.game.action.skeleton.core.flow.codec.DataCodec;
import com.iohao.game.action.skeleton.core.flow.codec.ProtoDataCodec;
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
 *     https://www.yuque.com/iohao/game/qiiaq3
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
@Accessors(chain = true)
@Setter
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

    /** 命令执行器 */
    ActionCommandFlowExecute actionCommandFlowExecute = DefaultActionCommandFlowExecute.me();
    /** action工厂 */
    @SuppressWarnings("unchecked")
    ActionFactoryBean<Object> actionFactoryBean = DefaultActionFactoryBean.me();
    /** 框架执行完后, 最后需要做的事. 一般用于write数据到调用端端 */
    ActionAfter actionAfter = new DefaultActionAfter();
    /** 结果包装器 */
    ActionMethodResultWrap actionMethodResultWrap = DefaultActionMethodResultWrap.me();
    /** 异常处理 */
    ActionMethodExceptionProcess actionMethodExceptionProcess = DefaultActionMethodExceptionProcess.me();
    /** InvokeActionMethod */
    ActionMethodInvoke actionMethodInvoke = DefaultActionMethodInvoke.me();
    /** ActionMethod 方法参数解析器 */
    ActionMethodParamParser actionMethodParamParser = DefaultActionMethodParamParser.me();
    /** 业务参数的编解码器 */
    DataCodec dataCodec = ProtoDataCodec.me();

    /** 响应对象的创建 */
    ResponseMessageCreate responseMessageCreate = DefaultResponseMessageCreate.me();
    /** 推送相关的文档 */
    ActionSendDocs actionSendDocs = new ActionSendDocs();
    /** 错误码相关的文档 */
    ErrorCodeDocs errorCodeDocs = new ErrorCodeDocs();

    BarSkeletonBuilder() {
    }

    private void before() {
    }

    /**
     * 构建骨架, 提供了一些默认配置
     */
    public BarSkeleton build() {

        this.before();

        // 参数设置
        var barSkeleton = this.createBarSkeleton()
                // action command 命令执行器流程
                .setActionCommandFlowExecute(this.actionCommandFlowExecute)
                // action 工厂
                .setActionFactoryBean(this.actionFactoryBean)
                // ActionMethod Invoke 方法回调
                .setActionMethodInvoke(this.actionMethodInvoke)
                // ActionMethod 方法参数解析器
                .setActionMethodParamParser(this.actionMethodParamParser)
                // 业务参数的编解码器
                .setDataCodec(this.dataCodec)
                // ActionMethod 的异常处理
                .setActionMethodExceptionProcess(this.actionMethodExceptionProcess)
                // ActionMethod 结果包装器
                .setActionMethodResultWrap(this.actionMethodResultWrap)
                // action after 对action最后的处理; 一般用于把结果 write 到调用端
                .setActionAfter(this.actionAfter)
                // 响应对象的创建
                .setResponseMessageCreate(this.responseMessageCreate)
                // 推送相关的文档
                .setActionSendDocs(this.actionSendDocs)
                // 错误码相关的文档
                .setErrorCodeDocs(this.errorCodeDocs);

        // 保存业务数据的编解码器
        DataCodecKit.dataCodec = this.dataCodec;

        // 构建推送相关的文档信息
        this.actionSendDocs.buildActionSendDoc(this.actionSendClazzList);

        // inout
        extractedInOut(barSkeleton);

        // 构建 actionMapping
        extractedActionCommand(barSkeleton);

        // 日志打印
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
        // 命令信息构建器
        var actionCommandInfoBuilder = new ActionCommandInfoBuilder(setting)
                .buildAction(this.actionControllerClazzList);

        var actionCommandRegions = actionCommandInfoBuilder.getActionCommandRegions();

        barSkeleton.setActionCommandRegions(actionCommandRegions);


    }

    private BarSkeleton createBarSkeleton() {

        // 如果没有配置handler, 那么使用默认的
        if (this.handlerList.isEmpty()) {
            this.handlerList.add(new ActionCommandHandler());
        }

        var handlers = new Handler[this.handlerList.size()];
        this.handlerList.toArray(handlers);

        return new BarSkeleton(handlers);
    }
}

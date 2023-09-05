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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.core.doc.ActionSendDoc;
import com.iohao.game.action.skeleton.core.doc.ActionSendDocs;
import com.iohao.game.action.skeleton.core.doc.BarSkeletonDoc;
import com.iohao.game.action.skeleton.core.doc.ErrorCodeDocs;
import com.iohao.game.action.skeleton.core.exception.MsgExceptionInfo;
import com.iohao.game.action.skeleton.core.flow.*;
import com.iohao.game.action.skeleton.core.flow.interal.*;
import com.iohao.game.action.skeleton.core.runner.Runner;
import com.iohao.game.action.skeleton.core.runner.Runners;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.*;

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
    final Runners runners = new Runners();
    /** handler 列表 */
    final List<Handler> handlerList = new LinkedList<>();
    /** ActionCommand 执行前与执行后的逻辑钩子类 */
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
    ActionFactoryBean<Object> actionFactoryBean = new DefaultActionFactoryBean<>();
    /** action 执行完后，最后需要做的事。 一般用于将数据发送到 Broker（游戏网关） */
    ActionAfter actionAfter = new DefaultActionAfter();
    /** 结果包装器 */
    ActionMethodResultWrap actionMethodResultWrap = new DefaultActionMethodResultWrap();
    /** 异常处理 */
    ActionMethodExceptionProcess actionMethodExceptionProcess = new DefaultActionMethodExceptionProcess();
    /** InvokeActionMethod */
    ActionMethodInvoke actionMethodInvoke = new DefaultActionMethodInvoke();
    /** ActionMethod 方法参数解析器 */
    ActionMethodParamParser actionMethodParamParser = new DefaultActionMethodParamParser();
    /** 响应对象的创建 */
    ResponseMessageCreate responseMessageCreate = new DefaultResponseMessageCreate();
    /** 业务框架 flow 上下文 工厂 */
    FlowContextFactory flowContextFactory = FlowContext::new;

    BarSkeletonBuilder() {
    }

    /**
     * 构建骨架, 提供了一些默认配置
     */
    public BarSkeleton build() {

        // 设置一些默认值
        this.defaultSetting();

        // 业务框架处理器
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
                .setFlowContextFactory(this.flowContextFactory)
                // runners 机制
                .setRunners(this.runners);

        // 构建推送相关的文档信息
        this.actionSendDocs.buildActionSendDoc(this.actionSendClazzList);

        // inout
        this.extractedInOut(barSkeleton);

        // 构建 actionMapping
        this.extractedActionCommand(barSkeleton);

        // 控制台打印
        PrintActionKit.print(barSkeleton, this.setting);

        // 文档相关
        BarSkeletonDoc.me().addSkeleton(barSkeleton);
        BarSkeletonDoc.me().setGenerateDoc(this.setting.generateDoc);

        this.runners.setBarSkeleton(barSkeleton);

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

    public BarSkeletonBuilder addActionSendDoc(ActionSendDoc actionSendDoc) {
        this.actionSendDocs.add(actionSendDoc);
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
     * <pre>
     *     如果存在相同的类型，则覆盖之前的
     * </pre>
     *
     * @param inOut inOut
     * @return this
     */
    public BarSkeletonBuilder addInOut(ActionMethodInOut inOut) {
        Objects.requireNonNull(inOut);
        this.inOutList.add(inOut);
        return this;
    }

    /**
     * 添加 Runner
     *
     * @param runner Runner
     * @return this
     */
    public BarSkeletonBuilder addRunner(Runner runner) {
        this.runners.addRunner(runner);
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
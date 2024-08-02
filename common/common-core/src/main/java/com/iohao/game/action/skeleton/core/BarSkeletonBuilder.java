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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.core.doc.*;
import com.iohao.game.action.skeleton.core.exception.MsgExceptionInfo;
import com.iohao.game.action.skeleton.core.flow.*;
import com.iohao.game.action.skeleton.core.flow.internal.*;
import com.iohao.game.action.skeleton.core.action.parser.ActionParserListener;
import com.iohao.game.action.skeleton.core.runner.Runner;
import com.iohao.game.action.skeleton.core.runner.Runners;
import com.iohao.game.common.kit.concurrent.executor.*;
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
    /** action class */
    final List<Class<?>> actionControllerClazzList = new LinkedList<>();
    /** 错误码 */
    final List<MsgExceptionInfo> msgExceptionInfoList = new ArrayList<>();
    /** action 构建时的钩子方法 */
    ActionParserListeners actionParserListeners = new ActionParserListeners();
    /** action工厂 */
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
    /** 线程执行器 */
    ExecutorRegion executorRegion;
    /** InOut 插件相管理器，ActionCommand 执行前与执行后的逻辑钩子类 */
    InOutManager inOutManager = InOutManager.ofPipeline();

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
                // 业务框架 flow 上下文 工厂
                .setFlowContextFactory(this.flowContextFactory)
                // 线程执行器
                .setExecutorRegion(this.executorRegion)
                // runners 机制
                .setRunners(this.runners)
                // inout
                .setInOutManager(this.inOutManager);

        // 构建 actionMapping
        this.extractedActionCommand(barSkeleton);

        // 控制台打印
        PrintActionKit.print(barSkeleton, this.setting);

        // 文档相关
        IoGameDocumentHelper.setGenerateDoc(this.setting.generateDoc);

        this.runners.setBarSkeleton(barSkeleton);

        this.actionParserListeners = null;

        return barSkeleton;
    }

    public BarSkeletonBuilder addActionController(Class<?> controller) {
        Objects.requireNonNull(controller);
        this.actionControllerClazzList.add(controller);
        return this;
    }

    /**
     * 添加广播文档
     *
     * @param builder 广播文档构建器
     * @return this
     */
    public BarSkeletonBuilder addBroadcastDocument(BroadcastDocumentBuilder builder) {
        IoGameDocumentHelper.addBroadcastDocument(builder.build());
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
        this.inOutManager.addInOut(inOut);
        return this;
    }

    /**
     * 添加 Runner 机制，会在逻辑服与 Broker（游戏网关）建立连接之前（onStart）、之后（onStartAfter）分别触发一次。
     *
     * @param runner Runner
     * @return this
     */
    public BarSkeletonBuilder addRunner(Runner runner) {
        this.runners.addRunner(runner);
        return this;
    }

    public BarSkeletonBuilder addActionParserListener(ActionParserListener listener) {
        this.actionParserListeners.addActionParserListener(listener);
        return this;
    }

    private void extractedActionCommand(BarSkeleton barSkeleton) {
        // action 命令对象解析器
        var actionCommandParser = new ActionCommandParser(this)
                .setBarSkeleton(barSkeleton)
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

        // 创建线程执行器
        if (Objects.isNull(this.executorRegion)) {
            this.executorRegion = ExecutorRegionKit.createExecutorRegion();
        }
    }

    /**
     * addMsgExceptionInfo
     *
     * @param msgExceptionInfo msgExceptionInfo
     * @return BarSkeletonBuilder
     * @deprecated 请使用 {@link IoGameDocumentHelper#addErrorCodeClass(Class)} 代替
     */
    @Deprecated
    public BarSkeletonBuilder addMsgExceptionInfo(MsgExceptionInfo msgExceptionInfo) {
//        Objects.requireNonNull(msgExceptionInfo);
//        this.errorCodeDocs.addMsgExceptionInfo(msgExceptionInfo);
        return this;
    }

    /**
     * addActionSend
     *
     * @param actionSend actionSend
     * @return BarSkeletonBuilder
     * @deprecated 请使用 {@link BarSkeletonBuilder#addBroadcastDoc(BroadcastDocBuilder)} 代替
     */
    @Deprecated
    public BarSkeletonBuilder addActionSend(Class<?> actionSend) {
//        Objects.requireNonNull(actionSend);
//        this.actionSendClazzList.add(actionSend);
        return this;
    }

    /**
     * addActionSendDoc
     *
     * @param actionSendDoc actionSendDoc
     * @return BarSkeletonBuilder
     * @deprecated 请使用 {@link BarSkeletonBuilder#addBroadcastDoc(BroadcastDocBuilder)} 代替
     */
    @Deprecated
    public BarSkeletonBuilder addActionSendDoc(ActionSendDoc actionSendDoc) {
//        this.actionSendDocs.add(actionSendDoc);
        return this;
    }

    /**
     * 添加广播文档
     *
     * @param broadcastDocBuilder broadcastDocBuilder
     * @return this
     * @deprecated 请使用 {@link BarSkeletonBuilder#addBroadcastDocument(BroadcastDocumentBuilder)}
     */
    @Deprecated
    public BarSkeletonBuilder addBroadcastDoc(BroadcastDocBuilder broadcastDocBuilder) {
        IoGameDocumentHelper.addBroadcastDocument(broadcastDocBuilder.buildDocument());
        return this;
    }
}
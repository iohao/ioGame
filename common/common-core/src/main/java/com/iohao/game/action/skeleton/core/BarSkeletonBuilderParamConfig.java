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

import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.DocActionSends;
import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.core.exception.MsgExceptionInfo;
import com.iohao.game.common.kit.ClassScanner;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * BarSkeletonBuilderParamConfig 构建参数的配置
 * <pre>
 *     设置一些参数
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-02-01
 */
@Setter
@Getter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BarSkeletonBuilderParamConfig {
    /** action controller class. class has @ActionController */
    final List<Class<?>> actionControllerClassList = new ArrayList<>();
    /** action send class. class has @DocActionSend */
    final List<Class<?>> actionSendClassList = new ArrayList<>();
    /** 错误码 class */
    final List<MsgExceptionInfo> msgExceptionInfoList = new ArrayList<>();

    /** true 打印广播日志，默认不打印 */
    boolean broadcastLog ;

    /** ActionController filter */
    Predicate<Class<?>> actionControllerPredicate = clazz -> Objects.nonNull(clazz.getAnnotation(ActionController.class));
    /** 推送相关的 class */
    Predicate<Class<?>> actionSendPredicate = clazz -> Objects.nonNull(clazz.getAnnotation(DocActionSends.class));

    /**
     * 创建业务框架构建器
     *
     * @return 业务框架构建器
     */
    public BarSkeletonBuilder createBuilder() {
        // 错误码-用于文档的生成
        this.addErrorCode(ActionErrorEnum.values());

        // 业务框架构建器
        BarSkeletonBuilder builder = BarSkeleton.newBuilder();

        // action send class. class has @DocActionSend
        this.scanClassActionSend(builder::addActionSend);

        // action controller class. class has @ActionController
        this.scanClassActionController(builder::addActionController);

        // 错误码相关的
        this.getMsgExceptionInfoList().forEach(builder::addMsgExceptionInfo);

        // true 打印广播日志，默认不打印
        DevConfig.me().broadcastLog = this.broadcastLog;

        return builder;
    }

    /**
     * 业务 action 类
     * <pre>
     *     需要扫描的类
     *     内部会扫描当前类路径和子包路径 下的所有类
     *     类需要是 @ActionController 注解的
     * </pre>
     *
     * @param actionControllerClass actionControllerClass
     * @return this
     */
    public BarSkeletonBuilderParamConfig addActionController(Class<?> actionControllerClass) {
        this.actionControllerClassList.add(actionControllerClass);
        return this;
    }

    /**
     * 推送消息的文档
     * <pre>
     *     需要扫描的类
     *     内部会扫描当前类路径和子包路径
     *     类需要是 @DocActionSends 注解的
     * </pre>
     *
     * @param actionSendClass actionSendClass
     * @return this
     */
    public BarSkeletonBuilderParamConfig addActionSend(Class<?> actionSendClass) {
        this.actionSendClassList.add(actionSendClass);
        return this;
    }

    /**
     * 错误码-用于文档的生成
     *
     * @param msgExceptionInfoArray msgExceptionInfoArray
     * @return this
     */
    public BarSkeletonBuilderParamConfig addErrorCode(MsgExceptionInfo[] msgExceptionInfoArray) {
        msgExceptionInfoList.addAll(Arrays.asList(msgExceptionInfoArray));
        return this;
    }

    /**
     * 扫描 actionControllerClassList 并把扫描好的类交给 actionConsumer 消费
     *
     * @param actionConsumer 消费者
     */
    public void scanClassActionController(Consumer<Class<?>> actionConsumer) {
        // action send class. class has @DocActionSend
        scanClass(this.actionControllerClassList, this.actionControllerPredicate, actionConsumer);
    }

    /**
     * 扫描 actionSendClassList 并把扫描好的类交给 sendConsumer 消费
     *
     * @param sendConsumer 消费者
     */
    public void scanClassActionSend(Consumer<Class<?>> sendConsumer) {
        // action controller class. class has @ActionController
        scanClass(this.actionSendClassList, this.actionSendPredicate, sendConsumer);
    }

    private void scanClass(final List<Class<?>> actionList
            , final Predicate<Class<?>> predicateFilter
            , final Consumer<Class<?>> actionConsumer) {

        for (Class<?> actionClazz : actionList) {
            // 扫描
            String packagePath = actionClazz.getPackageName();
            ClassScanner classScanner = new ClassScanner(packagePath, predicateFilter);
            List<Class<?>> classList = classScanner.listScan();

            // 将扫描好的 class 添加到业务框架中
            classList.forEach(actionConsumer);
        }
    }

}

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

import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.DocActionSends;
import com.iohao.game.action.skeleton.core.enhance.BarSkeletonBuilderEnhances;
import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.core.exception.MsgExceptionInfo;
import com.iohao.game.action.skeleton.toy.IoGameBanner;
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
public final class BarSkeletonBuilderParamConfig {
    /** action controller class. class has @ActionController */
    final List<Class<?>> actionControllerClassList = new ArrayList<>();
    /** action send class. class has @DocActionSend */
    final List<Class<?>> actionSendClassList = new ArrayList<>();
    /** 错误码 class */
    final List<MsgExceptionInfo> msgExceptionInfoList = new ArrayList<>();

    /** true 打印广播日志，默认不打印 */
    boolean broadcastLog;

    /** ActionController filter */
    Predicate<Class<?>> actionControllerPredicate = clazz -> Objects.nonNull(clazz.getAnnotation(ActionController.class));
    /** 推送相关的 class */
    Predicate<Class<?>> actionSendPredicate = clazz -> Objects.nonNull(clazz.getAnnotation(DocActionSends.class));
    boolean enhance = true;

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
        enhance(builder);

        // action send class. class has @DocActionSend
        this.scanClassActionSend(builder::addActionSend);

        // action controller class. class has @ActionController
        this.scanClassActionController(builder::addActionController);

        // 错误码相关的
        this.getMsgExceptionInfoList().forEach(builder::addMsgExceptionInfo);

        // true 打印广播日志，默认不打印
        DevConfig.broadcastLog = this.broadcastLog;

        extracted();

        return builder;
    }

    /**
     * 业务 action 类
     * <pre>
     *     需要扫描的类
     *     内部会扫描当前类路径和子包路径 下的所有类
     *     类需要是 @ActionController 注解的
     * </pre>
     * <pre>
     *     因为名字的原因，会给开发者带来理解上的混乱，因此将方法标记为过时的，
     *     请使用 {@link BarSkeletonBuilderParamConfig#scanActionPackage(Class)} 方法代替，
     *     将在下个大版本中移除
     * </pre>
     *
     * @param actionControllerClass actionControllerClass
     * @return this
     */
    @Deprecated
    public BarSkeletonBuilderParamConfig addActionController(Class<?> actionControllerClass) {
        this.actionControllerClassList.add(actionControllerClass);
        return this;
    }

    /**
     * 扫描 action 类所在包
     * <pre>
     *     内部会扫描当前 acton 类的路径和子包路径下的所有类
     *     类需要是 @ActionController 注解的
     * </pre>
     *
     * @param actionControllerClass action 类
     * @return this
     */
    public BarSkeletonBuilderParamConfig scanActionPackage(Class<?> actionControllerClass) {
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
     * <pre>
     *     因为名字的原因，会给开发者带来理解上的混乱，因此将方法标记为过时的，
     *     请使用 {@link BarSkeletonBuilderParamConfig#scanActionSendPackage(Class)} 方法代替，
     *     将在下个大版本中移除
     * </pre>
     *
     * @param actionSendClass actionSendClass
     * @return this
     */
    @Deprecated
    public BarSkeletonBuilderParamConfig addActionSend(Class<?> actionSendClass) {
        this.actionSendClassList.add(actionSendClass);
        return this;
    }

    /**
     * 扫描 action 推送类所在包，用于推送文档的生成
     * <pre>
     *     内部会扫描当前 action 推送类的路径和子包路径下的所有类
     *     类需要是 @DocActionSends 注解的
     * </pre>
     *
     * @param actionSendClass action 推送类
     * @return this
     */
    public BarSkeletonBuilderParamConfig scanActionSendPackage(Class<?> actionSendClass) {
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


    private void enhance(BarSkeletonBuilder builder) {
        if (this.enhance) {
            BarSkeletonBuilderEnhances.enhance(builder);
        }
    }

    /**
     * 扫描 actionControllerClassList 并把扫描好的类交给 actionConsumer 消费
     *
     * @param actionConsumer 消费者
     */
    private void scanClassActionController(Consumer<Class<?>> actionConsumer) {
        // action send class. class has @DocActionSend
        scanClass(this.actionControllerClassList, this.actionControllerPredicate, actionConsumer);
    }

    /**
     * 扫描 actionSendClassList 并把扫描好的类交给 sendConsumer 消费
     *
     * @param sendConsumer 消费者
     */
    private void scanClassActionSend(Consumer<Class<?>> sendConsumer) {
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

    private static void extracted() {
        if (IoGameBanner.flag45 != 1) {
        }
    }
}

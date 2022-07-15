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


import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import com.iohao.game.action.skeleton.core.doc.ActionCommandDoc;
import com.iohao.game.action.skeleton.core.doc.ActionCommandDocKit;
import com.iohao.game.action.skeleton.core.doc.JavaClassDocInfo;
import com.iohao.game.action.skeleton.core.flow.parser.MethodParsers;
import com.iohao.game.action.skeleton.ext.spring.ActionFactoryBeanForSpring;
import com.iohao.game.common.kit.StrKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * action命令对象的映射管理, 目前并没有做重复映射检测
 * <pre>
 *     实现方式是获取需要对外公开的 tcpActionController
 *     然后获取public void的方法,将这类型的方法封装成独立命令, 在与map进行一个简单的映射关系
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ActionCommandInfoBuilder {
    /** 命令域 管理器 */
    @Getter
    final ActionCommandRegions actionCommandRegions = new ActionCommandRegions();

    final BarSkeletonSetting setting;

    ActionCommandInfoBuilder(BarSkeletonSetting setting) {
        this.setting = setting;
    }

    /**
     * 构建映射
     * <pre>
     *     这个方法可以做一个抽象接口
     *     让接口子类来处理如何生成 Map<Integer, Map<Integer, ActionCommand>> 的数据
     *     但现在不着急; 2021-12-21
     * </pre>
     *
     * @param controllerList action 类列表
     */
    ActionCommandInfoBuilder buildAction(List<Class<?>> controllerList) {
        // java source
        Map<String, JavaClassDocInfo> javaClassDocInfoMap = ActionCommandDocKit.getJavaClassDocInfoMap(controllerList);

        // 条件: 类上配置了 ActionController 注解
        Predicate<Class<?>> controllerPredicate = controllerClazz -> Objects.nonNull(controllerClazz.getAnnotation(ActionController.class));

        Set<Class<?>> controllerSet = new HashSet<>(controllerList);
        controllerSet.stream().filter(controllerPredicate).forEach(controllerClazz -> {
            // 方法访问器: 获取类中自己定义的方法
            var methodAccess = MethodAccess.get(controllerClazz);
            var constructorAccess = ConstructorAccess.get(controllerClazz);

            // 主路由 (类上的路由)
            int cmd = controllerClazz.getAnnotation(ActionController.class).value();
            // 子路由 map
            var actionCommandRegion = this.actionCommandRegions.getActionCommandRegion(cmd);

            // true 表示交付给容器来管理 如 spring 等
            boolean deliveryContainer = this.deliveryContainer(controllerClazz);

            JavaClassDocInfo javaClassDocInfo = javaClassDocInfoMap.get(controllerClazz.toString());

            // 遍历所有方法上有 ActionMethod 注解的方法对象
            this.getMethodStream(controllerClazz).forEach(method -> {

                ActionCommandDoc actionCommandDoc = getActionCommandDoc(javaClassDocInfo, method);

                // 目标子路由 (方法上的路由)
                int subCmd = method.getAnnotation(ActionMethod.class).value();
                // 方法名
                String methodName = method.getName();
                // 方法下标
                int methodIndex = methodAccess.getIndex(methodName);
                // 方法返回值类型
                Class<?> returnType = methodAccess.getReturnTypes()[methodIndex];

                // 新建一个命令构建器
                var builder = new ActionCommand.Builder()
                        .setCmd(cmd)
                        .setSubCmd(subCmd)
                        .setActionControllerClazz(controllerClazz)
                        .setActionControllerConstructorAccess(constructorAccess)
                        .setActionMethod(method)
                        .setActionMethodName(methodName)
                        .setActionMethodIndex(methodIndex)
                        .setActionMethodAccess(methodAccess)
                        .setReturnTypeClazz(returnType)
                        .setActionCommandDoc(actionCommandDoc)
                        .setDeliveryContainer(deliveryContainer);

                // 检测路由是否重复
                checkExistSubCmd(controllerClazz, subCmd, actionCommandRegion);

                // 方法参数信息
                paramInfo(method, builder);

                // JSR
                ValidatorKit.buildValidator(this.setting, builder);

                /*
                 * 路由key，根据这个路由可以找到对应的 command（命令对象）
                 * 将映射类的方法，保存在 command 中。每个command封装成一个命令对象。
                 */
                var command = builder.build(this.setting);

                checkParamResultInfo(command);

                // 子路由映射
                actionCommandRegion.add(command);
            });

        });

        // 初始化 命令数组
        actionCommandRegions.initActionCommandArray(setting);

        return this;
    }

    private boolean deliveryContainer(Class<?> controllerClazz) {

        if (ActionFactoryBeanForSpring.me().isSpring()) {
            return controllerClazz.getAnnotation(Component.class) != null;
        }

        return false;
    }

    private ActionCommandDoc getActionCommandDoc(JavaClassDocInfo javaClassDocInfo, Method method) {
        if (javaClassDocInfo != null) {
            return javaClassDocInfo.createActionCommandDoc(method);
        }

        return new ActionCommandDoc();
    }


    private void paramInfo(Method method, ActionCommand.Builder builder) {

        // 方法参数列表
        var parameters = method.getParameters();
        if (Objects.isNull(parameters)) {
            return;
        }

        var paramInfos = new ActionCommand.ParamInfo[parameters.length];
        builder.setParamInfos(paramInfos);

        for (int i = 0; i < parameters.length; i++) {
            // 方法的参数对象
            Parameter parameter = parameters[i];
            // 构建参数信息
            paramInfos[i] = new ActionCommand.ParamInfo(i, parameter);
        }
    }

    private void checkExistSubCmd(Class<?> controllerClass, int subCmd, ActionCommandRegion actionCommandRegion) {

        if (actionCommandRegion.containsKey(subCmd)) {

            String message = StrKit.format("cmd:【{}】下已经存在方法编号 subCmd:【{}】 .请查看: {}",
                    actionCommandRegion.cmd,
                    subCmd,
                    controllerClass);

            throw new RuntimeException(message);
        }
    }

    /**
     * 查找类的所有方法
     * <pre>
     *     返回方法上带有 ActionMethod 的方法对象
     *
     *     不返回: 静态方法, 私有方法
     * </pre>
     *
     * @param actionControllerClass 类
     * @return 方法对象列表
     */
    private Stream<Method> getMethodStream(Class<?> actionControllerClass) {
        return Arrays.stream(actionControllerClass.getDeclaredMethods())
                .filter(method -> {
                    int mod = method.getModifiers();
                    // 静态方法 与 私有方法不做处理
                    if (Modifier.isStatic(mod) || Modifier.isPrivate(mod)) {
                        return false;
                    }

                    // 必须实现注解的方法才处理
                    return Objects.nonNull(method.getAnnotation(ActionMethod.class));
                });
    }

    private void checkParamResultInfo(ActionCommand actionCommand) {
        ActionCommand.ParamInfo[] paramInfos = actionCommand.getParamInfos();
        for (ActionCommand.ParamInfo paramInfo : paramInfos) {
            checkMethodParamResultInfo(paramInfo);
        }

        ActionCommand.ActionMethodReturnInfo actionMethodReturnInfo = actionCommand.getActionMethodReturnInfo();
        checkMethodParamResultInfo(actionMethodReturnInfo);
    }

    private void checkMethodParamResultInfo(ActionCommand.MethodParamResultInfo methodParamResultInfo) {

        if (!methodParamResultInfo.isList()) {
            return;
        }

        // 如果是 List 那么只支持基础类型
        Class<?> actualTypeArgumentClazz = methodParamResultInfo.getActualTypeArgumentClazz();
        boolean result = MethodParsers.me().containsKey(actualTypeArgumentClazz);

        if (!result) {
            Set<Class<?>> keySet = MethodParsers.me().keySet();
            throw new RuntimeException("List 中的泛型类型只能是基础类型，如 " + keySet);
        }
    }
}

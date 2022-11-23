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
import com.iohao.game.common.kit.StrKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * action 命令对象解析器，将 action 类下的业务方法解析为 actionCommand
 * <pre>
 *     解析后的 actionCommand 将存放到对应的 ActionCommandRegions 中
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
final class ActionCommandParser {
    /** 命令域 管理器 */
    @Getter
    final ActionCommandRegions actionCommandRegions = new ActionCommandRegions();

    final BarSkeletonSetting setting;

    ActionCommandParser(BarSkeletonSetting setting) {
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
    ActionCommandParser buildAction(List<Class<?>> controllerList) {
        // java source
        Map<String, JavaClassDocInfo> javaClassDocInfoMap = ActionCommandDocKit.getJavaClassDocInfoMap(controllerList);

        // 条件: 类上配置了 ActionController 注解
        Predicate<Class<?>> controllerPredicate = controllerClazz -> Objects.nonNull(controllerClazz.getAnnotation(ActionController.class));

        Set<Class<?>> controllerSet = new HashSet<>(controllerList);
        controllerSet.stream().filter(controllerPredicate).forEach(controllerClazz -> {
            // true 表示交付给容器来管理 如 spring 等
            boolean deliveryContainer = this.deliveryContainer(controllerClazz);

            JavaClassDocInfo javaClassDocInfo = javaClassDocInfoMap.get(controllerClazz.toString());

            // 方法访问器: 获取类中自己定义的方法
            var methodAccess = MethodAccess.get(controllerClazz);
            var constructorAccess = ConstructorAccess.get(controllerClazz);

            // 主路由 (类上的路由)
            int cmd = controllerClazz.getAnnotation(ActionController.class).value();
            // 子路由 map
            var actionCommandRegion = this.actionCommandRegions.getActionCommandRegion(cmd);
            actionCommandRegion.setActionControllerClazz(controllerClazz);
            actionCommandRegion.setJavaClassDocInfo(javaClassDocInfo);

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

        // 内部将所有的 action 转换为 action 二维数组
        actionCommandRegions.initActionCommandArray(setting);

        return this;
    }

    private boolean deliveryContainer(Class<?> controllerClazz) {

        if (DependencyInjectionPart.me().isInjection()) {
            return DependencyInjectionPart.me().deliveryContainer(controllerClazz);
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
            var paramInfo = new ActionCommand.ParamInfo(i, parameter);
            paramInfos[i] = paramInfo;

            /*
             * 下面是 JSR380 相关的逻辑
             *
             * 1 没开启 JSR380 验证， 不做处理
             * 2 过滤不需要验证的参数
             */
            if (!this.setting.validator || paramInfo.isExtension()) {
                continue;
            }

            paramInfo.validator = ValidatorKit.isValidator(parameter.getType());

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
     * 得到 action 类中标准 action 的 method stream
     * <pre>
     *     返回方法上带有 ActionMethod 的方法对象
     *     术语解释：在 action 类中提供的业务方法通常称为 action
     *
     *     当前标准 action 映射规则
     *     1. 业务方法上必需添加注解 ActionMethod
     *     2. 业务方法不能是：static、protected、private。
     *     简单的说就是标准 action 应该是非静态的、访问权限不能是 protected、private。
     *
     *     访问权限私有方法是 ioGame 业务框架中保留使用方式，
     *     比如将来有可能将声明为 private 的业务方法，即 private action ，
     *     私有 action 只能是内部逻辑服访问的。
     *     可以简单的理解为 private action 是给逻辑服之间提供访问的，
     *     这样开发者可以不需要在游戏对外服中做访问权限的控制。
     *     但这样可能会给开发者带来使用上的混淆，所以短期内不会提供这样的使用方式；
     *
     *
     *     其他访问权限说明：
     *     关于 public default 访问权限对 action 的限制说明。
     *
     *     首先 public action 对于每个开发者都是能看后明白的。
     *
     *     而 default action 权限，是个人比较喜欢的方式，因为这样的代码更简洁。
     *     当然，这一点对于部分开发者来说，可能是难以接受的。
     *     所以在做文档与示例时，基本都会明确是 public action 的使用方式。
     *
     *     至于个人为为比较喜欢 default 访问权限，
     *     且在示例与文档中的 DTO、PB、业务数据载体等，基本都是没有在代码中做显示的权限的声明，
     *     细心的朋友会发现，ioGame 中大部分的类是使用了 lombok 的 @FieldDefaults(level = AccessLevel.XXX) 注解的，
     *     是因为这样可以使得代码更加的简洁，从而在阅读代码时也更加的清晰。
     * </pre>
     *
     * @param actionControllerClass 类
     * @return 标准 action 方法对象 Stream
     */
    private Stream<Method> getMethodStream(Class<?> actionControllerClass) {
        return Arrays
                // 得到 action 类的所有方法
                .stream(actionControllerClass.getDeclaredMethods())
                // 得到在业务方法上添加了 ActionMethod 注解的方法对象
                .filter(method -> Objects.nonNull(method.getAnnotation(ActionMethod.class)))
                // 不能是静态方法的，访问权限不能是 protected private 的
                .filter(method -> {
                    int mod = method.getModifiers();

                    if (Modifier.isStatic(mod) || Modifier.isProtected(mod) || Modifier.isPrivate(mod)) {
                        // 将来这里可以做无效 action 警告日志，但现在不着急。
                        return false;
                    }

                    return true;
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

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


import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import com.iohao.game.action.skeleton.core.doc.ActionCommandDoc;
import com.iohao.game.action.skeleton.core.doc.ActionDoc;
import com.iohao.game.action.skeleton.core.doc.ActionDocs;
import com.iohao.game.common.kit.StrKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;
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
     *     让接口子类来处理如何生成 ActionCommand 的数据
     *     但现在不着急; 2021-12-21
     * </pre>
     *
     * @param controllerList action 类的 class 列表
     */
    ActionCommandParser buildAction(List<Class<?>> controllerList) {

        var doc = new ActionCommandDocParser(this, controllerList, setting.parseDoc);

        // action 类的 stream
        this.getActionControllerStream(controllerList).forEach(controllerClazz -> {
            // 方法访问器: 获取类中自己定义的方法
            var methodAccess = MethodAccess.get(controllerClazz);
            var constructorAccess = ConstructorAccess.get(controllerClazz);

            // 主路由 (类上的路由)
            int cmd = controllerClazz.getAnnotation(ActionController.class).value();
            // 子路由 map
            var actionCommandRegion = this.actionCommandRegions.getActionCommandRegion(cmd);

            // true 表示交付给容器来管理 如 spring 等
            boolean deliveryContainer = this.deliveryContainer(controllerClazz);
            // action 类的实例化对象
            Object actionControllerInstance = ofActionInstance(controllerClazz);

            // 遍历所有方法上有 ActionMethod 注解的方法对象
            this.getMethodStream(controllerClazz).forEach(method -> {

                // 目标子路由 (方法上的路由)
                int subCmd = method.getAnnotation(ActionMethod.class).value();
                // 方法名
                String methodName = method.getName();
                // 方法下标
                int methodIndex = methodAccess.getIndex(methodName);
                // 方法返回值类型
                Class<?> returnType = methodAccess.getReturnTypes()[methodIndex];
                // source doc
                ActionCommandDoc actionCommandDoc = doc.getActionCommandDoc(cmd, subCmd);

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
                        .setDeliveryContainer(deliveryContainer)
                        .setCreateSingleActionCommandController(this.setting.createSingleActionCommandController)
                        .setActionController(actionControllerInstance);

                // 检测路由是否重复
                checkExistSubCmd(controllerClazz, subCmd, actionCommandRegion);

                // 方法参数信息
                paramInfo(method, builder);

                /*
                 * 路由 key，根据这个路由可以找到对应的 command（命令对象）
                 * 将映射类的方法，保存在 command 中，每个 command 封装成一个命令对象。
                 */
                var command = builder.build();

                // 子路由映射
                actionCommandRegion.add(command);

                ActionDoc actionDoc = ActionDocs.ofActionDoc(cmd, controllerClazz);
                actionDoc.addActionCommand(command);
            });

        });

        // 内部将所有的 action 转换为 action 二维数组
        actionCommandRegions.initActionCommandArray(setting);

        return this;
    }

    Stream<Class<?>> getActionControllerStream(List<Class<?>> controllerList) {
        Set<Class<?>> controllerSet = new HashSet<>(controllerList);
        // action 类的 stream
        return controllerSet.stream()
                // 条件: 类上配置了 ActionController 注解
                .filter(clazz -> Objects.nonNull(clazz.getAnnotation(ActionController.class)));
    }

    /**
     * 得到 action 类中标准 action 的 method stream
     * <pre>
     *     返回方法上带有 ActionMethod 的方法对象
     *     术语解释：在 action 类中提供的业务方法通常称为 action
     *
     *     当前标准 action 映射规则
     *     1. 业务方法上添加注解 ActionMethod
     *     2. 业务方法的访问权限必须是：public
     *     3. 业务方法不能是：static
     *     4. 业务方法需要是在 action 类中声明的方法
     *     简单的说，标准的 action 是非静态的，且访问权限为 public 的方法。
     *     术语说明：在 action 类中提供的业务方法通常称为 action。
     *
     *     其他访问权限方法是 ioGame 业务框架中保留使用方式，
     *     比如将来有可能将声明为 private 的业务方法，即 private action ，
     *     私有 action 只能是内部逻辑服访问的。
     *     可以简单的理解为 private action 是给逻辑服之间提供访问的，
     *     这样开发者可以不需要在游戏对外服中做访问权限的控制。
     *     但这样可能会给开发者带来使用上的混淆，所以短期内不会提供这样的使用方式；
     * </pre>
     *
     * @param actionControllerClass 类
     * @return 标准 action 方法对象 Stream
     */
    Stream<Method> getMethodStream(Class<?> actionControllerClass) {
        return Arrays
                // 得到 action 类的所有方法
                .stream(actionControllerClass.getDeclaredMethods())
                // 得到在业务方法上添加了 ActionMethod 注解的方法对象
                .filter(method -> Objects.nonNull(method.getAnnotation(ActionMethod.class)))
                // 访问权限必须是 public 的
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                // 不能是静态方法的
                .filter(method -> !Modifier.isStatic(method.getModifiers()));
    }

    private boolean deliveryContainer(Class<?> controllerClazz) {

        if (DependencyInjectionPart.me().isInjection()) {
            return DependencyInjectionPart.me().deliveryContainer(controllerClazz);
        }

        return false;
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

    private Object ofActionInstance(Class<?> controllerClazz) {
        // 如果 actionController 交给容器管理了，就从容器中获取实例，否则就 newInstance
        boolean deliveryContainer = this.deliveryContainer(controllerClazz);

        var actionInstance = deliveryContainer
                ? DependencyInjectionPart.me().getBean(controllerClazz)
                : ConstructorAccess.get(controllerClazz).newInstance();

        // 正常来说不会为 null，除非是开发者在集成其他容器时，没有实现 getBean 方法
        Objects.requireNonNull(actionInstance);

        return actionInstance;
    }
}

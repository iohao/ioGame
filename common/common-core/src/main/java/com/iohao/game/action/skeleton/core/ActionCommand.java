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
import com.iohao.game.action.skeleton.annotation.ValidatedGroup;
import com.iohao.game.action.skeleton.core.doc.ActionCommandDoc;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.parser.MethodParser;
import com.iohao.game.action.skeleton.core.flow.parser.MethodParsers;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;

/**
 * ActionCommand 命令对象，也称为 action。
 * <pre>
 *     在业务框架构建时阶段创建的， ActionCommand 中保存了 action 相关信息，如：
 *     路由信息
 *     类信息
 *     类访问器
 *     方法对象
 *     方法名
 *     方法访问器
 *     方法信息下标
 *     方法返回值类型
 *     类是否托管于容器管理（是否交付给容器来管理 如 spring 等）
 * </pre>
 *
 * <pre>
 *     作用：
 *     业务框架处理业务流程时，也就是处理时阶段。会通过 action 来得到想要的各种信息。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
@Getter
public final class ActionCommand {
    /** cmdInfo */
    final CmdInfo cmdInfo;
    /** 构造方法访问器 */
    final ConstructorAccess<?> actionControllerConstructorAccess;
    /** 一个single控制器对象 */
    final Object actionController;
    /** 方法所在 class */
    final Class<?> actionControllerClazz;
    /** 默认:true ，action 对象是 single. 如果设置为 false, 每次创建新的 action 类的对象. */
    final boolean createSingleActionCommandController;

    /** 方法对象 */
    final Method actionMethod;
    /** 方法名 */
    final String actionMethodName;
    /** 方法下标 */
    final int actionMethodIndex;
    /** 方法访问器 */
    final MethodAccess actionMethodAccess;

    /** 方法参数信息 数组 */
    final ParamInfo[] paramInfos;
    /** 方法是否有参数: true 有参数 */
    final boolean methodHasParam;
    /** 方法是否有异常抛出, 一般是错误码: true 有异常 */
    final boolean throwException;
    /** 返回类型 */
    final ActionMethodReturnInfo actionMethodReturnInfo;

    final ActionCommandDoc actionCommandDoc;

    /** true 表示交付给容器来管理 如 spring 等 */
    boolean deliveryContainer;

    private ActionCommand(Builder builder) {
        // -------------- 路由相关 --------------
        this.cmdInfo = CmdInfoFlyweightFactory.of(builder.cmd, builder.subCmd);

        // -------------- 控制器相关 --------------
        this.actionControllerClazz = builder.actionControllerClazz;
        this.actionControllerConstructorAccess = builder.actionControllerConstructorAccess;
        this.actionController = builder.actionController;
        this.createSingleActionCommandController = builder.createSingleActionCommandController;

        // -------------- 控制器-方法相关 --------------
        this.actionMethod = builder.actionMethod;
        this.actionMethodName = builder.actionMethodName;
        this.actionMethodIndex = builder.actionMethodIndex;
        this.actionMethodAccess = builder.actionMethodAccess;

        // -------------- 控制器-方法参数相关 --------------
        this.paramInfos = builder.paramInfos;
        this.methodHasParam = builder.paramInfos != null;
        this.throwException = builder.actionMethod.getExceptionTypes().length != 0;
        this.actionMethodReturnInfo = new ActionMethodReturnInfo(builder);

        this.actionCommandDoc = builder.actionCommandDoc;

        this.deliveryContainer = builder.deliveryContainer;
    }

    /**
     * {@link ActionCommand} 命令的构建器
     * <p>
     * 因为 {@link ActionCommand} 的参数较复杂, 所以这里考虑用构建器。
     */
    @Accessors(chain = true)
    @Setter
    @FieldDefaults(level = AccessLevel.PUBLIC)
    static final class Builder {
        /** 目标路由 */
        int cmd;
        /** 目标子路由 */
        int subCmd;
        /** 方法访问器 */
        MethodAccess actionMethodAccess;
        /** 类访问器 */
        ConstructorAccess<?> actionControllerConstructorAccess;
        /** 方法名 */
        String actionMethodName;
        /** tcp controller类 */
        Class<?> actionControllerClazz;
        /** 方法对象 */
        Method actionMethod;
        /** 参数列表信息 */
        ActionCommand.ParamInfo[] paramInfos;
        /** 方法下标 (配合 MethodAccess 使用) */
        int actionMethodIndex;
        /** 返回值信息 */
        Class<?> returnTypeClazz;
        /** action command 文档 */
        ActionCommandDoc actionCommandDoc;
        /** true 表示交付给容器来管理 如 spring 等 */
        boolean deliveryContainer;
        /** 默认:true ，action 对象是 single. 如果设置为 false, 每次创建新的 action 类的对象. */
        boolean createSingleActionCommandController;
        /** 一个single控制器对象 */
        Object actionController;

        ActionCommand build() {
            return new ActionCommand(this);
        }
    }

    /**
     * action 方法中的参数与返回值信息
     */
    public interface MethodParamResultInfo {
        /**
         * 是否是 List 类型
         *
         * @return true 是 List 类型
         */
        default boolean isList() {
            return false;
        }

        /**
         * List 泛型的类型，也称为方法返回值类型
         * <pre>
         *     如果不是方法的返回值不是 List 类型，这个值会取自 paramClazz 成员属性
         *     原计划想用 Collection ，这样可以兼容 Set 之类的；但似乎这样有一点争议，先暂支持 List 把
         * </pre>
         *
         * @return List 泛型的类型
         */
        Class<?> getActualTypeArgumentClazz();
    }

    /**
     * 方法参数信息
     */
    @Getter
    public static final class ParamInfo implements MethodParamResultInfo {
        /** JSR380 空验证组 */
        static final Class<?>[] EMPTY_GROUPS = new Class<?>[0];
        /** 参数名 */
        final String name;
        /** 参数下标 */
        final int index;
        /** 保存 Parameter 对象 */
        final Parameter parameter;
        /** 参数类型 */
        final Class<?> paramClazz;
        /**
         * List 泛型的类型，也称为方法返回值类型
         * <pre>
         *     如果方法的返回值不是 List 类型，这个值会取自 paramClazz 成员属性
         *     原计划想用 Collection ，这样可以兼容 Set 之类的；但似乎这样有一点争议，先暂支持 List
         * </pre>
         */
        final Class<?> actualTypeArgumentClazz;
        /** true 是 list 类型 */
        final boolean list;

        /**
         * 实际的内置包装类型
         * <pre>
         *     常规的 java class 是指本身，如：
         *     开发者自定义了一个 StudentPb，在 action 方法上参数声明为 xxx(StudentPb studentPb)
         *     那么这个值就是 StudentPb
         * </pre>
         *
         * <pre>
         *     但由于框架现在内置了一些包装类型，如：
         *     int --> IntValue
         *     List&lt;Integer&gt; --> IntValueList
         *
         *     long --> LongValue
         *     List&lt;Long&gt; --> LongValueList
         *
         *     所以当开发者在 action 方法上参数声明为基础类型时；
         *     如声明为 int 那么这个值将会是 IntValue
         *     如声明为 long 那么这个值将会是 LongValue
         *
         *     如声明为 List&lt;Integer&gt; 那么这个值将会是 IntValueList
         *     包装类型相关的以此类推;
         *
         *     这么做的目的是为了生成文档时，不与前端产生争议，
         *     如果提供给前端的文档显示 int ，或许前端同学会懵B，
         *     当然如果提前与前端同学沟通好这些约定，也不是那么麻烦。
         *     但实际上如果前端是新来接手项目的，碰见这种情况也会小懵，
         *     所以为了避免这些小尬，框架在生成文档时，用基础类型的内置包装类型来表示。
         * </pre>
         */
        final Class<?> actualClazz;
        final boolean extension;
        final boolean customMethodParser;
        /** JSR380 验证组 */
        final Class<?>[] validatorGroups;
        /** true : 开启 JSR380 验证规范 */
        boolean validator;

        ParamInfo(int index, Parameter p) {
            // 保存Parameter对象
            this.parameter = p;
            // 方法的参数下标
            this.index = index;
            // 方法的参数名
            this.name = p.getName();
            // 方法的参数类型 class
            this.paramClazz = p.getType();

            /*
             * 关于方法参数支持 list 这个特性也纠结了很久
             * 因为这可能会给开发者造成一些困惑，现在方法支持 list 但只是为了支持基础类型相关的 list
             * 开发者会不会把这个 list 的泛型类型用在协议上，如: List<StudentPb> 这种；
             */
            if (List.class.isAssignableFrom(this.paramClazz)) {
                ParameterizedType genericReturnType = (ParameterizedType) p.getParameterizedType();
                this.actualTypeArgumentClazz = (Class<?>) genericReturnType.getActualTypeArguments()[0];
                this.list = true;
            } else {
                this.actualTypeArgumentClazz = this.paramClazz;
                this.list = false;
            }

            MethodParser methodParser = MethodParsers.getMethodParser(this);
            this.actualClazz = methodParser.getActualClazz(this);
            this.customMethodParser = methodParser.isCustomMethodParser();

            // JSR380 相关，确定验证组，校验组对象的 Class 数组
            var validatedAnn = this.parameter.getAnnotation(ValidatedGroup.class);
            this.validatorGroups = Objects.isNull(validatedAnn) ? EMPTY_GROUPS : validatedAnn.value();

            this.extension = FlowContext.class.isAssignableFrom(paramClazz);
        }

        /**
         * 废弃的方法，请使用 toString 代替
         *
         * @return name
         */
        @Deprecated
        public String toStringShort() {
            return actualClazz.getSimpleName() + " " + name;
        }

        @Override
        public String toString() {
            return this.toString(false);
        }

        /**
         * 是否扩展属性
         *
         * @return true 是扩展属性
         */
        public boolean isExtension() {
            return extension;
        }

        /**
         * 废弃的方法
         *
         * @return name
         */
        @Deprecated
        public String getMethodParamClassName() {
            if (this.isCustomMethodParser() || MethodParsers.containsKey(this.actualClazz)) {
                return this.actualClazz.getSimpleName();
            }

            return this.actualClazz.getName();
        }

        public String toString(boolean fullName) {
            boolean isCustomList = this.list && !MethodParsers.containsKey(this.actualClazz);

            if (isCustomList) {
                String simpleNameParamClazz = this.paramClazz.getSimpleName();
                String simpleNameActualClazz = fullName
                        ? this.actualClazz.getName()
                        : this.actualClazz.getSimpleName();

                return String.format("%s<%s> %s", simpleNameParamClazz, simpleNameActualClazz, this.name);
            }

            String simpleNameActualClazz = fullName
                    ? this.actualClazz.getName()
                    : this.actualClazz.getSimpleName();

            return String.format("%s %s", simpleNameActualClazz, this.name);
        }
    }

    /**
     * action 方法参数返回值信息
     */
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    public static final class ActionMethodReturnInfo implements MethodParamResultInfo {
        /** 返回类型 */
        final Class<?> returnTypeClazz;
        /**
         * List 泛型的类型，也称为方法返回值类型
         * <pre>
         *     如果不是方法的返回值不是 List 类型，这个值会取自 returnTypeClazz 成员属性
         *     原计划想用 Collection ，这样可以兼容 Set 之类的；但似乎这样有一点争议，先暂支持 List 把
         * </pre>
         */
        final Class<?> actualTypeArgumentClazz;
        /** true 是 list 类型 */
        final boolean list;
        /**
         * 实际的内置包装类型
         * <pre>
         *     常规的 java class 是指本身，如：
         *     开发者自定义了一个 StudentPb，在 action 方法上参数声明为 xxx(StudentPb studentPb)
         *     那么这个值就是 StudentPb
         * </pre>
         *
         * <pre>
         *     但由于框架现在内置了一些包装类型，如：
         *     int --> IntValue
         *     List&lt;Integer&gt; --> IntValueList
         *
         *     long --> LongValue
         *     List&lt;Long&gt; --> LongValueList
         *
         *     所以当开发者在 action 方法上参数声明为基础类型时；
         *     如声明为 int 那么这个值将会是 IntValue
         *     如声明为 long 那么这个值将会是 LongValue
         *
         *     如声明为 List&lt;Integer&gt; 那么这个值将会是 IntValueList
         *     包装类型相关的以此类推;
         *
         *     这么做的目的是为了生成文档时，不与前端产生争议，
         *     如果提供给前端的文档显示 int ，或许前端同学会懵B，
         *     当然如果提前与前端同学沟通好这些约定，也不是那么麻烦。
         *     但实际上如果前端是新来接手项目的，碰见这种情况也会小懵，
         *     所以为了避免这些小尬，框架在生成文档时，用基础类型的内置包装类型来表示。
         * </pre>
         */
        final Class<?> actualClazz;
        final boolean customMethodParser;

        private ActionMethodReturnInfo(ActionCommand.Builder builder) {

            this.returnTypeClazz = builder.returnTypeClazz;

            if (List.class.isAssignableFrom(returnTypeClazz)) {
                ParameterizedType genericReturnType = (ParameterizedType) builder.actionMethod.getGenericReturnType();
                this.actualTypeArgumentClazz = (Class<?>) genericReturnType.getActualTypeArguments()[0];
                this.list = true;
            } else {
                this.actualTypeArgumentClazz = returnTypeClazz;
                this.list = false;
            }

            MethodParser methodParser = MethodParsers.getMethodParser(this);
            this.actualClazz = methodParser.getActualClazz(this);
            this.customMethodParser = methodParser.isCustomMethodParser();
        }

        /**
         * 方法返回值类型是否 void
         *
         * @return true 是 void
         */
        public boolean isVoid() {
            return Void.TYPE == this.returnTypeClazz;
        }

        /**
         * 废弃，没什么作用了
         *
         * @return name
         */
        @Deprecated
        public String getReturnTypeClazzName() {
            if (this.isCustomMethodParser() || MethodParsers.containsKey(this.actualClazz)) {
                return this.actualClazz.getSimpleName();
            }

            return this.actualClazz.getName();
        }

        @Override
        public String toString() {
            return toString(false);
        }

        public String toString(boolean fullName) {
            boolean isCustomList = this.list && !MethodParsers.containsKey(this.actualClazz);

            if (isCustomList) {
                String simpleNameReturnTypeClazz = this.returnTypeClazz.getSimpleName();
                String simpleNameActualClazz = fullName
                        ? this.actualClazz.getName()
                        : this.actualClazz.getSimpleName();

                return String.format("%s<%s>", simpleNameReturnTypeClazz, simpleNameActualClazz);
            }

            return fullName
                    ? this.actualClazz.getName()
                    : this.actualClazz.getSimpleName();
        }
    }
}

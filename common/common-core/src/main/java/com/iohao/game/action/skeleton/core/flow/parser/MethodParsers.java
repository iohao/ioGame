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
package com.iohao.game.action.skeleton.core.flow.parser;

import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.protocol.wrapper.*;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 关于业务框架中，action 参数相关的包装类
 *
 * @author 渔民小镇
 * @date 2022-06-26
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class MethodParsers {
    /**
     * 方法解析器 map
     * <pre>
     *     key : 一般用基础类型
     *     value : 基础类型对应的解析器
     *
     *     如 int 与对应的包装类 Integer 在这里规划为基础类型
     * </pre>
     */
    static final Map<Class<?>, MethodParser> methodParserMap = new HashMap<>();
    static final Map<Class<?>, Supplier<?>> paramSupplierMap = new HashMap<>();

    /** action 业务方法参数的默认解析器 */
    @Setter
    static MethodParser methodParser = DefaultMethodParser.me();

    static {
        init();
    }

    /**
     * 临时兼容
     * <pre>
     *     将在下个大版本移除
     *
     *     此方法是为了兼容 IntPb、IntListPb、LongPb、LongListPb 的用法，
     *     如果你的项目中没有使用上面提到的这几个，是不需要调用此方法的，
     *     如果有使用到上面提到的这几个，请尽快的做相应的替换。
     * </pre>
     */
    @Deprecated
    public static void tempCompatibility() {
        // 表示在 action 参数中，遇见 int 类型的参数，用 IntPbMethodParser 来解析
        mapping(int.class, IntPbMethodParser.me());
        mapping(Integer.class, IntPbMethodParser.me());
        // 表示在 action 参数中，遇见 long 类型的参数，用 LongPbMethodParser 来解析
        mapping(long.class, LongPbMethodParser.me());
        mapping(Long.class, LongPbMethodParser.me());

        mapping(IntPb.class, DefaultMethodParser.me(), IntPb::new);
        mapping(IntListPb.class, DefaultMethodParser.me(), IntListPb::new);

        mapping(LongPb.class, DefaultMethodParser.me(), LongPb::new);
        mapping(LongListPb.class, DefaultMethodParser.me(), LongListPb::new);
    }

    public static void mappingParamSupplier(Class<?> paramClass, Supplier<?> supplier) {
        paramSupplierMap.put(paramClass, supplier);
    }

    public static void mapping(Class<?> paramClass, MethodParser methodParamParser) {
        methodParserMap.put(paramClass, methodParamParser);
    }

    public static MethodParser getMethodParser(ActionCommand.ActionMethodReturnInfo actionMethodReturnInfo) {
        Class<?> methodResultClass = actionMethodReturnInfo.getActualTypeArgumentClazz();
        return getMethodParser(methodResultClass);
    }

    public static MethodParser getMethodParser(ActionCommand.ParamInfo paramInfo) {
        Class<?> actualTypeArgumentClazz = paramInfo.getActualTypeArgumentClazz();
        return getMethodParser(actualTypeArgumentClazz);
    }

    public static MethodParser getMethodParser(Class<?> paramClazz) {
        return methodParserMap.getOrDefault(paramClazz, methodParser);
    }

    public static void clear() {
        methodParserMap.clear();
        paramSupplierMap.clear();
    }

    public static boolean containsKey(Class<?> clazz) {
        return methodParserMap.containsKey(clazz);
    }

    public static Set<Class<?>> keySet() {
        return methodParserMap.keySet();
    }


    private static void init() {
        // 表示在 action 参数中，遇见 int 类型的参数，用 IntValueMethodParser 来解析
        mapping(int.class, IntValueMethodParser.me());
        mapping(Integer.class, IntValueMethodParser.me());

        // 表示在 action 参数中，遇见 long 类型的参数，用 LongValueMethodParser 来解析
        mapping(long.class, LongValueMethodParser.me());
        mapping(Long.class, LongValueMethodParser.me());

        // 表示在 action 参数中，遇见 String 类型的参数，用 StringValueMethodParser 来解析
        mapping(String.class, StringValueMethodParser.me());

        // 表示在 action 参数中，遇见 boolean 类型的参数，用 BoolValueMethodParser 来解析
        mapping(boolean.class, BoolValueMethodParser.me());
        mapping(Boolean.class, BoolValueMethodParser.me());

        /*
         * 这里注册是为了顺便使用 containsKey 方法，因为生成文档的时候要用到短名字
         * 当然也可以使用 instanceof 来做这些，但似乎没有这种方式优雅
         */
        mapping(IntValue.class, DefaultMethodParser.me(), IntValue::new);
        mapping(IntValueList.class, DefaultMethodParser.me(), IntValueList::new);

        mapping(LongValue.class, DefaultMethodParser.me(), LongValue::new);
        mapping(LongValueList.class, DefaultMethodParser.me(), LongValueList::new);

        mapping(BoolValue.class, DefaultMethodParser.me(), BoolValue::new);
        mapping(BoolValueList.class, DefaultMethodParser.me(), BoolValueList::new);

        mapping(StringValue.class, DefaultMethodParser.me(), StringValue::new);
        mapping(StringValueList.class, DefaultMethodParser.me(), StringValueList::new);
    }

    static Object newObject(Class<?> paramClass) {
        if (paramSupplierMap.containsKey(paramClass)) {
            return paramSupplierMap.get(paramClass).get();
        }

        return null;
    }

    private static void mapping(Class<?> paramClass, MethodParser methodParamParser, Supplier<?> supplier) {
        mapping(paramClass, methodParamParser);

        /*
         * 使用原生 pb 如果值为 0，在 jprotobuf 中会出现 nul 的情况，为了避免这个问题
         * 如果业务参数为 null，当解析到对应的类型时，则使用 Supplier 来创建对象
         *
         * 具体使用可参考 DefaultMethodParser
         */
        mappingParamSupplier(paramClass, supplier);
    }

    private MethodParsers() {
    }

    /**
     * 已经标记过期，将在下个大版本移除
     * <pre>
     *     请直接使用静态方法代替； MethodParsers.xxx
     * </pre>
     *
     * @return MethodParsers
     */
    @Deprecated
    public static MethodParsers me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final MethodParsers ME = new MethodParsers();
    }
}

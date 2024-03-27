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
package com.iohao.game.action.skeleton.core.flow.parser;

import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.protocol.wrapper.*;
import lombok.Setter;
import lombok.experimental.UtilityClass;

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
@UtilityClass
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
    final Map<Class<?>, MethodParser> methodParserMap = new HashMap<>();
    final Map<Class<?>, Supplier<?>> paramSupplierMap = new HashMap<>();

    /** action 业务方法参数的默认解析器 */
    @Setter
    MethodParser methodParser = DefaultMethodParser.me();

    static {
        init();
    }

    public void mappingParamSupplier(Class<?> paramClass, Supplier<?> supplier) {
        paramSupplierMap.put(paramClass, supplier);
    }

    public void mapping(Class<?> paramClass, MethodParser methodParamParser) {
        methodParserMap.put(paramClass, methodParamParser);
    }

    public MethodParser getMethodParser(ActionCommand.ActionMethodReturnInfo actionMethodReturnInfo) {
        Class<?> methodResultClass = actionMethodReturnInfo.getActualTypeArgumentClazz();
        return getMethodParser(methodResultClass);
    }

    public MethodParser getMethodParser(ActionCommand.ParamInfo paramInfo) {
        Class<?> actualTypeArgumentClazz = paramInfo.getActualTypeArgumentClazz();
        return getMethodParser(actualTypeArgumentClazz);
    }

    public MethodParser getMethodParser(Class<?> paramClazz) {
        return methodParserMap.getOrDefault(paramClazz, methodParser);
    }

    public void clear() {
        methodParserMap.clear();
        paramSupplierMap.clear();
    }

    public boolean containsKey(Class<?> clazz) {
        return methodParserMap.containsKey(clazz);
    }

    public Set<Class<?>> keySet() {
        return methodParserMap.keySet();
    }

    private void init() {
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

    Object newObject(Class<?> paramClass) {
        if (paramSupplierMap.containsKey(paramClass)) {
            return paramSupplierMap.get(paramClass).get();
        }

        return null;
    }

    private void mapping(Class<?> paramClass, MethodParser methodParamParser, Supplier<?> supplier) {
        mapping(paramClass, methodParamParser);

        /*
         * 使用原生 pb 如果值为 0，在 jprotobuf 中会出现 nul 的情况，为了避免这个问题
         * 如果业务参数为 null，当解析到对应的类型时，则使用 Supplier 来创建对象
         *
         * 具体使用可参考 DefaultMethodParser
         */
        mappingParamSupplier(paramClass, supplier);
    }
}

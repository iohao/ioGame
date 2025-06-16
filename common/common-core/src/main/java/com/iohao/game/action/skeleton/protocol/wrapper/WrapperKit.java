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
package com.iohao.game.action.skeleton.protocol.wrapper;

import lombok.experimental.UtilityClass;

import java.util.*;

/**
 * 装箱、拆箱包装工具
 * <a href="https://iohao.github.io/game/docs/manual/protocol_fragment">解决协议碎片</a>
 *
 * @author 渔民小镇
 * @date 2023-06-09
 */
@UtilityClass
public class WrapperKit {
    public IntValue of(int value) {
        return IntValue.of(value);
    }

    public IntValueList ofListIntValue(List<Integer> values) {
        return IntValueList.of(values);
    }

    public BoolValue of(boolean value) {
        return BoolValue.of(value);
    }

    public BoolValueList ofListBoolValue(List<Boolean> values) {
        return BoolValueList.of(values);
    }

    public LongValue of(long value) {
        return LongValue.of(value);
    }

    public LongValueList ofListLongValue(List<Long> values) {
        return LongValueList.of(values);
    }

    public StringValue of(String value) {
        return StringValue.of(value);
    }

    public StringValueList ofListStringValue(List<String> values) {
        return StringValueList.of(values);
    }

    public <T> ByteValueList ofListByteValue(List<T> values) {
        return ofList(values);
    }

    public <T> ByteValueList ofList(List<T> values) {
        return ByteValueList.ofList(values);
    }

    public <T> ByteValueList ofListByteValue(Collection<T> values) {
        return ByteValueList.ofList(values);
    }

    /** 框架支持的协议碎片类型 */
    final Set<Class<?>> wrapperTypeSet = Set.of(
            int.class,
            Integer.class,
            IntValue.class,

            long.class,
            Long.class,
            LongValue.class,

            boolean.class,
            Boolean.class,
            BoolValue.class,

            String.class,
            StringValue.class
    );

    /**
     * 框架支持的协议碎片类型
     *
     * @param clazz class
     * @return true 是框架支持的协议碎片类型
     * @since 21.7
     */
    public boolean isWrapper(Class<?> clazz) {
        return wrapperTypeSet.contains(clazz);
    }

    final Map<Class<?>, ValueRecord> refTypeMap = new HashMap<>();

    static {
        var intRecord = new ValueRecord(IntValue.class, IntValueList.class);
        refTypeMap.put(int.class, intRecord);
        refTypeMap.put(Integer.class, intRecord);
        refTypeMap.put(IntValue.class, intRecord);

        var longRecord = new ValueRecord(LongValue.class, LongValueList.class);
        refTypeMap.put(long.class, longRecord);
        refTypeMap.put(Long.class, longRecord);
        refTypeMap.put(LongValue.class, longRecord);

        var boolRecord = new ValueRecord(BoolValue.class, BoolValueList.class);
        refTypeMap.put(boolean.class, boolRecord);
        refTypeMap.put(Boolean.class, boolRecord);
        refTypeMap.put(BoolValue.class, boolRecord);

        var stringRecord = new ValueRecord(StringValue.class, StringValueList.class);
        refTypeMap.put(String.class, stringRecord);
        refTypeMap.put(StringValue.class, stringRecord);
    }

    @Deprecated
    public Optional<Class<?>> optionalRefType(Class<?> clazz) {
        return Optional.ofNullable(refTypeMap.get(clazz)).map(ValueRecord::getClass);
    }

    public Optional<ValueRecord> optionalValueRecord(Class<?> clazz) {
        return Optional.ofNullable(refTypeMap.get(clazz));
    }

}

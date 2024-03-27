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
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.protocol.wrapper.IntValue;
import com.iohao.game.action.skeleton.protocol.wrapper.IntValueList;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * int 包装类解析器
 *
 * @author 渔民小镇
 * @date 2023-02-10
 */
final class IntValueMethodParser implements MethodParser {

    @Override
    public Class<?> getActualClazz(ActionCommand.MethodParamResultInfo methodParamResultInfo) {
        return methodParamResultInfo.isList() ? IntValueList.class : IntValue.class;
    }

    @Override
    public Object parseParam(byte[] data, ActionCommand.ParamInfo paramInfo) {

        if (paramInfo.isList()) {

            if (Objects.isNull(data)) {
                return Collections.emptyList();
            }

            var valueList = DataCodecKit.decode(data, IntValueList.class);
            return valueList.values;
        }

        if (Objects.isNull(data)) {
            return 0;
        }

        var intValue = DataCodecKit.decode(data, IntValue.class);
        return intValue.value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object parseResult(ActionCommand.ActionMethodReturnInfo actionMethodReturnInfo, Object methodResult) {

        if (actionMethodReturnInfo.isList()) {
            var valueList = new IntValueList();
            valueList.values = (List<Integer>) methodResult;
            return valueList;
        }

        /*
         * 将结果转换为 IntValue；
         * 注意这里不会检测 methodResult 是否为 null，如果担心 null 问题，
         * 可以使用 int，而不是使用 Integer
         */
        var intValue = new IntValue();
        intValue.value = (int) methodResult;
        return intValue;
    }

    private IntValueMethodParser() {
    }

    public static IntValueMethodParser me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final IntValueMethodParser ME = new IntValueMethodParser();
    }
}

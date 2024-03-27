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
import com.iohao.game.action.skeleton.protocol.wrapper.LongValue;
import com.iohao.game.action.skeleton.protocol.wrapper.LongValueList;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * long 包装类解析器
 *
 * @author 渔民小镇
 * @date 2023-02-10
 */
final class LongValueMethodParser implements MethodParser {

    @Override
    public Class<?> getActualClazz(ActionCommand.MethodParamResultInfo methodParamResultInfo) {
        return methodParamResultInfo.isList() ? LongValueList.class : LongValue.class;
    }

    @Override
    public Object parseParam(byte[] data, ActionCommand.ParamInfo paramInfo) {
        if (paramInfo.isList()) {

            if (Objects.isNull(data)) {
                return Collections.emptyList();
            }

            var valueList = DataCodecKit.decode(data, LongValueList.class);
            return valueList.values;
        }

        if (Objects.isNull(data)) {
            return 0L;
        }

        var longValue = DataCodecKit.decode(data, LongValue.class);
        return longValue.value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object parseResult(ActionCommand.ActionMethodReturnInfo actionMethodReturnInfo, Object methodResult) {
        if (actionMethodReturnInfo.isList()) {
            var valueList = new LongValueList();
            valueList.values = (List<Long>) methodResult;
            return valueList;
        }

        /*
         * 将结果转换为 LongValue；
         * 注意这里不会检测 methodResult 是否为 null，如果担心 null 问题，
         * 可以使用 long，而不是使用 Long
         */
        var longValue = new LongValue();
        longValue.value = (long) methodResult;
        return longValue;
    }

    private LongValueMethodParser() {
    }

    public static LongValueMethodParser me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final LongValueMethodParser ME = new LongValueMethodParser();
    }
}

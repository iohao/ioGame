/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.action.skeleton.core.flow.parser;

import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.protocol.wrapper.IntListPb;
import com.iohao.game.action.skeleton.protocol.wrapper.IntPb;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 由 {@link IntValueMethodParser} 代替
 * <pre>
 *     将在下个大版本中移除
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-06-26
 */
@Deprecated
final class IntPbMethodParser implements MethodParser {

    @Override
    public Class<?> getActualClazz(ActionCommand.MethodParamResultInfo methodParamResultInfo) {
        return methodParamResultInfo.isList() ? IntListPb.class : IntPb.class;
    }

    @Override
    public Object parseParam(byte[] data, ActionCommand.ParamInfo paramInfo) {

        if (paramInfo.isList()) {

            if (Objects.isNull(data)) {
                return new ArrayList<Integer>();
            }

            IntListPb intListPb = DataCodecKit.decode(data, IntListPb.class);
            return intListPb.intValues;
        }

        if (Objects.isNull(data)) {
            return 0;
        }

        IntPb intPb = DataCodecKit.decode(data, IntPb.class);
        return intPb.intValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object parseResult(ActionCommand.ActionMethodReturnInfo actionMethodReturnInfo, Object methodResult) {

        if (actionMethodReturnInfo.isList()) {
            IntListPb intListPb = new IntListPb();
            intListPb.intValues = (List<Integer>) methodResult;
            return intListPb;
        }

        /*
         * 将结果转换为 intPb；
         * 注意这里不会检测 methodResult 是否为 null，如果担心 null 问题，
         * 可以使用 int，而不是使用 Integer
         */
        IntPb intPb = new IntPb();
        intPb.intValue = (int) methodResult;
        return intPb;
    }

    private IntPbMethodParser() {

    }

    public static IntPbMethodParser me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final IntPbMethodParser ME = new IntPbMethodParser();
    }
}

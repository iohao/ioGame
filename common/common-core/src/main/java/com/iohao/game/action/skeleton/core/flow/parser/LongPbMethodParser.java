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
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.protocol.wrapper.LongListPb;
import com.iohao.game.action.skeleton.protocol.wrapper.LongPb;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 由 {@link LongValueMethodParser} 代替
 * <pre>
 *     将在下个大版本中移除
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-06-26
 */
@Deprecated
final class LongPbMethodParser implements MethodParser {

    @Override
    public Class<?> getActualClazz(ActionCommand.MethodParamResultInfo methodParamResultInfo) {
        return methodParamResultInfo.isList() ? LongListPb.class : LongPb.class;
    }

    @Override
    public Object parseParam(byte[] data, ActionCommand.ParamInfo paramInfo) {
        if (paramInfo.isList()) {

            if (Objects.isNull(data)) {
                return new ArrayList<Long>();
            }

            LongListPb longListPb = DataCodecKit.decode(data, LongListPb.class);
            return longListPb.longValues;
        }

        if (Objects.isNull(data)) {
            return 0L;
        }

        LongPb longPb = DataCodecKit.decode(data, LongPb.class);
        return longPb.longValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object parseResult(ActionCommand.ActionMethodReturnInfo actionMethodReturnInfo, Object methodResult) {
        if (actionMethodReturnInfo.isList()) {
            LongListPb longListPb = new LongListPb();
            longListPb.longValues = (List<Long>) methodResult;
            return longListPb;
        }

        /*
         * 将结果转换为 LongPb；
         * 注意这里不会检测 methodResult 是否为 null，如果担心 null 问题，
         * 可以使用 long，而不是使用 Long
         */
        LongPb longPb = new LongPb();
        longPb.longValue = (long) methodResult;
        return longPb;

    }

    private LongPbMethodParser() {

    }

    public static LongPbMethodParser me() {
        return Holder.ME;
    }


    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final LongPbMethodParser ME = new LongPbMethodParser();
    }
}

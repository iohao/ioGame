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
import com.iohao.game.action.skeleton.protocol.wrapper.ByteValueList;
import com.iohao.game.common.kit.CollKit;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 默认解析器
 *
 * @author 渔民小镇
 * @date 2022-06-26
 */

class DefaultMethodParser implements MethodParser {
    @Override
    public Class<?> getActualClazz(ActionCommand.MethodParamResultInfo methodParamResultInfo) {
        return methodParamResultInfo.getActualTypeArgumentClazz();
    }

    @Override
    public Object parseParam(byte[] data, ActionCommand.ParamInfo paramInfo) {
        Class<?> actualTypeArgumentClazz = paramInfo.getActualTypeArgumentClazz();

        if (paramInfo.isList()) {
            if (Objects.isNull(data)) {
                return Collections.emptyList();
            }

            ByteValueList byteValueList = DataCodecKit.decode(data, ByteValueList.class);

            if (CollKit.isEmpty(byteValueList.values)) {
                return Collections.emptyList();
            }

            return byteValueList.values.stream()
                    .map(bytes -> DataCodecKit.decode(bytes, actualTypeArgumentClazz))
                    .toList();
        }

        if (Objects.isNull(data)) {
            // 如果配置了 action 参数类型的 Supplier，则通过 Supplier 来创建对象
            var o = MethodParsers.newObject(actualTypeArgumentClazz);
            if (Objects.nonNull(o)) {
                return o;
            }
        }

        return DataCodecKit.decode(data, actualTypeArgumentClazz);
    }

    @Override
    public Object parseResult(ActionCommand.ActionMethodReturnInfo actionMethodReturnInfo, Object methodResult) {

        if (actionMethodReturnInfo.isList()) {

            List<Object> list = (List<Object>) methodResult;

            ByteValueList byteValueList = new ByteValueList();
            byteValueList.values = list.stream()
                    .map(DataCodecKit::encode)
                    .collect(Collectors.toList());

            return byteValueList;
        }

        return methodResult;
    }

    @Override
    public boolean isCustomMethodParser() {
        return false;
    }

    private DefaultMethodParser() {

    }

    public static DefaultMethodParser me() {
        return Holder.ME;
    }


    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final DefaultMethodParser ME = new DefaultMethodParser();
    }
}

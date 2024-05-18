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

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.common.kit.CollKit;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

/**
 * 对象类型的包装类
 *
 * @author 渔民小镇
 * @date 2023-04-17
 */
@ToString
@ProtobufClass
public final class ByteValueList {
    /** byte[] List */
    @Protobuf(fieldType = FieldType.BYTES, order = 1)
    public List<byte[]> values;

    public static ByteValueList of(List<byte[]> values) {
        if (CollKit.isEmpty(values)) {
            return new ByteValueList();
        }

        var theValue = new ByteValueList();
        theValue.values = values;
        return theValue;
    }

    public static <T> ByteValueList ofList(Collection<T> values) {
        if (CollKit.isEmpty(values)) {
            return new ByteValueList();
        }

        return of(values.stream().map(DataCodecKit::encode).toList());
    }
}

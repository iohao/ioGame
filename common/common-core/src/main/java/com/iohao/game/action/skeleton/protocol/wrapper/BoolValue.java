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
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.core.codec.DataSelfEncode;
import lombok.ToString;

import java.util.Objects;

/**
 * boolean value
 *
 * @author 渔民小镇
 * @date 2023-02-07
 */
@ToString
@ProtobufClass
public final class BoolValue implements DataSelfEncode {
    /** bool 值 */
    @Protobuf(fieldType = FieldType.BOOL, order = 1)
    public boolean value;

    transient byte[] data;

    @Ignore
    private static final BoolValue trueValue = create(true);
    @Ignore
    private static final BoolValue falseValue = create(false);

    public static BoolValue of(boolean value) {
        return value ? trueValue : falseValue;
    }

    private static BoolValue create(Boolean value) {
        var theValue = new BoolValue();
        theValue.value = value;
        theValue.data = DataCodecKit.encode(theValue);
        return theValue;
    }

    @Override
    public byte[] getEncodeData() {
        return Objects.nonNull(data) ? data : DataCodecKit.encode(this);
    }
}

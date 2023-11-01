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
package com.iohao.game.action.skeleton.protocol.wrapper;

import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.common.kit.CollKit;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * 装箱、拆箱包装工具
 * <a href="https://www.yuque.com/iohao/game/ieimzn">解决协议碎片</a>
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

        if (CollKit.isEmpty(values)) {
            return new ByteValueList();
        }

        return ByteValueList.of(values.stream().map(DataCodecKit::encode).toList());
    }
}

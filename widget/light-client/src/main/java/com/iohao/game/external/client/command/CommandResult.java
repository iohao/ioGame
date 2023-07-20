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
package com.iohao.game.external.client.command;

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.protocol.wrapper.*;
import com.iohao.game.common.kit.StrKit;
import com.iohao.game.external.core.message.ExternalMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;

/**
 * 回调结果
 *
 * @author 渔民小镇
 * @date 2023-07-08
 */
@FieldDefaults(level = AccessLevel.PACKAGE)
public class CommandResult {
    ExternalMessage externalMessage;
    /** 请求参数 */
    @Getter
    Object requestData;

    /** 业务对象 */
    Object value;

    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> toList(Class<? extends T> clazz) {
        if (value instanceof ByteValueList byteValueList) {
            return (List<T>) byteValueList.values.stream()
                    .map(bytes -> DataCodecKit.decode(bytes, clazz))
                    .toList();
        }

        if (value instanceof StringValueList stringValueList) {
            return (List<T>) stringValueList.values;
        }

        if (value instanceof LongValueList longValueList) {
            return (List<T>) longValueList.values;
        }

        if (value instanceof IntValueList intValueList) {
            return (List<T>) intValueList.values;
        }

        if (value instanceof BoolValueList boolValueList) {
            return (List<T>) boolValueList.values;
        }

        return Collections.emptyList();
    }

    public int getMsgId() {
        return externalMessage.getMsgId();
    }

    public CmdInfo getCmdInfo() {
        int cmdMerge = externalMessage.getCmdMerge();
        return CmdInfo.getCmdInfo(cmdMerge);
    }

    public byte[] getBytes() {
        return externalMessage.getData();
    }

    @Override
    public String toString() {

        CmdInfo cmdInfo = getCmdInfo();

        return StrKit.format("msgId:{} - {} \n{}"
                , getMsgId()
                , CmdKit.mergeToShort(cmdInfo.getCmdMerge())
                , value);
    }
}

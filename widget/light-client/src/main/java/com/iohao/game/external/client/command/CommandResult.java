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
package com.iohao.game.external.client.command;

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.protocol.BarMessage;
import com.iohao.game.action.skeleton.protocol.wrapper.*;
import com.iohao.game.common.kit.StrKit;
import com.iohao.game.external.core.message.ExternalMessage;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;

/**
 * 回调结果
 *
 * @author 渔民小镇
 * @date 2023-07-08
 */
@FieldDefaults(level = AccessLevel.PACKAGE)
public class CommandResult {
    final BarMessage message;
    /** 业务对象 */
    Object value;

    public CommandResult(BarMessage message) {
        this.message = message;
    }

    public ExternalMessage getExternalMessage() {
        return this.message.getHeadMetadata().getExternalMessage();
    }

    public int getMsgId() {
        return message.getHeadMetadata().getMsgId();
    }

    public CmdInfo getCmdInfo() {
        return message.getHeadMetadata().getCmdInfo();
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(Class<? extends T> clazz) {
        byte[] data = this.message.getData();

        if (Objects.isNull(this.value)) {
            this.value = DataCodecKit.decode(data, clazz);
        }

        return (T) value;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> listValue(Class<? extends T> clazz) {
        return (List<T>) this.getValue(ByteValueList.class)
                .values
                .stream()
                .map(v -> DataCodecKit.decode(v, clazz))
                .toList();
    }

    public String getString() {
        return this.getValue(StringValue.class).value;
    }

    public List<String> listString() {
        return this.getValue(StringValueList.class).values;
    }

    public int getInt() {
        return this.getValue(IntValue.class).value;
    }

    public List<Integer> listInt() {
        return this.getValue(IntValueList.class).values;
    }

    public long getLong() {
        return this.getValue(LongValue.class).value;
    }

    public List<Long> listLong() {
        return this.getValue(LongValueList.class).values;
    }

    public boolean getBoolean() {
        return this.getValue(BoolValue.class).value;
    }

    public List<Boolean> listBoolean() {
        return this.getValue(BoolValueList.class).values;
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

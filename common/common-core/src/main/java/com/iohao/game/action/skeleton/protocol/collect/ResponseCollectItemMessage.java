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
package com.iohao.game.action.skeleton.protocol.collect;

import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.action.skeleton.protocol.wrapper.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 逻辑服结果与逻辑服的信息
 *
 * @author 渔民小镇
 * @date 2022-05-22
 */
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseCollectItemMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = -7655620321337836719L;
    String logicServerId;
    ResponseMessage responseMessage;

    public ResponseCollectItemMessage() {
    }

    public ResponseCollectItemMessage(String logicServerId, ResponseMessage responseMessage) {
        this.logicServerId = logicServerId;
        this.responseMessage = responseMessage;
    }

    public boolean hasData() {
        byte[] data = responseMessage.getData();
        return data != null && data.length != 0;
    }

    /**
     * get 业务数据
     *
     * @param dataClazz biz data clazz
     * @param <T>       t
     * @return 业务数据
     */
    public <T> T getData(Class<T> dataClazz) {

        if (!this.hasData()) {
            return null;
        }

        return this.getValue(dataClazz);
    }

    /**
     * get 业务数据，与 {@link ResponseMessage#getData(Class)} 功能一样，该方法只是为了与模拟客户端命名保持一致。
     *
     * @param clazz biz data clazz
     * @param <T>   t
     * @return 业务数据，一定不为 null
     */
    public <T> T getValue(Class<? extends T> clazz) {
        return responseMessage.getData(clazz);
    }

    /**
     * get list 业务数据
     *
     * @param clazz biz data clazz
     * @param <T>   t
     * @return list 业务数据
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> listValue(Class<? extends T> clazz) {
        return (List<T>) this.getValue(ByteValueList.class)
                .values
                .stream()
                .map(v -> DataCodecKit.decode(v, clazz))
                .toList();
    }

    /**
     * get String 业务数据。<a href="https://iohao.github.io/game/docs/manual/protocol_fragment">协议碎片</a>
     *
     * @return 业务数据
     */
    public String getString() {
        return this.getValue(StringValue.class).value;
    }

    /**
     * get list String 业务数据。<a href="https://iohao.github.io/game/docs/manual/protocol_fragment">协议碎片</a>
     *
     * @return 业务数据
     */
    public List<String> listString() {
        return this.getValue(StringValueList.class).values;
    }

    /**
     * get int 业务数据。<a href="https://iohao.github.io/game/docs/manual/protocol_fragment">协议碎片</a>
     *
     * @return 业务数据
     */
    public int getInt() {
        return this.getValue(IntValue.class).value;
    }

    /**
     * get list int 业务数据。<a href="https://iohao.github.io/game/docs/manual/protocol_fragment">协议碎片</a>
     *
     * @return 业务数据
     */
    public List<Integer> listInt() {
        return this.getValue(IntValueList.class).values;
    }

    /**
     * get long 业务数据。<a href="https://iohao.github.io/game/docs/manual/protocol_fragment">协议碎片</a>
     *
     * @return 业务数据
     */
    public long getLong() {
        return this.getValue(LongValue.class).value;
    }

    /**
     * get list long 业务数据。<a href="https://iohao.github.io/game/docs/manual/protocol_fragment">协议碎片</a>
     *
     * @return 业务数据
     */
    public List<Long> listLong() {
        return this.getValue(LongValueList.class).values;
    }

    /**
     * get boolean 业务数据。<a href="https://iohao.github.io/game/docs/manual/protocol_fragment">协议碎片</a>
     *
     * @return 业务数据
     */
    public boolean getBoolean() {
        return this.getValue(BoolValue.class).value;
    }

    /**
     * get list boolean 业务数据。<a href="https://iohao.github.io/game/docs/manual/protocol_fragment">协议碎片</a>
     *
     * @return 业务数据
     */
    public List<Boolean> listBoolean() {
        return this.getValue(BoolValueList.class).values;
    }
}
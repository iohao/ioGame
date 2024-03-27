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

import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

/**
 * 逻辑服结果与逻辑服的信息
 *
 * @author 渔民小镇
 * @date 2022-05-22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseCollectItemMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = -7655620321337836719L;

    ResponseMessage responseMessage;
    String logicServerId;

    public boolean hasData() {
        if (responseMessage == null) {
            return false;
        }

        byte[] data = responseMessage.getData();
        return data != null && data.length != 0;
    }

    public <T> T getData(Class<T> dataClazz) {

        if (!this.hasData()) {
            return null;
        }

        return responseMessage.getData(dataClazz);
    }
}

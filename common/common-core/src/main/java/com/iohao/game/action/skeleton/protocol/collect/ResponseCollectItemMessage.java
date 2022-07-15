/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.action.skeleton.protocol.collect;

import com.iohao.game.action.skeleton.core.DataCodecKit;
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

        byte[] data = responseMessage.getData();
        return DataCodecKit.decode(data, dataClazz);
    }
}

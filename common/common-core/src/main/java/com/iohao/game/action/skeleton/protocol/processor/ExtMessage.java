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
package com.iohao.game.action.skeleton.protocol.processor;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 渔民小镇
 * @date 2022-05-30
 */
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class ExtMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = -1525598942046052346L;

    /**
     * 来源逻辑服 client id
     * <pre>
     *     谁发起的请求，这个来源就是记录谁的服务器id
     *     clientId 指的是 服务器的唯一id
     *
     *     see {@link com.iohao.game.common.kit.MurmurHash3}
     * </pre>
     */
    int sourceClientId;

    /** 业务数据 */
    Serializable data;

    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return (T) this.data;
    }
}

/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.common.kit;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2022-01-11
 */
@Slf4j
@UtilityClass
public class ProtoKit {
    final byte[] emptyBytes = new byte[0];

    /**
     * 将对象转为 pb 字节数组
     *
     * @param data 对象
     * @return 字节数组 （一定不为null）
     */
    @SuppressWarnings("unchecked")
    public byte[] toBytes(Object data) {

        if (Objects.isNull(data)) {
            return emptyBytes;
        }

        Class clazz = data.getClass();
        Codec<Object> codec = ProtobufProxy.create(clazz);

        try {
            return codec.encode(data);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

        return emptyBytes;
    }

    /**
     * 将字节解析成 pb 对象
     *
     * @param data  pb 字节
     * @param clazz pb class
     * @param <T>   t
     * @return pb 对象
     */
    public <T> T parseProtoByte(byte[] data, Class<T> clazz) {

        if (Objects.isNull(data) || data.length == 0) {
            return null;
        }

        Codec<T> codec = ProtobufProxy.create(clazz);

        try {
            return codec.decode(data);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }
}

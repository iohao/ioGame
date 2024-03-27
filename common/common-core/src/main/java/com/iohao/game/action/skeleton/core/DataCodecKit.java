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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.core.codec.DataCodec;
import com.iohao.game.action.skeleton.core.codec.ProtoDataCodec;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

/**
 * 业务框架对业务数据的编解码器
 * <pre>
 *     会在构建业务框架时赋值  DataCodecKit#dataCodec
 *     see {@link BarSkeletonBuilder#build()}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-18
 */
@UtilityClass
public class DataCodecKit {

    /** 业务数据的编解码器 */
    @Getter
    @Setter
    DataCodec dataCodec = new ProtoDataCodec();

    /**
     * 将业务参数编码成字节数组
     *
     * @param data 业务参数 (指的是请求端的请求参数)
     * @return bytes
     */
    public byte[] encode(Object data) {
        return dataCodec.encode(data);
    }

    /**
     * 将字节数组解码成对象
     *
     * @param data       业务参数 (指的是请求端的请求参数)
     * @param paramClazz clazz
     * @param <T>        t
     * @return 业务参数
     */
    public <T> T decode(byte[] data, Class<T> paramClazz) {
        return dataCodec.decode(data, paramClazz);
    }
}

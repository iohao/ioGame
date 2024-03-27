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
package com.iohao.game.action.skeleton.core.codec;


import com.alibaba.fastjson2.JSON;

/**
 * json 使用的 fastjson2
 * <pre>
 *     注意：如果使用该类，需要在你的项目中引入 fastjson2 的依赖
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-11-24
 */
public final class JsonDataCodec implements DataCodec {
    @Override
    public byte[] encode(Object data) {
        return JSON.toJSONBytes(data);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T decode(byte[] data, Class<?> dataClass) {
        return (T) JSON.parseObject(data, dataClass);
    }

    @Override
    public String codecName() {
        return "fastjson2";
    }
}

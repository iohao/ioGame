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
package com.iohao.game.bolt.broker.core.kit;

import com.alipay.remoting.exception.CodecException;
import com.alipay.remoting.serialization.HessianSerializer;
import com.iohao.game.common.consts.IoGameLogName;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 渔民小镇
 * @date 2023-01-18
 */
@UtilityClass
@Slf4j(topic = IoGameLogName.CommonStdout)
public class HessianKit {
    final HessianSerializer hessianSerializer = new HessianSerializer();

    public byte[] serialize(Object obj) {
        try {
            return hessianSerializer.serialize(obj);
        } catch (CodecException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public <T> T deserialize(byte[] data, Class<T> classOfT) {
        try {
            return hessianSerializer.deserialize(data, "");
        } catch (CodecException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}

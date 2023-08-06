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
package com.iohao.game.action.skeleton.pulse.core.consumer;

import com.iohao.game.action.skeleton.pulse.core.PulseTransmit;
import com.iohao.game.action.skeleton.pulse.message.PulseSignalRequest;
import com.iohao.game.action.skeleton.pulse.message.PulseSignalResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2023-04-20
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class DefaultPulseConsumers implements PulseConsumers {
    final Map<String, PulseConsumer<?>> map = new NonBlockingHashMap<>();
    PulseTransmit pulseTransmit;

    public void addPulseConsumer(PulseConsumer<?> pulseConsumer) {
        String channel = pulseConsumer.channel();
        this.map.putIfAbsent(channel, pulseConsumer);
    }

    public void acceptPulseSign(PulseSignalRequest request) {
        // 通过信号频道，找到对应的脉冲消费者来处理该信号
        String channel = request.getChannel();
        PulseConsumer<?> pulseConsumer = this.map.get(channel);
        if (Objects.isNull(pulseConsumer)) {
            return;
        }

        try {
            // 把需要使用的变量先取出来，防止开发者变更 request 的内容。
            int sourceClientId = request.getSourceClientId();
            // 脉冲消费者处理后的数据
            Serializable data = pulseConsumer.accept(request.getData(), request);
            if (Objects.isNull(data)) {
                // 如果为 null 表示不需要响应数据给脉冲发射器
                return;
            }

            // 脉冲响应
            PulseSignalResponse response = new PulseSignalResponse();
            response.setChannel(channel);
            response.setSourceClientId(sourceClientId);
            response.setData(data);

            // 脉冲消费者信号响应
            pulseTransmit.transmit(response);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void setPulseTransmit(PulseTransmit pulseTransmit) {
        this.pulseTransmit = pulseTransmit;
    }

    @Override
    public PulseTransmit getPulseTransmit() {
        return this.pulseTransmit;
    }
}

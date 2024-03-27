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
package com.iohao.game.bolt.broker.core.common.processor.pulse;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.iohao.game.action.skeleton.pulse.core.consumer.PulseConsumers;
import com.iohao.game.action.skeleton.pulse.message.PulseSignalRequest;
import com.iohao.game.bolt.broker.core.aware.PulseConsumerAware;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * 脉冲信号请求接收
 *
 * @author 渔民小镇
 * @date 2023-04-20
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PulseSignalRequestUserProcessor extends AbstractAsyncUserProcessor<PulseSignalRequest>
        implements PulseConsumerAware {

    PulseConsumers pulseConsumers;

    @Override
    public void setPulseConsumers(PulseConsumers pulseConsumers) {
        this.pulseConsumers = pulseConsumers;
    }

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, PulseSignalRequest request) {
        // 脉冲消费者接收脉冲发射器发送过来的脉冲信号
        pulseConsumers.acceptPulseSign(request);
    }

    @Override
    public String interest() {
        return PulseSignalRequest.class.getName();
    }
}

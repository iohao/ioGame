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
import com.iohao.game.action.skeleton.pulse.core.producer.PulseProducers;
import com.iohao.game.action.skeleton.pulse.message.PulseSignalResponse;
import com.iohao.game.bolt.broker.core.aware.PulseProducerAware;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;

/**
 * 脉冲信号响应接收
 *
 * @author 渔民小镇
 * @date 2023-04-22
 */
public class PulseSignalResponseUserProcessor extends AbstractAsyncUserProcessor<PulseSignalResponse>
        implements PulseProducerAware {
    PulseProducers pulseProducers;

    @Override
    public void setPulseProducers(PulseProducers pulseProducers) {
        this.pulseProducers = pulseProducers;
    }

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, PulseSignalResponse response) {
        this.pulseProducers.acceptPulseSign(response);
    }

    @Override
    public String interest() {
        return PulseSignalResponse.class.getName();
    }
}

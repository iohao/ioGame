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
package com.iohao.game.action.skeleton.pulse.runner;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.SkeletonAttr;
import com.iohao.game.action.skeleton.core.commumication.BrokerClientContext;
import com.iohao.game.action.skeleton.core.runner.Runner;
import com.iohao.game.action.skeleton.protocol.processor.SimpleServerInfo;
import com.iohao.game.action.skeleton.pulse.Pulses;
import com.iohao.game.action.skeleton.pulse.core.consumer.PulseConsumers;
import com.iohao.game.action.skeleton.pulse.core.producer.PulseProducers;
import com.iohao.game.action.skeleton.pulse.core.PulseTransmit;
import com.iohao.game.action.skeleton.pulse.message.PulseSignalMessage;
import com.iohao.game.action.skeleton.pulse.message.PulseSignalRequest;
import com.iohao.game.action.skeleton.pulse.message.PulseSignalResponse;

import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2023-04-23
 */
public class CreatePulsesRunner implements Runner {

    @Override
    public void onStart(BarSkeleton skeleton) {
        BrokerClientContext brokerClientContext = skeleton.option(SkeletonAttr.brokerClientContext);
        Integer sourceClientId = skeleton.option(SkeletonAttr.logicServerIdHash);

        // 脉冲管理器
        Pulses pulses = getPulses(skeleton);

        // 脉冲消费者
        PulseConsumers pulseConsumers = pulses.getPulseConsumers();
        pulseConsumers.setPulseTransmit(new ConsumerTransmit(brokerClientContext));
        skeleton.option(SkeletonAttr.consumerPulseTransmit, pulseConsumers.getPulseTransmit());

        // 脉冲生产者
        PulseProducers pulseProducers = pulses.getPulseProducers();
        pulseProducers.setPulseTransmit(new ProducerTransmit(brokerClientContext, sourceClientId));
        skeleton.option(SkeletonAttr.producerPulseTransmit, pulseProducers.getPulseTransmit());

        // 启动脉冲生产者
        pulseProducers.startup();
    }

    protected Pulses getPulses(BarSkeleton skeleton) {
        Pulses pulses = skeleton.option(SkeletonAttr.pulses);

        if (Objects.isNull(pulses)) {
            pulses = new Pulses();
            // 将脉冲管理器的引用保存到业务框架的动态属性中
            skeleton.option(SkeletonAttr.pulses, pulses);
        }

        return pulses;
    }

    record ConsumerTransmit(BrokerClientContext brokerClientContext) implements PulseTransmit {
        @Override
        public void transmit(PulseSignalMessage message) {

            if (message instanceof PulseSignalResponse response) {
                SimpleServerInfo simpleServerInfo = brokerClientContext.getSimpleServerInfo();
                response.setSimpleServerInfo(simpleServerInfo);
                this.brokerClientContext.sendResponse(message);
            }

        }
    }

    record ProducerTransmit(BrokerClientContext brokerClientContext, int sourceClientId) implements PulseTransmit {
        @Override
        public void transmit(PulseSignalMessage message) {
            /*
             * 设置当前逻辑服的的 id，目的是为了当有脉冲信号响应时，可以找到发送脉冲信号的服务器；
             * 由于是当前逻辑服发送的脉冲信号，所以设置当前逻辑服 id。
             *
             * 将脉冲信号发射到 Broker（游戏网关）
             */
            if (message instanceof PulseSignalRequest request && request.getSignalType() != 0) {
                message.setSourceClientId(sourceClientId);
                this.brokerClientContext.sendResponse(message);
            }
        }
    }
}

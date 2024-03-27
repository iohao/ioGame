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
package com.iohao.game.action.skeleton.pulse.core.consumer;

import com.iohao.game.action.skeleton.pulse.core.PulseTransmit;
import com.iohao.game.action.skeleton.pulse.message.PulseSignalRequest;

/**
 * 脉冲消费器
 *
 * @author 渔民小镇
 * @date 2023-04-20
 */
public interface PulseConsumers {
    /**
     * 添加脉冲信号消费者
     *
     * @param pulseConsumer 脉冲信号消费者
     */
    void addPulseConsumer(PulseConsumer<?> pulseConsumer);

    /**
     * 接收脉冲信号
     * <pre>
     *     当消费者接收到信号后
     * </pre>
     *
     * @param request 脉冲信号请求
     */
    void acceptPulseSign(PulseSignalRequest request);

    /**
     * 设置脉冲消息发射器
     *
     * @param pulseTransmit 脉冲消息发射器
     */
    void setPulseTransmit(PulseTransmit pulseTransmit);

    /**
     * 得到脉冲消息发射器
     *
     * @return 脉冲消息发射器
     */
    PulseTransmit getPulseTransmit();
}

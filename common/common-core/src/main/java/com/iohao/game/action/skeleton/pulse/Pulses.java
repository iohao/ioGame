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
package com.iohao.game.action.skeleton.pulse;

import com.iohao.game.action.skeleton.pulse.core.consumer.DefaultPulseConsumers;
import com.iohao.game.action.skeleton.pulse.core.producer.DefaultPulseProducers;
import com.iohao.game.action.skeleton.pulse.core.consumer.PulseConsumers;
import com.iohao.game.action.skeleton.pulse.core.producer.PulseProducers;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * 脉冲管理器
 *
 * @author 渔民小镇
 * @date 2023-04-21
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PACKAGE)
public final class Pulses {
    /** 脉冲信号生产者 */
    PulseConsumers pulseConsumers = new DefaultPulseConsumers();
    /** 脉冲信号消费者 */
    PulseProducers pulseProducers = new DefaultPulseProducers();
}

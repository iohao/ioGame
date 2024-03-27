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

import com.iohao.game.action.skeleton.pulse.Pulses;
import com.iohao.game.action.skeleton.core.runner.Runner;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.SkeletonAttr;
import com.iohao.game.action.skeleton.pulse.core.consumer.PulseConsumers;
import com.iohao.game.action.skeleton.pulse.core.producer.PulseProducers;

/**
 * @author 渔民小镇
 * @date 2023-04-23
 */
public interface PulseRunner extends Runner {
    /**
     * 启动
     * <pre>
     *     框架会在系统启动后调用一次
     * </pre>
     *
     * @param skeleton 业务框架
     */
    @Override
    default void onStart(BarSkeleton skeleton) {
        Pulses pulses = skeleton.option(SkeletonAttr.pulses);
        PulseProducers pulseProducers = pulses.getPulseProducers();
        PulseConsumers pulseConsumers = pulses.getPulseConsumers();
        this.onStart(pulseProducers, pulseConsumers, skeleton);
    }

    /**
     * 启动
     * <pre>
     *     框架会在系统启动后调用一次
     * </pre>
     *
     * @param pulseProducers 脉冲生产器
     * @param pulseConsumers 脉冲消费器
     * @param skeleton       业务框架
     */
    void onStart(PulseProducers pulseProducers, PulseConsumers pulseConsumers, BarSkeleton skeleton);
}

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

import com.iohao.game.action.skeleton.core.commumication.BrokerClientContext;
import com.iohao.game.action.skeleton.eventbus.EventBus;
import com.iohao.game.action.skeleton.pulse.Pulses;
import com.iohao.game.action.skeleton.pulse.core.PulseTransmit;
import com.iohao.game.common.kit.attr.AttrOption;

/**
 * 业务框架动态属性
 *
 * @author 渔民小镇
 * @date 2023-04-21
 * @see BarSkeleton
 */
public interface SkeletonAttr {
    /** 脉冲管理器 */
    AttrOption<Pulses> pulses = AttrOption.valueOf("pulses");
    /** 当前逻辑服引用 */
    AttrOption<BrokerClientContext> brokerClientContext = AttrOption.valueOf("brokerClientContext");
    /** 服务器唯一标识 hash */
    AttrOption<Integer> logicServerIdHash = AttrOption.valueOf("logicServerIdHash");
    /** 脉冲生产者的发射器 */
    AttrOption<PulseTransmit> producerPulseTransmit = AttrOption.valueOf("producerPulseTransmit");
    /** 脉冲消费者的发射器 */
    AttrOption<PulseTransmit> consumerPulseTransmit = AttrOption.valueOf("consumerPulseTransmit");
    /** EventBus 是逻辑服事件总线，与业务框架、逻辑服是 1:1:1 的关系 */
    AttrOption<EventBus> eventBus = AttrOption.valueOf("eventBus");
}

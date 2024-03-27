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
package com.iohao.game.action.skeleton.eventbus;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author 渔民小镇
 * @date 2023-12-24
 */
@Slf4j
@EventBusSubscriber
public class CustomEvent {
    static LongAdder myMessageLong = new LongAdder();
    static LongAdder myRecordLong = new LongAdder();

    @EventSubscribe(order = 1, value = ExecutorSelector.simpleExecutor)
    public void myMessage1(MyMessage message) {
        log.info("###myMessage1 : {}", message);
        myMessageLong.increment();
    }

    @EventSubscribe(order = 3, value = ExecutorSelector.simpleExecutor)
    public void myMessage3(MyMessage message) {
        log.info("###myMessage3 : {}", message);
        myMessageLong.increment();
    }

    @EventSubscribe(order = 2, value = ExecutorSelector.simpleExecutor)
    public void myMessage2(MyMessage message) {
        log.info("###myMessage2 : {}", message);
        myMessageLong.increment();
    }

    @EventSubscribe
    public void myRecord1(MyRecord message) {
        log.info("myRecord1 : {}", message);
        myRecordLong.increment();
    }

    @EventSubscribe
    public void myRecord2(MyRecord message) {
        log.info("myRecord2 : {} - {}", message, message.getClass());
        myRecordLong.increment();
    }
}

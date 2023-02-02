/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.widget.light.domain.event;

import cn.hutool.core.thread.ThreadUtil;
import com.iohao.game.widget.light.domain.event.student.StudentCountEventHandler;
import com.iohao.game.widget.light.domain.event.student.StudentEo;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author 渔民小镇
 * @date 2022-09-02
 */
@Slf4j
public class StudentDomainEventTest2 {


    DomainEventContext domainEventContext;

    @After
    public void tearDown() throws Exception {
        // 事件消费完后 - 事件停止
        domainEventContext.stop();
    }

    @Before
    public void setUp() {
        // ======项目启动时配置一次（初始化）======

        // 领域事件上下文参数
        DomainEventContextParam contextParam = new DomainEventContextParam();

        contextParam.addEventHandler(new StudentCountEventHandler());

        // 启动事件驱动
        domainEventContext = new DomainEventContext(contextParam);
        domainEventContext.startup();
    }

    @Test
    public void testEventSendMulti2() throws InterruptedException {
        StudentEo studentEo = new StudentEo(1);

        ThreadUtil.concurrencyTest(100, () -> {
            for (int j = 0; j < 20_000; j++) {
                studentEo.send();
            }
        });

        log.info("start");
        /*
         * 需要等待一下；
         * 如果不等待，但在测试用例中又执行 domainEventContext.stop(); 方法
         * disruptor 将不会接收事件了
         */
        log.info("StudentCountEventHandler.longAdder : {}", StudentCountEventHandler.longAdder);
        TimeUnit.SECONDS.sleep(1);
        log.info("======== longAdder ========: {}", StudentCountEventHandler.longAdder);
    }

    @Test
    public void testEventSendSingle() throws InterruptedException {
        StudentEo studentEo = new StudentEo(1);
        for (int i = 0; i < 2_000_000; i++) {
            studentEo.send();
        }

        log.info("start");
        TimeUnit.SECONDS.sleep(2);
        log.info("StudentCountEventHandler.longAdder : {}", StudentCountEventHandler.longAdder);
    }
}

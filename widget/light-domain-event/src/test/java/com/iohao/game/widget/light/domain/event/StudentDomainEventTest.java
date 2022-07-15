/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.widget.light.domain.event;

import com.iohao.game.widget.light.domain.event.student.StudentEmailEventHandler1;
import com.iohao.game.widget.light.domain.event.student.StudentEo;
import com.iohao.game.widget.light.domain.event.student.StudentGoHomeEventHandler2;
import com.iohao.game.widget.light.domain.event.student.StudentSleepEventHandler3;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <pre>
 *     领域事件的启动与停止,整个系统的生命周期只需要做一次.
 *     例如在你的应用中启动, 那么启动领取事件 DomainEventHandlerConfig.start();
 *     应用关闭时可以调用:DomainEventHandlerConfig.stop()
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
public class StudentDomainEventTest {

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
        // 配置一个学生的领域事件消费 - 给学生发生一封邮件
        contextParam.addEventHandler(new StudentEmailEventHandler1());
        // 配置一个学生的领域事件消费 - 回家
        contextParam.addEventHandler(new StudentGoHomeEventHandler2());
        // 配置一个学生的领域事件消费 - 让学生睡觉
        contextParam.addEventHandler(new StudentSleepEventHandler3());

        // 启动事件驱动
        domainEventContext = new DomainEventContext(contextParam);
        domainEventContext.startup();
    }

    @Test
    public void testEventSend() {
        // 这里开始就是你的业务代码
        StudentEo studentEo = new StudentEo(1);
        /*
         * 发送事件、上面只配置了一个事件。
         * 如果将来还需要给学生发送一封email,那么直接配置。（可扩展）
         * 如果将来还需要记录学生今天上了什么课程，那么也是直接配置 （可扩展） 这里的业务代码无需任何改动（松耦合）
         * 如果将来又不需要给学生发送email的事件了，直接删除配置即可，这里还是无需改动代码。（高伸缩）
         */
        studentEo.send();
    }
}

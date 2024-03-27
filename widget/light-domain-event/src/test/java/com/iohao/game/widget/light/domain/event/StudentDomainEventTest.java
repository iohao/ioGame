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

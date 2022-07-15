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

import com.iohao.game.widget.light.domain.event.user.UserLogin;
import com.iohao.game.widget.light.domain.event.user.UserLoginEmailEventHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author 渔民小镇
 * @date 2021-12-26
 */
public class UserLoginDomainEventTest {

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
        // 用户登录就发送 email
        contextParam.addEventHandler(new UserLoginEmailEventHandler());

        // 启动事件驱动
        domainEventContext = new DomainEventContext(contextParam);
        domainEventContext.startup();
    }

    @Test
    public void testEventSend() {
        // 这里开始就是你的业务代码
        UserLogin userLogin = new UserLogin(101, "塔姆");
        /*
         * 发送事件、上面只配置了一个事件。
         * 如果将来还需要给用户登录 记录登录日志,那么直接配置。（可扩展）
         * 如果将来还需要记录用户登录 今天上了什么课程，那么也是直接配置 （可扩展） 这里的业务代码无需任何改动（松耦合）
         * 如果将来又不需要给用户登录发送email的事件了，直接删除配置即可，这里还是无需改动代码。（高伸缩）
         */
        DomainEventPublish.send(userLogin);
    }
}

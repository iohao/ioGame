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

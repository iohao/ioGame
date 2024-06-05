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

import com.iohao.game.action.skeleton.core.action.ExampleActionCmd;
import com.iohao.game.action.skeleton.core.action.pojo.BeeApple;
import com.iohao.game.action.skeleton.core.data.TestDataKit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class BarSkeletonTest {
    BarSkeleton barSkeleton;

    @Before
    public void setUp() {
        // 构建业务框架
        barSkeleton = TestDataKit.newBarSkeleton();
    }

    @Test
    public void newBuilder() {

        // 模拟路由信息
        CmdInfo cmdInfo = CmdInfo.of(ExampleActionCmd.BeeActionCmd.cmd, ExampleActionCmd.BeeActionCmd.hello);

        // 模拟请求数据 （一般由前端传入）
        BeeApple beeApple = new BeeApple();
        beeApple.setContent("hello 塔姆!");
        beeApple.setId(101);

        var flowContext = TestDataKit.ofFlowContext(cmdInfo, beeApple);

        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);
        System.out.println();

        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);
        System.out.println();
    }

    @Test
    public void testVoid() {

        // 模拟路由信息
        CmdInfo cmdInfo = CmdInfo.of(ExampleActionCmd.BeeActionCmd.cmd, ExampleActionCmd.BeeActionCmd.test_void);

        // 模拟请求数据 （一般由前端传入）
        BeeApple beeApple = new BeeApple();
        beeApple.setContent("hello 塔姆!");
        beeApple.setId(1010);

        var flowContext = TestDataKit.ofFlowContext(cmdInfo, beeApple);

        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);
        System.out.println();
    }
}
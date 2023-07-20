/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import org.junit.Test;

public class BarSkeletonTest {

    @Test
    public void newBuilder() {
        // 构建业务框架
        BarSkeleton barSkeleton = TestDataKit.newBarSkeleton();

        // 模拟路由信息
        CmdInfo cmdInfo = CmdInfo.getCmdInfo(ExampleActionCmd.BeeActionCmd.cmd, ExampleActionCmd.BeeActionCmd.hello);

        // 模拟请求
        HeadMetadata headMetadata = new HeadMetadata();
        headMetadata.setCmdInfo(cmdInfo);

        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setHeadMetadata(headMetadata);

        // 模拟请求数据 （一般由前端传入）
        BeeApple beeApple = new BeeApple();
        beeApple.setContent("hello 塔姆!");
        beeApple.setId(101);
        // 把模拟请求的数据,放入请求对象中

        byte[] data = DataCodecKit.encode(beeApple);
        requestMessage.setData(data);

        var flowContext = new FlowContext()
                .setRequest(requestMessage);

        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);
        System.out.println();

        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);
        System.out.println();
    }

    @Test
    public void testVoid() {
        // 构建业务框架
        BarSkeleton barSkeleton = TestDataKit.newBarSkeleton();

        // 模拟路由信息
        CmdInfo cmdInfo = CmdInfo.getCmdInfo(ExampleActionCmd.BeeActionCmd.cmd, ExampleActionCmd.BeeActionCmd.test_void);

        // 模拟请求
        HeadMetadata headMetadata = new HeadMetadata();
        headMetadata.setCmdInfo(cmdInfo);

        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setHeadMetadata(headMetadata);

        // 模拟请求数据 （一般由前端传入）
        BeeApple beeApple = new BeeApple();
        beeApple.setContent("hello 塔姆!");
        beeApple.setId(1010);
        // 把模拟请求的数据,放入请求对象中
        byte[] data = DataCodecKit.encode(beeApple);
        requestMessage.setData(data);

        var flowContext = new FlowContext()
                .setRequest(requestMessage);

        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);
        System.out.println();
    }

}
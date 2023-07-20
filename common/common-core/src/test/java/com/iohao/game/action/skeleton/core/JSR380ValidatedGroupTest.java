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
import com.iohao.game.action.skeleton.core.action.pojo.BirdValid;
import com.iohao.game.action.skeleton.core.data.TestDataKit;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import org.junit.Before;
import org.junit.Test;

/**
 * @author fangwei
 * @date 2022-09-20
 */
public class JSR380ValidatedGroupTest {
    BarSkeleton barSkeleton;

    @Before
    public void setUp() {
        BarSkeletonBuilder builder = TestDataKit.createBuilder();
        builder.getSetting().setValidator(true);

        barSkeleton = builder.build();
    }

    @Test
    public void updateGroupTest() {
        BirdValid birdValid = new BirdValid();

        CmdInfo cmdInfo = CmdInfo.getCmdInfo(ExampleActionCmd.BeeActionCmd.cmd, ExampleActionCmd.BeeActionCmd.validated_group_update);

        RequestMessage requestMessage = TestDataKit.createRequestMessage(cmdInfo);
        requestMessage.setData(birdValid);

        FlowContext flowContext = new FlowContext();
        flowContext.setRequest(requestMessage);

        barSkeleton.handle(flowContext);
    }

    @Test
    public void createGroupTest() {
        BirdValid birdValid = new BirdValid();

        CmdInfo cmdInfo = CmdInfo.getCmdInfo(ExampleActionCmd.BeeActionCmd.cmd, ExampleActionCmd.BeeActionCmd.validated_group_create);

        RequestMessage requestMessage = TestDataKit.createRequestMessage(cmdInfo);
        requestMessage.setData(birdValid);

        FlowContext flowContext = new FlowContext();
        flowContext.setRequest(requestMessage);

        barSkeleton.handle(flowContext);
    }

}

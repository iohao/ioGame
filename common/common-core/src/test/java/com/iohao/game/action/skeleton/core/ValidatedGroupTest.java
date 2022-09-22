/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.core.action.ExampleActionCont;
import com.iohao.game.action.skeleton.core.action.pojo.BirdValid;
import com.iohao.game.action.skeleton.core.data.TestDataKit;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

/**
 * @author fangwei
 * @date 2022-09-20
 */
@Slf4j
public class ValidatedGroupTest {
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

        CmdInfo cmdInfo = CmdInfo.getCmdInfo(ExampleActionCont.BeeActionCont.cmd, ExampleActionCont.BeeActionCont.validated_group_update);

        RequestMessage requestMessage = TestDataKit.createRequestMessage(cmdInfo);
        requestMessage.setData(birdValid);

        FlowContext flowContext = new FlowContext();
        flowContext.setRequest(requestMessage);

        barSkeleton.handle(flowContext);
    }

    @Test
    public void createGroupTest() {
        BirdValid birdValid = new BirdValid();

        CmdInfo cmdInfo = CmdInfo.getCmdInfo(ExampleActionCont.BeeActionCont.cmd, ExampleActionCont.BeeActionCont.validated_group_create);

        RequestMessage requestMessage = TestDataKit.createRequestMessage(cmdInfo);
        requestMessage.setData(birdValid);

        FlowContext flowContext = new FlowContext();
        flowContext.setRequest(requestMessage);

        barSkeleton.handle(flowContext);
    }

}

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

import com.iohao.game.action.skeleton.core.data.TestDataKit;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.wrapper.IntListPb;
import com.iohao.game.action.skeleton.protocol.wrapper.IntPb;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;

import static com.iohao.game.action.skeleton.core.action.ExampleActionCont.WrapperIntActionCont;

/**
 * @author 渔民小镇
 * @date 2022-06-26
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WrapperIntTest {

    private CmdInfo getCmdInfo(int subCmd) {
        return CmdInfo.getCmdInfo(WrapperIntActionCont.cmd, subCmd);
    }

    private FlowContext createIntPbFlowContext(int subCmd) {
        CmdInfo cmdInfo = this.getCmdInfo(subCmd);

        IntPb intPb = new IntPb();
        intPb.intValue = 100;

        RequestMessage requestMessage = TestDataKit.createRequestMessage(cmdInfo);
        requestMessage.setData(intPb);

        return new FlowContext()
                .setRequest(requestMessage);
    }

    BarSkeleton barSkeleton;

//    @Before
    public void setUp() {
        barSkeleton = TestDataKit.newBarSkeleton();
    }


//    @Test
    public void intPb() {
        FlowContext flowContext ;
        flowContext = this.createIntPbFlowContext(WrapperIntActionCont.intPb2Void);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperIntActionCont.intPb2Int);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperIntActionCont.intPb2IntPb);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperIntActionCont.intPb2IntList);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperIntActionCont.intListVoid);
        IntListPb intListPb = new IntListPb();
        intListPb.intValues = new ArrayList<>();
        intListPb.intValues.add(1);
        intListPb.intValues.add(3);
        intListPb.intValues.add(5);

        RequestMessage request = flowContext.getRequest();
        request.setData(intListPb);

        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);
    }

//    @Test
    public void intValue() {
        FlowContext flowContext ;
        flowContext = this.createIntPbFlowContext(WrapperIntActionCont.int2Void);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperIntActionCont.int2Int);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperIntActionCont.int2IntPb);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperIntActionCont.int2IntList);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);
    }

//    @Test
    public void integerValue() {
        FlowContext flowContext ;
        flowContext = this.createIntPbFlowContext(WrapperIntActionCont.integer2Void);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperIntActionCont.integer2Integer);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperIntActionCont.integer2IntPb);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperIntActionCont.integer2IntegerList);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);
    }
}

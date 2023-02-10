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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.core.data.TestDataKit;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.wrapper.IntValueList;
import com.iohao.game.action.skeleton.protocol.wrapper.IntValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;

import static com.iohao.game.action.skeleton.core.action.ExampleActionCmd.WrapperIntActionCmd;

/**
 * @author 渔民小镇
 * @date 2022-06-26
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WrapperIntTest {

    private CmdInfo getCmdInfo(int subCmd) {
        return CmdInfo.getCmdInfo(WrapperIntActionCmd.cmd, subCmd);
    }

    private FlowContext createIntValueFlowContext(int subCmd) {
        CmdInfo cmdInfo = this.getCmdInfo(subCmd);

        IntValue intValue = new IntValue();
        intValue.value = 100;

        RequestMessage requestMessage = TestDataKit.createRequestMessage(cmdInfo);
        requestMessage.setData(intValue);

        return new FlowContext()
                .setRequest(requestMessage);
    }

    BarSkeleton barSkeleton;

    //    @Before
    public void setUp() {
        barSkeleton = TestDataKit.newBarSkeleton();
    }


    //    @Test
    public void intValue1() {
        FlowContext flowContext;
        flowContext = this.createIntValueFlowContext(WrapperIntActionCmd.intValue2Void);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntValueFlowContext(WrapperIntActionCmd.intValue2Int);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntValueFlowContext(WrapperIntActionCmd.intValue2IntValue);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntValueFlowContext(WrapperIntActionCmd.intValue2IntList);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntValueFlowContext(WrapperIntActionCmd.intListVoid);
        IntValueList intValueList = new IntValueList();
        intValueList.values = new ArrayList<>();
        intValueList.values.add(1);
        intValueList.values.add(3);
        intValueList.values.add(5);

        RequestMessage request = flowContext.getRequest();
        request.setData(intValueList);

        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);
    }

    //    @Test
    public void intValue2() {
        FlowContext flowContext;
        flowContext = this.createIntValueFlowContext(WrapperIntActionCmd.int2Void);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntValueFlowContext(WrapperIntActionCmd.int2Int);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntValueFlowContext(WrapperIntActionCmd.int2IntValue);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntValueFlowContext(WrapperIntActionCmd.int2IntList);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);
    }

    //    @Test
    public void integerValue() {
        FlowContext flowContext;
        flowContext = this.createIntValueFlowContext(WrapperIntActionCmd.integer2Void);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntValueFlowContext(WrapperIntActionCmd.integer2Integer);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntValueFlowContext(WrapperIntActionCmd.integer2IntValue);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntValueFlowContext(WrapperIntActionCmd.integer2IntegerList);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);
    }
}

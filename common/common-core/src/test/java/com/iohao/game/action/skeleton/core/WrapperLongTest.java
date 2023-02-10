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
import com.iohao.game.action.skeleton.protocol.wrapper.LongValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import static com.iohao.game.action.skeleton.core.action.ExampleActionCmd.WrapperLongActionCmd;

/**
 * @author 渔民小镇
 * @date 2022-06-26
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WrapperLongTest {

    private CmdInfo getCmdInfo(int subCmd) {
        return CmdInfo.getCmdInfo(WrapperLongActionCmd.cmd, subCmd);
    }

    private FlowContext createLongValueFlowContext(int subCmd) {
        CmdInfo cmdInfo = this.getCmdInfo(subCmd);

        LongValue longValue = new LongValue();
        longValue.value = 100;

        RequestMessage requestMessage = TestDataKit.createRequestMessage(cmdInfo);
        requestMessage.setData(longValue);

        return new FlowContext()
                .setRequest(requestMessage);
    }

    BarSkeleton barSkeleton;

    //    @Before
    public void setUp() {
        barSkeleton = TestDataKit.newBarSkeleton();
    }

    //    @Test
    public void longValue1() {
        FlowContext flowContext = null;
        flowContext = this.createLongValueFlowContext(WrapperLongActionCmd.longValue2Void);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createLongValueFlowContext(WrapperLongActionCmd.longValue2Long);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createLongValueFlowContext(WrapperLongActionCmd.longValue2LongValue);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createLongValueFlowContext(WrapperLongActionCmd.longValue2LongList);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);
    }

    //    @Test
    public void longValue2() {
        FlowContext flowContext = null;
        flowContext = this.createLongValueFlowContext(WrapperLongActionCmd.long2Void);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createLongValueFlowContext(WrapperLongActionCmd.long2Long);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createLongValueFlowContext(WrapperLongActionCmd.long2LongValue);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createLongValueFlowContext(WrapperLongActionCmd.long2LongList);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);
    }

    //    @Test
    public void longerValue3() {
        FlowContext flowContext = null;
        flowContext = this.createLongValueFlowContext(WrapperLongActionCmd.longer2Void);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createLongValueFlowContext(WrapperLongActionCmd.longer2Long);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createLongValueFlowContext(WrapperLongActionCmd.longer2LongValue);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createLongValueFlowContext(WrapperLongActionCmd.longer2LongList);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);
    }
}

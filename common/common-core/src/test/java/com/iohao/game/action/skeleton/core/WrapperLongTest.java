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
import com.iohao.game.action.skeleton.protocol.wrapper.LongPb;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import static com.iohao.game.action.skeleton.core.action.ExampleActionCont.WrapperLongActionCont;

/**
 * @author 渔民小镇
 * @date 2022-06-26
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WrapperLongTest {

    private CmdInfo getCmdInfo(int subCmd) {
        return CmdInfo.getCmdInfo(WrapperLongActionCont.cmd, subCmd);
    }

    private FlowContext createIntPbFlowContext(int subCmd) {
        CmdInfo cmdInfo = this.getCmdInfo(subCmd);

        LongPb intPb = new LongPb();
        intPb.longValue = 100;

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
    public void longPb() {
        FlowContext flowContext = null;
        flowContext = this.createIntPbFlowContext(WrapperLongActionCont.longPb2Void);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperLongActionCont.longPb2Long);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperLongActionCont.longPb2LongPb);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperLongActionCont.longPb2LongList);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);
    }

//    @Test
    public void longValue() {
        FlowContext flowContext = null;
        flowContext = this.createIntPbFlowContext(WrapperLongActionCont.long2Void);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperLongActionCont.long2Long);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperLongActionCont.long2LongPb);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperLongActionCont.long2LongList);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);
    }

//    @Test
    public void longerValue() {
        FlowContext flowContext = null;
        flowContext = this.createIntPbFlowContext(WrapperLongActionCont.longer2Void);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperLongActionCont.longer2Long);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperLongActionCont.longer2LongPb);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);

        flowContext = this.createIntPbFlowContext(WrapperLongActionCont.longer2LongList);
        // 业务框架处理用户请求
        barSkeleton.handle(flowContext);
    }
}

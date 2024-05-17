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

import com.iohao.game.action.skeleton.core.data.TestDataKit;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.wrapper.IntValueList;
import com.iohao.game.action.skeleton.protocol.wrapper.IntValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.junit.Before;
import org.junit.Test;

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

    private CmdInfo of(int subCmd) {
        return CmdInfo.of(WrapperIntActionCmd.cmd, subCmd);
    }

    private FlowContext createIntValueFlowContext(int subCmd) {
        CmdInfo cmdInfo = this.of(subCmd);

        IntValue intValue = new IntValue();
        intValue.value = 100;

        return TestDataKit.ofFlowContext(cmdInfo, intValue);
    }

    BarSkeleton barSkeleton;

    @Before
    public void setUp() {
        barSkeleton = TestDataKit.newBarSkeleton();
    }


    @Test
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

    @Test
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

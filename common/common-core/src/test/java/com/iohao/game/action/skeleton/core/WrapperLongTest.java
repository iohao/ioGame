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
import com.iohao.game.action.skeleton.protocol.wrapper.LongValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.junit.Before;
import org.junit.Test;

import static com.iohao.game.action.skeleton.core.action.ExampleActionCmd.WrapperLongActionCmd;

/**
 * @author 渔民小镇
 * @date 2022-06-26
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WrapperLongTest {

    private CmdInfo of(int subCmd) {
        return CmdInfo.of(WrapperLongActionCmd.cmd, subCmd);
    }

    private FlowContext createLongValueFlowContext(int subCmd) {
        CmdInfo cmdInfo = this.of(subCmd);

        LongValue longValue = new LongValue();
        longValue.value = 100;

        return TestDataKit.ofFlowContext(cmdInfo, longValue);
    }

    BarSkeleton barSkeleton;

    @Before
    public void setUp() {
        barSkeleton = TestDataKit.newBarSkeleton();
    }

    @Test
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

    @Test
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

    @Test
    public void longerValue3() {
        FlowContext flowContext;
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

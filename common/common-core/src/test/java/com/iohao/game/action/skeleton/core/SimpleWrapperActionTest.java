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

import com.iohao.game.action.skeleton.core.action.SimpleWrapperAction;
import com.iohao.game.action.skeleton.core.data.TestDataKit;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.protocol.wrapper.IntValue;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import static com.iohao.game.action.skeleton.core.action.ExampleActionCmd.SimpleWrapperActionActionCmd.*;

/**
 * @author 渔民小镇
 * @date 2024-05-02
 */
@Slf4j
public class SimpleWrapperActionTest {

    BarSkeleton barSkeleton;

    @Before
    public void setUp() {
        barSkeleton = TestDataKit.createBuilder(SimpleWrapperAction.class::equals).build();
    }

    @Test
    public void testInt() {
        CmdInfo cmdInfo = CmdInfo.of(cmd, testInt);

        FlowContext flowContext = TestDataKit.ofFlowContext(cmdInfo, IntValue.of(100));

        barSkeleton.handle(flowContext);
    }
}

package com.iohao.game.action.skeleton.core.flow.internal;

import com.iohao.game.action.skeleton.core.data.TestDataKit;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 渔民小镇
 * @date 2024-10-06
 * @since 21.18
 */
@Slf4j
public class ThreadMonitorInOutTest {

    @Test
    public void fuckIn() {
        FlowContext flowContext = TestDataKit.ofFlowContext();

        ThreadMonitorInOut threadMonitorInOut = new ThreadMonitorInOut();
        threadMonitorInOut.fuckIn(flowContext);

        threadMonitorInOut.fuckOut(flowContext);
        threadMonitorInOut.fuckOut(flowContext);

        var region = threadMonitorInOut.getRegion();
        log.info("region : {}", region);
        Assert.assertEquals(region.map.size(), 1);
    }
}
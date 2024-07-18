package com.iohao.game.action.skeleton.core.flow.internal;

import com.iohao.game.action.skeleton.core.BarMessageKit;
import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.common.kit.RandomKit;
import org.junit.Test;

import java.util.List;

/**
 * @author 渔民小镇
 * @date 2023-11-19
 */
public class StatActionInOutTest {

    @Test
    public void testStatActionInOut() {

        StatActionInOut inOut = new StatActionInOut();
        final StatActionInOut.StatActionRegion region = inOut.region;
        setListener(inOut);

        RequestMessage requestMessage = BarMessageKit.createRequestMessage(CmdInfo.of(1, 1));
        FlowContext flowContext = new FlowContext();
        flowContext.setRequest(requestMessage);
        extracted(region, flowContext);

        requestMessage = BarMessageKit.createRequestMessage(CmdInfo.of(1, 2));
        flowContext.setRequest(requestMessage);
        extracted(region, flowContext);

        region.stream().forEach(System.out::println);
    }

    private void setListener(StatActionInOut inOut) {
        inOut.setListener(new StatActionInOut.StatActionChangeListener() {
            @Override
            public void changed(StatActionInOut.StatAction statAction, long time, FlowContext flowContext) {

            }

            @Override
            public boolean triggerUpdateTimeRange(StatActionInOut.StatAction statAction, long time, FlowContext flowContext) {
                return time >= 500;
//                return false;
            }

            @Override
            public List<StatActionInOut.TimeRange> createTimeRangeList() {
                return List.of(
                        StatActionInOut.TimeRange.create(1000, 1999),
                        StatActionInOut.TimeRange.create(2000, Long.MAX_VALUE, "> 2000"));
            }
        });
    }

    private static void extracted(StatActionInOut.StatActionRegion region, FlowContext flowContext) {
        for (int i = 0; i < 50; i++) {
            // 模拟执行时间
            int time = RandomKit.random(500, 3000);

            region.update(time, flowContext);
        }
    }
}
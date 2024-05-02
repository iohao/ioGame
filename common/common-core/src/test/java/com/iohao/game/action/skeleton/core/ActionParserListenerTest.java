package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.core.action.ExampleActionCmd;
import com.iohao.game.action.skeleton.core.action.pojo.BeeApple;
import com.iohao.game.action.skeleton.core.action.pojo.DogValid;
import com.iohao.game.action.skeleton.core.data.TestDataKit;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author 渔民小镇
 * @date 2024-05-01
 */
@Slf4j
public class ActionParserListenerTest {
    BarSkeleton barSkeleton;

    @Before
    public void setUp() throws InterruptedException {
        BarSkeletonBuilder builder = TestDataKit.createBuilder();

        barSkeleton = builder.build();

        // 等待 protobuf proxy class 加载完成。 see JProtobufParserActionListener
        TimeUnit.MILLISECONDS.sleep(1000);
    }

    @Test
    public void onActionCommand() {
        long l = System.currentTimeMillis();

        extractedBeeHello();
        extractedBeeHelloDog();

        log.info("l : {}", System.currentTimeMillis() - l);
    }

    private void extractedBeeHello() {
        var bizData = new BeeApple();
        bizData.content = "a";

        CmdInfo cmdInfo = CmdInfo.of(ExampleActionCmd.BeeActionCmd.cmd, ExampleActionCmd.BeeActionCmd.hello);
        FlowContext flowContext = TestDataKit.ofFlowContext(cmdInfo, bizData);
        flowContext.inOutStartTime();

        barSkeleton.handle(flowContext);

        var data = flowContext.getResponse().getData(BeeApple.class);

        Assert.assertEquals(data.content, "a，I'm hello");
    }

    private void extractedBeeHelloDog() {
        var bizData = new DogValid();
        bizData.name = "a";

        CmdInfo cmdInfo = CmdInfo.of(ExampleActionCmd.BeeActionCmd.cmd, ExampleActionCmd.BeeActionCmd.hello_dog);
        FlowContext flowContext = TestDataKit.ofFlowContext(cmdInfo, bizData);
        flowContext.inOutStartTime();

        barSkeleton.handle(flowContext);

        var data = flowContext.getResponse().getData(DogValid.class);

        Assert.assertEquals(data.name, "a");
    }
}
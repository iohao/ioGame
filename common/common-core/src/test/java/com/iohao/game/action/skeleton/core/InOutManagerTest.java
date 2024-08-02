package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.core.flow.ActionMethodInOut;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowOption;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 渔民小镇
 * @date 2024-08-02
 */
@Slf4j
public class InOutManagerTest {
    FlowContext flowContext = new FlowContext();
    static FlowOption<List<String>> resultListOption = FlowOption.valueOf("resultListOption");

    @Test
    public void test() {
        extracted(InOutManager.ofAbcAbc(), "ABCABC");
        extracted(InOutManager.ofPipeline(), "ABCCBA");
    }

    private void extracted(InOutManager inOutManager, String result) {

        inOutManager.addInOut(new A_ActionMethodInOut());
        inOutManager.addInOut(new B_ActionMethodInOut());
        inOutManager.addInOut(new C_ActionMethodInOut());

        flowContext.option(resultListOption, new ArrayList<>());
        inOutManager.fuckIn(flowContext);
        inOutManager.fuckOut(flowContext);

        List<String> resultList = flowContext.option(resultListOption);
        var line = String.join("", resultList);
        Assert.assertEquals(line, result);

        System.out.println();
    }
}

@Slf4j
class A_ActionMethodInOut implements ActionMethodInOut {
    @Override
    public void fuckIn(FlowContext flowContext) {
        log.info("A in");
        flowContext.option(InOutManagerTest.resultListOption).add("A");
    }

    @Override
    public void fuckOut(FlowContext flowContext) {
        log.info("A out");
        flowContext.option(InOutManagerTest.resultListOption).add("A");
    }
}

@Slf4j
class B_ActionMethodInOut implements ActionMethodInOut {
    @Override
    public void fuckIn(FlowContext flowContext) {
        log.info("B in");
        flowContext.option(InOutManagerTest.resultListOption).add("B");
    }

    @Override
    public void fuckOut(FlowContext flowContext) {
        log.info("B out");
        flowContext.option(InOutManagerTest.resultListOption).add("B");
    }
}

@Slf4j
class C_ActionMethodInOut implements ActionMethodInOut {
    @Override
    public void fuckIn(FlowContext flowContext) {
        log.info("C in");
        flowContext.option(InOutManagerTest.resultListOption).add("C");
    }

    @Override
    public void fuckOut(FlowContext flowContext) {
        log.info("C out");
        flowContext.option(InOutManagerTest.resultListOption).add("C");
    }
}
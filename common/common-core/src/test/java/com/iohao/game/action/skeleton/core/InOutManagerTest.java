package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.core.flow.ActionMethodInOut;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowOption;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
        List<ActionMethodInOut> inOutList = new ArrayList<>();
        inOutList.add(new A_ActionMethodInOut());
        inOutList.add(new B_ActionMethodInOut());
        inOutList.add(new C_ActionMethodInOut());

        System.out.println("------ 测试多个 inout ------");
        extracted(InOutManager.ofAbcAbc(), "Ain Bin Cin Aout Bout Cout", inOutList);
        extracted(InOutManager.ofPipeline(), "Ain Bin Cin Cout Bout Aout", inOutList);

        // 测试单个 inout
        System.out.println("------ 测试 1 个 inout ------");
        extracted(InOutManager.ofAbcAbc(), "Ain Aout", List.of(new A_ActionMethodInOut()));
        extracted(InOutManager.ofPipeline(), "Ain Aout", List.of(new A_ActionMethodInOut()));

        // 测试 0 个 inout
        System.out.println("------ 测试 0 个 inout ------");
        extracted(InOutManager.ofAbcAbc(), "", Collections.emptyList());
        extracted(InOutManager.ofPipeline(), "", Collections.emptyList());
    }

    private void extracted(InOutManager inOutManager, String result, List<ActionMethodInOut> inOutList) {

        for (ActionMethodInOut actionMethodInOut : inOutList) {
            inOutManager.addInOut(actionMethodInOut);
        }

        flowContext.option(resultListOption, new ArrayList<>());
        inOutManager.fuckIn(flowContext);
        inOutManager.fuckOut(flowContext);

        List<String> resultList = flowContext.option(resultListOption);
        var line = String.join(" ", resultList);
        Assert.assertEquals(line, result);

        System.out.println();
    }
}

@Slf4j
class A_ActionMethodInOut implements ActionMethodInOut {
    @Override
    public void fuckIn(FlowContext flowContext) {
        log.info("A in");
        flowContext.option(InOutManagerTest.resultListOption).add("Ain");
    }

    @Override
    public void fuckOut(FlowContext flowContext) {
        log.info("A out");
        flowContext.option(InOutManagerTest.resultListOption).add("Aout");
    }
}

@Slf4j
class B_ActionMethodInOut implements ActionMethodInOut {
    @Override
    public void fuckIn(FlowContext flowContext) {
        log.info("B in");
        flowContext.option(InOutManagerTest.resultListOption).add("Bin");
    }

    @Override
    public void fuckOut(FlowContext flowContext) {
        log.info("B out");
        flowContext.option(InOutManagerTest.resultListOption).add("Bout");
    }
}

@Slf4j
class C_ActionMethodInOut implements ActionMethodInOut {
    @Override
    public void fuckIn(FlowContext flowContext) {
        log.info("C in");
        flowContext.option(InOutManagerTest.resultListOption).add("Cin");
    }

    @Override
    public void fuckOut(FlowContext flowContext) {
        log.info("C out");
        flowContext.option(InOutManagerTest.resultListOption).add("Cout");
    }
}
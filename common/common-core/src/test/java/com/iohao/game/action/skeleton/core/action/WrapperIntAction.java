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
package com.iohao.game.action.skeleton.core.action;

import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import com.iohao.game.action.skeleton.protocol.wrapper.IntPb;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.iohao.game.action.skeleton.core.action.ExampleActionCmd.WrapperIntActionCmd;

/**
 * @author 渔民小镇
 * @date 2022-06-26
 */
@ActionController(WrapperIntActionCmd.cmd)
public class WrapperIntAction {
    static final Logger log = IoGameLoggerFactory.getLoggerCommonStdout();

    @ActionMethod(WrapperIntActionCmd.intPb2Void)
    public void intPb2Void(IntPb intPb) {
        log.info("intPb : {}", intPb);
    }

    @ActionMethod(WrapperIntActionCmd.intPb2Int)
    public int intPb2Int(IntPb intPb) {
        return intPb.intValue + 1;
    }

    @ActionMethod(WrapperIntActionCmd.intPb2IntPb)
    public IntPb intPb2IntPb(IntPb intPb) {

        IntPb newIntPb = new IntPb();
        newIntPb.intValue = intPb.intValue + 2;

        return newIntPb;
    }

    @ActionMethod(WrapperIntActionCmd.intPb2IntList)
    public List<Integer> intPb2IntList(IntPb intPb) {

        List<Integer> intList = new ArrayList<>();
        intList.add(intPb.intValue);
        intList.add(intPb.intValue + 3);

        return intList;
    }

    @ActionMethod(WrapperIntActionCmd.intListVoid)
    public List<Integer> IntListVoid(List<Integer> intList) {

        log.info("intList : {}", intList);
        return Collections.emptyList();
    }

    /**
     * int
     *
     * @param intPb intPb
     */
    @ActionMethod(WrapperIntActionCmd.int2Void)
    public void int2Void(int intPb) {
    }

    @ActionMethod(WrapperIntActionCmd.int2Int)
    public int int2Int(int intPb) {
        return intPb + 1;
    }

    @ActionMethod(WrapperIntActionCmd.int2IntPb)
    public IntPb int2IntPb(int intPb) {

        IntPb newIntPb = new IntPb();
        newIntPb.intValue = intPb + 2;

        return newIntPb;
    }

    @ActionMethod(WrapperIntActionCmd.int2IntList)
    public List<Integer> int2IntList(int intPb) {
        log.info("intPb : {}", intPb);

        List<Integer> intList = new ArrayList<>();
        intList.add(intPb);
        intList.add(intPb + 3);

        return intList;
    }

    /**
     * integer
     *
     * @param intPb intPb
     */
    @ActionMethod(WrapperIntActionCmd.integer2Void)
    public void integer2Void(Integer intPb) {
    }

    @ActionMethod(WrapperIntActionCmd.integer2Integer)
    public Integer integer2Integer(Integer intPb) {
        return intPb + 1;
    }

    @ActionMethod(WrapperIntActionCmd.integer2IntPb)
    public IntPb integer2IntPb(Integer intPb) {

        IntPb newIntPb = new IntPb();
        newIntPb.intValue = intPb + 2;

        return newIntPb;
    }

    @ActionMethod(WrapperIntActionCmd.integer2IntegerList)
    public List<Integer> integer2IntegerList(Integer intPb) {
        log.info("intPb : {}", intPb);

        List<Integer> intList = new ArrayList<>();
        intList.add(intPb);
        intList.add(intPb + 3);

        return intList;
    }

}

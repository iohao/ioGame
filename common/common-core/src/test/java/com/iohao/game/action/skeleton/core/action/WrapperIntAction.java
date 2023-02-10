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
import com.iohao.game.action.skeleton.protocol.wrapper.IntValue;
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

    @ActionMethod(WrapperIntActionCmd.intValue2Void)
    public void intValue2Void(IntValue intValue) {
        log.info("intValue : {}", intValue);
    }

    @ActionMethod(WrapperIntActionCmd.intValue2Int)
    public int intValue2Int(IntValue intValue) {
        return intValue.value + 1;
    }

    @ActionMethod(WrapperIntActionCmd.intValue2IntValue)
    public IntValue intValue2IntValue(IntValue intValue) {

        IntValue newIntValue = new IntValue();
        newIntValue.value = intValue.value + 2;

        return newIntValue;
    }

    @ActionMethod(WrapperIntActionCmd.intValue2IntList)
    public List<Integer> intValue2IntList(IntValue intValue) {

        List<Integer> intList = new ArrayList<>();
        intList.add(intValue.value);
        intList.add(intValue.value + 3);

        return intList;
    }

    @ActionMethod(WrapperIntActionCmd.intListVoid)
    public List<Integer> IntListVoid(List<Integer> intList) {

        log.info("intList : {}", intList);
        return Collections.emptyList();
    }

    @ActionMethod(WrapperIntActionCmd.int2Void)
    public void int2Void(int intValue) {
    }

    @ActionMethod(WrapperIntActionCmd.int2Int)
    public int int2Int(int intValue) {
        return intValue + 1;
    }

    @ActionMethod(WrapperIntActionCmd.int2IntValue)
    public IntValue int2IntValue(int intValue) {

        IntValue newIntValue = new IntValue();
        newIntValue.value = intValue + 2;

        return newIntValue;
    }

    @ActionMethod(WrapperIntActionCmd.int2IntList)
    public List<Integer> int2IntList(int intValue) {
        log.info("intValue : {}", intValue);

        List<Integer> intList = new ArrayList<>();
        intList.add(intValue);
        intList.add(intValue + 3);

        return intList;
    }


    @ActionMethod(WrapperIntActionCmd.integer2Void)
    public void integer2Void(Integer intValue) {
    }

    @ActionMethod(WrapperIntActionCmd.integer2Integer)
    public Integer integer2Integer(Integer intValue) {
        return intValue + 1;
    }

    @ActionMethod(WrapperIntActionCmd.integer2IntValue)
    public IntValue integer2IntValue(Integer intValue) {

        IntValue newIntValue = new IntValue();
        newIntValue.value = intValue + 2;

        return newIntValue;
    }

    @ActionMethod(WrapperIntActionCmd.integer2IntegerList)
    public List<Integer> integer2IntegerList(Integer intValue) {
        log.info("intValue : {}", intValue);

        List<Integer> intList = new ArrayList<>();
        intList.add(intValue);
        intList.add(intValue + 3);

        return intList;
    }

}

/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
package com.iohao.game.action.skeleton.core.action;

import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import com.iohao.game.action.skeleton.protocol.wrapper.IntValue;
import com.iohao.game.common.consts.IoGameLogName;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.iohao.game.action.skeleton.core.action.ExampleActionCmd.WrapperIntActionCmd;

/**
 * @author 渔民小镇
 * @date 2022-06-26
 */
@Slf4j(topic = IoGameLogName.CommonStdout)
@ActionController(WrapperIntActionCmd.cmd)
public class WrapperIntAction {
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

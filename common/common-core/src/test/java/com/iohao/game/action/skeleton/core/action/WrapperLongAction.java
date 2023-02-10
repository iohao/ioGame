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
import com.iohao.game.action.skeleton.protocol.wrapper.LongValue;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.iohao.game.action.skeleton.core.action.ExampleActionCmd.WrapperLongActionCmd;

/**
 * @author 渔民小镇
 * @date 2022-06-26
 */
@ActionController(WrapperLongActionCmd.cmd)
public class WrapperLongAction {
    static final Logger log = IoGameLoggerFactory.getLoggerCommonStdout();

    @ActionMethod(WrapperLongActionCmd.longValue2Void)
    public void longValue(LongValue longValue) {
        log.info("longValue : {}", longValue);
    }

    @ActionMethod(WrapperLongActionCmd.longValue2Long)
    public long longValue2Long(LongValue longValue) {
        return longValue.value + 1;
    }

    @ActionMethod(WrapperLongActionCmd.longValue2LongValue)
    public LongValue longValue2LongValue(LongValue longValue) {

        LongValue newLongValue = new LongValue();
        newLongValue.value = longValue.value + 2;

        return newLongValue;
    }

    @ActionMethod(WrapperLongActionCmd.longValue2LongList)
    public List<Long> longValue2LongList(LongValue longValue) {

        List<Long> list = new ArrayList<>();
        list.add(longValue.value);
        list.add(longValue.value + 3);

        return list;
    }


    @ActionMethod(WrapperLongActionCmd.long2Void)
    public void long2Void(long value) {
        log.info("value : {}", value);
    }

    @ActionMethod(WrapperLongActionCmd.long2Long)
    public long long2Long(long value) {
        return value + 1;
    }

    @ActionMethod(WrapperLongActionCmd.long2LongValue)
    public LongValue long2LongValue(long value) {

        LongValue newLongValue = new LongValue();
        newLongValue.value = value + 2;

        return newLongValue;
    }

    @ActionMethod(WrapperLongActionCmd.long2LongList)
    public List<Long> long2LongList(long value) {

        List<Long> list = new ArrayList<>();
        list.add(value);
        list.add(value + 3);

        return list;
    }

    @ActionMethod(WrapperLongActionCmd.longer2Void)
    public void longer2Void(LongValue longValue) {
        log.info("longValue : {}", longValue);
    }

    @ActionMethod(WrapperLongActionCmd.longer2Long)
    public long longer2Long(long value) {
        return value + 1;
    }

    @ActionMethod(WrapperLongActionCmd.longer2LongValue)
    public LongValue longer2LongValue(Long value) {

        LongValue newLongValue = new LongValue();
        newLongValue.value = value + 2;

        return newLongValue;
    }

    @ActionMethod(WrapperLongActionCmd.longer2LongList)
    public List<Long> longer2LongList(Long value) {

        List<Long> intList = new ArrayList<>();
        intList.add(value);
        intList.add(value + 3);

        return intList;
    }

}

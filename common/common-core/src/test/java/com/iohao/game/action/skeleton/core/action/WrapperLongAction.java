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
import com.iohao.game.action.skeleton.protocol.wrapper.LongPb;
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

    @ActionMethod(WrapperLongActionCmd.longPb2Void)
    public void longPb2Void(LongPb longPb) {
        log.info("longPb : {}", longPb);
    }

    @ActionMethod(WrapperLongActionCmd.longPb2Long)
    public long longPb2Long(LongPb longPb) {
        return longPb.longValue + 1;
    }

    @ActionMethod(WrapperLongActionCmd.longPb2LongPb)
    public LongPb longPb2LongPb(LongPb intPb) {

        LongPb newLongPb = new LongPb();
        newLongPb.longValue = intPb.longValue + 2;

        return newLongPb;
    }

    @ActionMethod(WrapperLongActionCmd.longPb2LongList)
    public List<Long> longPb2LongList(LongPb intPb) {

        List<Long> intList = new ArrayList<>();
        intList.add(intPb.longValue);
        intList.add(intPb.longValue + 3);

        return intList;
    }

    /**
     * int
     *
     * @param longValue intPb
     */
    @ActionMethod(WrapperLongActionCmd.long2Void)
    public void long2Void(long longValue) {
        log.info("longValue : {}", longValue);
    }

    @ActionMethod(WrapperLongActionCmd.long2Long)
    public long long2Long(long longValue) {
        return longValue + 1;
    }

    @ActionMethod(WrapperLongActionCmd.long2LongPb)
    public LongPb long2LongPb(long longValue) {

        LongPb newIntPb = new LongPb();
        newIntPb.longValue = longValue + 2;

        return newIntPb;
    }

    @ActionMethod(WrapperLongActionCmd.long2LongList)
    public List<Long> long2LongList(long longValue) {

        List<Long> intList = new ArrayList<>();
        intList.add(longValue);
        intList.add(longValue + 3);

        return intList;
    }

    /**
     * integer
     *
     * @param longPb intPb
     */
    @ActionMethod(WrapperLongActionCmd.longer2Void)
    public void longer2Void(LongPb longPb) {
        log.info("longPb : {}", longPb);
    }

    @ActionMethod(WrapperLongActionCmd.longer2Long)
    public long longer2Long(long longValue) {
        return longValue + 1;
    }

    @ActionMethod(WrapperLongActionCmd.longer2LongPb)
    public LongPb longer2LongPb(Long longValue) {

        LongPb newIntPb = new LongPb();
        newIntPb.longValue = longValue + 2;

        return newIntPb;
    }

    @ActionMethod(WrapperLongActionCmd.longer2LongList)
    public List<Long> longer2LongList(Long longValue) {

        List<Long> intList = new ArrayList<>();
        intList.add(longValue);
        intList.add(longValue + 3);

        return intList;
    }

}

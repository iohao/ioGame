/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
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
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.iohao.game.action.skeleton.core.action.ExampleActionCont.WrapperLongActionCont;

/**
 * @author 渔民小镇
 * @date 2022-06-26
 */
@Slf4j
@ActionController(WrapperLongActionCont.cmd)
public class WrapperLongAction {

    @ActionMethod(WrapperLongActionCont.longPb2Void)
    public void longPb2Void(LongPb longPb) {
        log.info("longPb : {}", longPb);
    }

    @ActionMethod(WrapperLongActionCont.longPb2Long)
    public long longPb2Long(LongPb longPb) {
        return longPb.longValue + 1;
    }

    @ActionMethod(WrapperLongActionCont.longPb2LongPb)
    public LongPb longPb2LongPb(LongPb intPb) {

        LongPb newLongPb = new LongPb();
        newLongPb.longValue = intPb.longValue + 2;

        return newLongPb;
    }

    @ActionMethod(WrapperLongActionCont.longPb2LongList)
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
    @ActionMethod(WrapperLongActionCont.long2Void)
    public void long2Void(long longValue) {
        log.info("longValue : {}", longValue);
    }

    @ActionMethod(WrapperLongActionCont.long2Long)
    public long long2Long(long longValue) {
        return longValue + 1;
    }

    @ActionMethod(WrapperLongActionCont.long2LongPb)
    public LongPb long2LongPb(long longValue) {

        LongPb newIntPb = new LongPb();
        newIntPb.longValue = longValue + 2;

        return newIntPb;
    }

    @ActionMethod(WrapperLongActionCont.long2LongList)
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
    @ActionMethod(WrapperLongActionCont.longer2Void)
    public void longer2Void(LongPb longPb) {
        log.info("longPb : {}", longPb);
    }

    @ActionMethod(WrapperLongActionCont.longer2Long)
    public long longer2Long(long longValue) {
        return longValue + 1;
    }

    @ActionMethod(WrapperLongActionCont.longer2LongPb)
    public LongPb longer2LongPb(Long longValue) {

        LongPb newIntPb = new LongPb();
        newIntPb.longValue = longValue + 2;

        return newIntPb;
    }

    @ActionMethod(WrapperLongActionCont.longer2LongList)
    public List<Long> longer2LongList(Long longValue) {

        List<Long> intList = new ArrayList<>();
        intList.add(longValue);
        intList.add(longValue + 3);

        return intList;
    }

}

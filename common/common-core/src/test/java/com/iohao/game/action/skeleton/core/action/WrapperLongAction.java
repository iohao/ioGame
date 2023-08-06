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
import com.iohao.game.action.skeleton.protocol.wrapper.LongValue;
import com.iohao.game.common.consts.IoGameLogName;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.iohao.game.action.skeleton.core.action.ExampleActionCmd.WrapperLongActionCmd;

/**
 * @author 渔民小镇
 * @date 2022-06-26
 */
@Slf4j(topic = IoGameLogName.CommonStdout)
@ActionController(WrapperLongActionCmd.cmd)
public class WrapperLongAction {
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

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
package com.iohao.game.common.kit.abc;

import com.iohao.game.common.consts.IoGameLogName;
import com.iohao.game.common.kit.StrKit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 渔民小镇
 * @date 2022-12-23
 */
@Slf4j(topic = IoGameLogName.CommonStdout)
public class StrTest {
    @Test
    public void test() {
        log.info("args : hello");

        String template = """
                ┏━━━━━ Debug. [({className}.java:{lineNumber}).{actionMethodName}] ━━━ {cmdInfo} ━━━ [逻辑服 [{logicServerTag}] - id:[{logicServerId}]]
                ┣ userId: {userId}
                ┣ 参数: {paramName} : {paramData}
                ┣ 响应: {returnData}
                ┣ 时间: {time} ms (业务方法总耗时)
                ┗━━━━━ Debug  [{className}.java] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                """;

        Map<String, Object> paramMap = new HashMap<>();

        paramMap.put("className", StrTest.class.getSimpleName());
        String s = StrKit.format(template, paramMap);

        System.out.println(s);

        String bb = StrKit.format("aa {} {}", "bb", "c");
        System.out.println(bb);

    }
}

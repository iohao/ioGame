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
package com.iohao.game.common.kit.abc;

import com.iohao.game.common.kit.StrKit;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 渔民小镇
 * @date 2022-12-23
 */
public class StrTest {
    static final Logger log = IoGameLoggerFactory.getLoggerCommonStdout();

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

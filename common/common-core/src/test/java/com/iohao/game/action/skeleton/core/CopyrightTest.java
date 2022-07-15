/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.action.skeleton.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

/**
 * @author 渔民小镇
 * @date 2022-03-10
 */
@Slf4j
public class CopyrightTest {
    public static void main(String[] args) {
        String rightInfo = """
                /*
                 * # iohao.com . 渔民小镇
                 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
                 *
                 * Licensed under the Apache License, Version 2.0 (the "License");
                 * you may not use this file except in compliance with the License.
                 * You may obtain a copy of the License at
                 *
                 *     http://www.apache.org/licenses/LICENSE-2.0
                 *
                 * Unless required by applicable law or agreed to in writing, software
                 * distributed under the License is distributed on an "AS IS" BASIS,
                 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
                 * See the License for the specific language governing permissions and
                 * limitations under the License..
                 */
                """;


        String currentDir = SystemUtil.getUserInfo().getCurrentDir();

        List<File> fileList = FileUtil.loopFiles(currentDir, pathname -> {

            if (!pathname.isFile()) {
                return false;
            }

            if (pathname.getAbsolutePath().contains("/fxgl-tank/")) {
                return false;
            }

            return pathname.getName().endsWith(".java");
        });


        for (File file1 : fileList) {
            System.out.println(file1.getAbsolutePath());
//            String s = FileUtil.readUtf8String(file1);

//            FileUtil.writeUtf8String(rightInfo + s, file1);
//
//            log.info("{} \n{}", file1, s);
        }

        log.info("fileList {}", fileList.size());


    }
}

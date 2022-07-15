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
package com.iohao.game.widget.light.protobuf;

import cn.hutool.system.SystemUtil;
import com.iohao.game.widget.light.protobuf.pojo.TempProtoFile;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author 渔民小镇
 * @date 2022-01-24
 */
@Slf4j
public class ProtoJavaTest {

    public void name() {

        if (!SystemUtil.getOsInfo().isMac()) {
            return;
        }

        // 源码目录
        String protoSourcePath = "/Users/join/gitme/iogame/widget/light-jprotobuf/src/test/java/com/iohao/game/widget/light/protobuf/pojo";
        // 需要扫描的包名
        String protoPackagePath = TempProtoFile.class.getPackageName();
        // 生成 .proto 文件存放的目录
        String generateFolder = "/Users/join/gitme/iogame/widget/light-jprotobuf/target/proto";

        ProtoGenerateFile protoGenerateFile = ProtoGenerateFile.builder()
                .protoSourcePath(protoSourcePath)
                .protoPackagePath(protoPackagePath)
                .generateFolder(generateFolder)
                .build();

        // 生成 .proto 文件
        protoGenerateFile.generate();

        /*
         * 本示例会生成两个 .proto 文件
         * one.proto 和 common.proto
         */
    }
}
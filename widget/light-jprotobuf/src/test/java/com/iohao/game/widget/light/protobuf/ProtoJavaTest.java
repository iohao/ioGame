/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
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

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2022-01-24
 */
@Slf4j
public class ProtoJavaTest {

    public static void main(String[] args) {

        /*
         * .proto 文件生成
         * 相关文档 https://www.yuque.com/iohao/game/vpe2t6
         *
         * 运行该类，会在当前项目 target/proto 目录下生成 .proto 文件
         */

        // 需要扫描的包名
        String protoPackagePath = ProtoJavaTest.class.getPackageName();

        String curDir = System.getProperty("user.dir");
        char[] curDirCharArray = curDir.toCharArray();
        if (Objects.equals(String.valueOf(curDirCharArray[curDirCharArray.length - 1]), File.separator)) {
            curDir = curDir.substring(0, curDir.length() - 1);
        }

        String[] protoSourcePathArray = new String[]{
                curDir
                , "widget"
                , "light-jprotobuf"
                , "src"
                , "test"
                , "java", "com", "iohao", "game", "widget", "light", "protobuf"
        };

        // 源码目录
        String protoSourcePath = String.join(File.separator, protoSourcePathArray);

        String[] generateFolderArray = new String[]{
                System.getProperty("user.dir")
                , "widget"
                , "light-jprotobuf"
                , "target"
                , "proto"
        };

        // 生成 .proto 文件存放的目录
        String generateFolder = String.join(File.separator, generateFolderArray);

        ProtoGenerateFile protoGenerateFile = ProtoGenerateFile.builder()
                // 源码目录
                .protoSourcePath(protoSourcePath)
                // 需要扫描的包名
                .protoPackagePath(protoPackagePath)
                // 生成 .proto 文件存放的目录
                .generateFolder(generateFolder)
                .build();

        // 生成 .proto 文件
        protoGenerateFile.generate();
    }
}
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

import cn.hutool.core.io.FileUtil;
import com.iohao.game.common.kit.StrKit;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

/**
 * 生成 pb 文件
 *
 * @author 渔民小镇
 * @date 2022-01-25
 */
@Slf4j
@Builder
public class ProtoGenerateFile {
    /** pb 源码目录 */
    String protoSourcePath;
    /** pb class 目录 */
    String protoPackagePath;
    /** 生成 proto file 目录 */
    String generateFolder;

    private void checked() {
        Objects.requireNonNull(protoSourcePath);
        Objects.requireNonNull(protoPackagePath);
        Objects.requireNonNull(generateFolder);

        FileUtil.mkdir(this.generateFolder);
    }

    public void generate() {
        checked();

        ProtoJavaAnalyse analyse = new ProtoJavaAnalyse();
        Map<ProtoJavaRegionKey, ProtoJavaRegion> regionMap = analyse.analyse(protoPackagePath, protoSourcePath);

        Consumer<ProtoJavaRegion> javaRegionConsumer = javaRegion -> {
            String fileName = javaRegion.getFileName();

            List<ProtoJava> protoJavaList = javaRegion.getProtoJavaList();

            log.info("fileName: {} - {}", fileName, protoJavaList.size());

            String protoString = javaRegion.toProtoFile();
            log.info("-------------{}---------------------", fileName);
            log.info("{}", protoString);

            String protoFilePath = StrKit.format("{}{}{}"
                    , this.generateFolder
                    , File.separator
                    , fileName
            );

            FileUtil.writeUtf8String(protoString, protoFilePath);
        };

        regionMap.values().forEach(javaRegionConsumer);
    }
}

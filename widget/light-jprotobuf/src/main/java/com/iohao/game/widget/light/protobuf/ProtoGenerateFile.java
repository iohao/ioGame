/*
 * ioGame
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
package com.iohao.game.widget.light.protobuf;

import com.iohao.game.common.kit.StrKit;
import com.iohao.game.common.kit.io.FileKit;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

        FileKit.mkdir(this.generateFolder);
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

            FileKit.writeUtf8String(protoString, protoFilePath);
            log.info("\nprotoFilePath {}", protoFilePath);
        };

        regionMap.values().forEach(javaRegionConsumer);
    }
}

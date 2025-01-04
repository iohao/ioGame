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
import com.iohao.game.common.kit.exception.ThrowKit;
import com.iohao.game.common.kit.io.FileKit;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

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
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProtoGenerateFile {
    /** proto package path */
    final Set<String> protoPackageSet = new HashSet<>();
    /** pb 源码目录 */
    String protoSourcePath;
    /** 生成 proto file 目录 */
    String generateFolder;

    public ProtoGenerateFile addProtoPackage(Collection<String> protoPackageList) {
        this.protoPackageSet.addAll(protoPackageList);
        return this;
    }

    public ProtoGenerateFile addProtoPackage(String packageName) {
        protoPackageSet.add(packageName);
        return this;
    }

    private void checked() {
        Objects.requireNonNull(generateFolder);
        FileKit.mkdir(this.generateFolder);

        if (Objects.isNull(this.protoSourcePath)) {
            this.protoSourcePath = System.getProperty("user.dir");
        }

        if (protoPackageSet.isEmpty()) {
            ThrowKit.ofRuntimeException("protoPackageSet is empty");
        }
    }

    public void generate() {
        checked();

        ProtoJavaAnalyse.getJavaProjectBuilder(protoSourcePath);

        ProtoJavaAnalyse analyse = new ProtoJavaAnalyse();
        Map<ProtoJavaRegionKey, ProtoJavaRegion> regionMap = new NonBlockingHashMap<>();

        protoPackageSet.parallelStream().forEach(protoPackage -> {
            // analyse protoPackage
            regionMap.putAll(analyse.analyse(protoPackage, protoSourcePath));
        });

        Consumer<ProtoJavaRegion> javaRegionConsumer = javaRegion -> {
            var fileName = javaRegion.getFileName();
            var protoJavaList = javaRegion.getProtoJavaList();

            String protoString = javaRegion.toProtoFile();

            if (ProtoGenerateSetting.enableLog) {
                log.info("""
                        ########## {} ########## protoSize:{}
                        {}
                        """, fileName, protoJavaList.size(), protoString);
            }

            String protoFilePath = StrKit.format("{}{}{}"
                    , this.generateFolder
                    , File.separator
                    , fileName
            );

            FileKit.writeUtf8String(protoString, protoFilePath);
            log.info("\nprotoFilePath: {}", protoFilePath);
        };

        regionMap.values().forEach(javaRegionConsumer);
    }

    @Deprecated
    public static ProtoGenerateFileBuilder builder() {
        return new ProtoGenerateFileBuilder();
    }

    @Deprecated
    public static class ProtoGenerateFileBuilder {
        private final ProtoGenerateFile generateFile = new ProtoGenerateFile();
        /** pb 源码目录 */
        String protoSourcePath;
        /** 生成 proto file 目录 */
        String generateFolder;

        public ProtoGenerateFileBuilder protoSourcePath(String protoSourcePath) {
            this.protoSourcePath = protoSourcePath;
            return this;
        }

        public ProtoGenerateFileBuilder generateFolder(String generateFolder) {
            this.generateFolder = generateFolder;
            return this;
        }

        @Deprecated
        public ProtoGenerateFileBuilder protoPackagePath(String protoPackagePath) {
            return this.addProtoPackage(protoPackagePath);
        }

        public ProtoGenerateFileBuilder addProtoPackage(String packageName) {
            generateFile.protoPackageSet.add(packageName);
            return this;
        }

        public ProtoGenerateFileBuilder addProtoPackage(Collection<String> protoPackageList) {
            generateFile.protoPackageSet.addAll(protoPackageList);
            return this;
        }

        public ProtoGenerateFile build() {
            generateFile.generateFolder = this.generateFolder;
            generateFile.protoSourcePath = this.protoSourcePath;

            return generateFile;
        }
    }
}

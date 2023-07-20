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
package com.iohao.game.widget.light.protobuf.kit;

import com.iohao.game.common.kit.ArrayKit;
import com.iohao.game.widget.light.protobuf.ProtoGenerateFile;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.util.regex.Matcher;

/**
 * proto 文件生成工具
 *
 * @author 渔民小镇
 * @date 2023-07-13
 */
@UtilityClass
public class GenerateFileKit {
    /**
     * 生成 proto 文件
     *
     * @param packagePath proto 类所在包名
     */
    public void generate(String packagePath) {
        /*
         * .proto 文件生成
         * 相关文档 https://www.yuque.com/iohao/game/vpe2t6
         *
         * 运行该类，会在当前项目 target/proto 目录下生成 .proto 文件
         */

        // 需要扫描的包名

        String currentDir = System.getProperty("user.dir");
        String[] protoSourcePathArray = new String[]{
                currentDir
                , "src"
                , "main"
                , "java"
                , packagePath.replaceAll("\\.", Matcher.quoteReplacement(File.separator))
        };

        // 源码目录
        String protoSourcePath = ArrayKit.join(protoSourcePathArray, File.separator);

        String[] generateFolderArray = new String[]{
                currentDir
                , "target"
                , "proto"
        };

        // 生成 .proto 文件存放的目录
        String generateFolder = ArrayKit.join(generateFolderArray, File.separator);

        ProtoGenerateFile protoGenerateFile = ProtoGenerateFile.builder()
                // 源码目录
                .protoSourcePath(protoSourcePath)
                // 需要扫描的包名
                .protoPackagePath(packagePath)
                // 生成 .proto 文件存放的目录
                .generateFolder(generateFolder)
                .build();

        // 生成 .proto 文件
        protoGenerateFile.generate();
    }
}

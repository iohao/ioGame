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
package com.iohao.game.widget.light.protobuf.kit;

import com.iohao.game.common.kit.ArrayKit;
import com.iohao.game.widget.light.protobuf.ProtoGenerateFile;
import lombok.experimental.UtilityClass;

import java.io.File;

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
     * @param protoPackagePath proto 类所在包名
     * @param generateFolder   生成 proto file 的目录
     */
    public void generate(String protoPackagePath, String generateFolder) {
        /*
         * .proto 文件生成
         * 相关文档 https://www.yuque.com/iohao/game/vpe2t6
         *
         * 运行该类，会在当前项目 target/proto 目录下生成 .proto 文件
         */

        String currentDir = System.getProperty("user.dir");

        ProtoGenerateFile protoGenerateFile = ProtoGenerateFile.builder()
                // 源码目录
                .protoSourcePath(currentDir)
                // 需要扫描的包名
                .protoPackagePath(protoPackagePath)
                // 生成 .proto 文件存放的目录
                .generateFolder(generateFolder)
                .build();

        // 生成 .proto 文件
        protoGenerateFile.generate();
    }

    /**
     * 生成 proto 文件
     *
     * @param protoPackagePath proto 类所在包名
     */
    public void generate(String protoPackagePath) {
        String currentDir = System.getProperty("user.dir");

        // 生成 .proto 文件存放的目录
        String generateFolder = ArrayKit.join(new String[]{
                currentDir
                , "target"
                , "proto"
        }, File.separator);

        generate(protoPackagePath, generateFolder);
    }
}

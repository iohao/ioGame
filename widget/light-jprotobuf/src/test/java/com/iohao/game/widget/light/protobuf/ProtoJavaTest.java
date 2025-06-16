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

import com.iohao.game.widget.light.protobuf.kit.GenerateFileKit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author 渔民小镇
 * @date 2022-01-24
 */
@Slf4j
public class ProtoJavaTest {
    @Test
    public void generate() {
        /*
         * .proto 文件生成
         *
         * 运行该类，会在当前项目 target/proto 目录下生成 .proto 文件
         */

        // 需要扫描的包名
        String packagePath = ProtoJavaTest.class.getPackageName();
        // .proto 文件生成
        GenerateFileKit.generate(packagePath);
    }
}
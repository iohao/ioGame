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
package com.iohao.game.common.kit.io;

import com.iohao.game.common.kit.adapter.AdapterHuUtils;
import lombok.experimental.UtilityClass;

import java.io.File;

/**
 * @author 渔民小镇
 * @date 2022-12-23
 */
@UtilityClass
public class FileKit {
    /**
     * 创建文件夹，如果存在直接返回此文件夹<br>
     * 此方法不对 File 对象类型做判断，如果 File 不存在，无法判断其类型
     *
     * @param dirPath 文件夹路径，使用 POSIX 格式，无论哪个平台
     * @return 创建的目录
     */
    public File mkdir(String dirPath) {
        return AdapterHuUtils.mkdir(dirPath);
    }

    /**
     * 创建 File 对象，自动识别相对或绝对路径，相对路径将自动从 ClassPath 下寻找
     *
     * @param path 相对 ClassPath 的目录或者绝对路径目录
     * @return File
     */
    public File file(String path) {
        return AdapterHuUtils.file(path);
    }

    /**
     * 将String写入文件，覆盖模式，字符集为UTF-8
     *
     * @param content 写入的内容
     * @param path    文件路径
     * @return 写入的文件
     */
    public File writeUtf8String(String content, String path) {
        return AdapterHuUtils.writeUtf8String(content, path);
    }

    /**
     * 判断是否为目录，如果 path 为 null，则返回 false
     *
     * @param path 文件路径
     * @return 如果为目录 true
     */
    public boolean isDirectory(String path) {
        return AdapterHuUtils.isDirectory(path);
    }

    /**
     * 判断文件是否存在，如果 file 为 null，则返回 false
     *
     * @param file 文件
     * @return 如果存在返回 true
     */
    public static boolean exist(File file) {
        return AdapterHuUtils.exist(file);
    }
}

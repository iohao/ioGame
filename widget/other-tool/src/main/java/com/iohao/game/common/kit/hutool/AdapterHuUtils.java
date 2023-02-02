/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.common.kit.hutool;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
@UtilityClass
public class AdapterHuUtils {
    public String readStr(String resource, Charset charset) {
        return HuResourceUtil.readStr(resource, charset);
    }

    public File mkdir(String dirPath) {
        return HuFileUtil.mkdir(dirPath);
    }

    public File file(String path) {
        return HuFileUtil.file(path);
    }

    public File writeUtf8String(String content, String path) throws HuIoRuntimeException {
        return HuFileUtil.writeUtf8String(content, path);
    }

    public boolean isDirectory(String path) {
        return HuFileUtil.isDirectory(path);
    }

    public static boolean exist(File file) {
        return (null != file) && file.exists();
    }

    public String format(@NonNull CharSequence template, @NonNull Map<?, ?> map) {
        return HuStrUtil.format(template, map);
    }

    public String format(@NonNull CharSequence template, Object... params) {
        return HuStrUtil.format(template, params);
    }

}

/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.common.kit.io;

import cn.hutool.core.io.resource.ResourceUtil;
import lombok.experimental.UtilityClass;

import java.nio.charset.Charset;

/**
 * @author 渔民小镇
 * @date 2022-12-23
 */
@UtilityClass
public class ResourceKit {

    /**
     * 读取Classpath下的资源为字符串
     *
     * @param resource 可以是绝对路径，也可以是相对路径（相对ClassPath）
     * @param charset  编码
     * @return 资源内容
     * @since 3.1.1
     */
    public static String readStr(String resource, Charset charset) {
        return ResourceUtil.readStr(resource, charset);
    }
}

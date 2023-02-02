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
package com.iohao.game.common.kit.system;

import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;

/**
 * copy from hutool
 *
 * @author 渔民小镇
 * @date 2023-01-19
 */
@UtilityClass
class InternalSystemPropsKit {
    Logger log = IoGameLoggerFactory.getLoggerCommonStdout();

    /**
     * 取得系统属性，如果因为Java安全的限制而失败，则将错误打在Log中，然后返回 {@code null}
     *
     * @param name  属性名
     * @param quiet 安静模式，不将出错信息打在{@code System.err}中
     * @return 属性值或{@code null}
     * @see System#getProperty(String)
     * @see System#getenv(String)
     */
    public static String get(String name, boolean quiet) {
        String value = null;
        try {
            value = System.getProperty(name);
        } catch (SecurityException e) {
            if (!quiet) {
                log.error("Caught a SecurityException reading the system property '{}'; the SystemPropsKit property value will default to null.", name);
            }
        }

        if (null == value) {
            try {
                value = System.getenv(name);
            } catch (SecurityException e) {
                if (!quiet) {
                    log.error("Caught a SecurityException reading the system env '{}'; the SystemPropsKit env value will default to null.", name);
                }
            }
        }

        return value;
    }
}

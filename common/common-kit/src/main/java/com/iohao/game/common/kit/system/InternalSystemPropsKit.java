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
package com.iohao.game.common.kit.system;

import com.iohao.game.common.consts.IoGameLogName;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * copy from hutool
 *
 * @author 渔民小镇
 * @date 2023-01-19
 */
@UtilityClass
@Slf4j(topic = IoGameLogName.CommonStdout)
class InternalSystemPropsKit {

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

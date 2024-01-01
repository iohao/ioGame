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
package com.alipay.remoting.log;

import com.alipay.remoting.util.StringUtils;
import com.alipay.sofa.common.log.LoggerSpaceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 兼容 slf4j 2.0.x、logback 1.4.x ...等系列。
 * 原因：LoggerSpaceManager 、MultiAppLoggerSpaceManager 没有提供扩展点。
 * 这里使用 class 覆盖的方式来兼容。
 *
 * @author 渔民小镇
 * @date 2024-01-01
 */
public class BoltLoggerFactory {
    public static final String BOLT_LOG_SPACE_PROPERTY = "bolt.log.space";

    private static String BOLT_LOG_SPACE = "com.alipay.remoting";

    private static final String LOG_PATH = "logging.path";
    private static final String LOG_PATH_DEFAULT = System.getProperty("user.home")
            + File.separator + "logs";
    private static final String CLIENT_LOG_LEVEL = "com.alipay.remoting.client.log.level";
    private static final String CLIENT_LOG_LEVEL_DEFAULT = "INFO";
    private static final String CLIENT_LOG_ENCODE = "com.alipay.remoting.client.log.encode";
    private static final String COMMON_ENCODE = "file.encoding";
    private static final String CLIENT_LOG_ENCODE_DEFAULT = "UTF-8";

    static {
        String logSpace = System.getProperty(BOLT_LOG_SPACE_PROPERTY);
        if (null != logSpace && !logSpace.isEmpty()) {
            BOLT_LOG_SPACE = logSpace;
        }

        String logPath = System.getProperty(LOG_PATH);
        if (StringUtils.isBlank(logPath)) {
            System.setProperty(LOG_PATH, LOG_PATH_DEFAULT);
        }

        String logLevel = System.getProperty(CLIENT_LOG_LEVEL);
        if (StringUtils.isBlank(logLevel)) {
            System.setProperty(CLIENT_LOG_LEVEL, CLIENT_LOG_LEVEL_DEFAULT);
        }

        String commonEncode = System.getProperty(COMMON_ENCODE);
        if (StringUtils.isNotBlank(commonEncode)) {
            System.setProperty(CLIENT_LOG_ENCODE, commonEncode);
        } else {
            String logEncode = System.getProperty(CLIENT_LOG_ENCODE);
            if (StringUtils.isBlank(logEncode)) {
                System.setProperty(CLIENT_LOG_ENCODE, CLIENT_LOG_ENCODE_DEFAULT);
            }
        }
    }

    public static Logger getLogger(Class<?> clazz) {
        if (clazz == null) {
            return getLogger("");
        }

        return getLogger(clazz.getCanonicalName());
    }

    public static Logger getLogger(String name) {
        if (name == null || name.isEmpty()) {
            return LoggerSpaceManager.getLoggerBySpace("", BOLT_LOG_SPACE);
        }

        // 兼容 slf4j 2.0.x、logback 1.4.x 等系列。原因：LoggerSpaceManager 、MultiAppLoggerSpaceManager 没有提供扩展点。
        return LoggerFactory.getLogger(name);
//        return LoggerSpaceManager.getLoggerBySpace(name, BOLT_LOG_SPACE);
    }
}
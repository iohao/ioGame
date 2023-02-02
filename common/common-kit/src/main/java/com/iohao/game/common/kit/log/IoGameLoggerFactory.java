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
package com.iohao.game.common.kit.log;

import ch.qos.logback.classic.LoggerContext;
import com.iohao.game.common.kit.StrKit;
import com.iohao.game.common.log.LoggerSpaceManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.io.File;

/**
 * 日志工厂
 *
 * @author 渔民小镇
 * @date 2023-01-16
 */
@Slf4j
@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IoGameLoggerFactory {
    public final String GAME_LOG_SPACE_PROPERTY = "ioGame.log.space";

    String GAME_LOG_SPACE = "com.iohao.game.common";

    /** 日志存放路径: {user.home}/logs */
    final String LOG_PATH = "logging.path";
    /** default level INFO */
    final String LOG_LEVEL = "com.iohao.game.common.log.level";
    /** default UTF-8 */
    final String LOG_ENCODE = "com.iohao.game.common.log.encode";
    final String COMMON_ENCODE = "file.encoding";

    static {
        String logSpace = System.getProperty(GAME_LOG_SPACE_PROPERTY);
        if (null != logSpace && !logSpace.isEmpty()) {
            GAME_LOG_SPACE = logSpace;
        }

        String logPath = System.getProperty(LOG_PATH);
        if (StrKit.isBlank(logPath)) {
            final String logPathDefault = System.getProperty("user.home") + File.separator + "logs";
            System.setProperty(LOG_PATH, logPathDefault);
        }

        String logLevel = System.getProperty(LOG_LEVEL);
        if (StrKit.isBlank(logLevel)) {
            String logLevelDefault = "INFO";
            System.setProperty(LOG_LEVEL, logLevelDefault);
        }

        String commonEncode = System.getProperty(COMMON_ENCODE);
        if (StrKit.isNotBlank(commonEncode)) {
            System.setProperty(LOG_ENCODE, commonEncode);
        } else {
            String logEncode = System.getProperty(LOG_ENCODE);
            if (StrKit.isBlank(logEncode)) {
                System.setProperty(LOG_ENCODE, "UTF-8");
            }
        }
    }

    public Logger getLogger(Class<?> clazz) {
        if (clazz == null) {
            return getLogger("");
        }
        return getLogger(clazz.getCanonicalName());
    }

    public Logger getLogger(String name) {
        if (name == null || name.isEmpty()) {
            return LoggerSpaceManager.getLoggerBySpace("", GAME_LOG_SPACE);
        }

//        return LoggerSpaceManager.getLoggerBySpace(name, GAME_LOG_SPACE);

//        return a();
        return log;
    }

    private Logger a() {
        LoggerContext loggerContext = new LoggerContext();

        return loggerContext.getLogger("hh");
    }

    public Logger getLoggerConnection() {
        return getLogger("ConnectionDefault");
    }

    public Logger getLoggerCommon() {
        return getLogger("CommonStdout");
        // return getLogger("CommonDefault");
    }

    public Logger getLoggerCommonStdout() {
        return getLogger("CommonStdout");
    }

    public Logger getLoggerCluster() {
        return getLogger("ClusterDefault");
    }

    public Logger getLoggerMsg() {
        return getLogger("MsgTransfer");
    }


}

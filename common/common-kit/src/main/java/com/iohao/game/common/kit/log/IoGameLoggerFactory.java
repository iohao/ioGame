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
package com.iohao.game.common.kit.log;

import com.iohao.game.common.kit.StrKit;
import lombok.AccessLevel;
import lombok.Setter;
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
@Deprecated
@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IoGameLoggerFactory {
    public final String GAME_LOG_SPACE_PROPERTY = "ioGame.log.space";

    String GAME_LOG_SPACE = "com.iohao.game.common";

    @Setter
    String commonDefaultName = "CommonDefault";
    String connectionDefaultName = "ConnectionDefault";

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

    /**
     * 控制台也打印一份日志输出
     */
    public void printConsole() {
        // 控制台也打印一份
        commonDefaultName = "CommonStdout";
        connectionDefaultName = commonDefaultName;
    }

    public Logger getLogger(Class<?> clazz) {
        if (clazz == null) {
            return getLogger("");
        }
        return getLogger(clazz.getCanonicalName());
    }

    public Logger getLogger(String name) {
        return log;
//        if (name == null || name.isEmpty()) {
//            return LoggerSpaceManager.getLoggerBySpace("", GAME_LOG_SPACE);
//        }
//
//        return LoggerSpaceManager.getLoggerBySpace(name, GAME_LOG_SPACE);
    }

    public Logger getLoggerConnection() {
        return getLogger(connectionDefaultName);
    }

    public Logger getLoggerCommon() {
        return getLogger(commonDefaultName);
    }

    public Logger getLoggerExternal() {
        return getLogger("CommonExternal");
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

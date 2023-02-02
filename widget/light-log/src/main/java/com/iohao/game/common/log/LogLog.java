/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.common.log;

import com.iohao.game.common.log.utils.SofaStringUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

import static com.iohao.game.common.log.Constants.SOFA_MIDDLEWARE_LOG_INTERNAL_LEVEL;

/**
 * @author 渔民小镇
 * @date 2023-01-31
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogLog {
    static final String DEBUG_PREFIX = "Sofa-Middleware-Log:DEBUG  ";
    static final String INFO_PREFIX = "Sofa-Middleware-Log:INFO ";
    static final String ERR_PREFIX = "Sofa-Middleware-Log:ERROR ";
    static final String WARN_PREFIX = "Sofa-Middleware-Log:WARN ";

    static final Map<String, Level> LEVELS = new HashMap<>();

    volatile static Level consoleLogLevel = Level.WARN;

    static {
        LEVELS.put("DEBUG", Level.DEBUG);
        LEVELS.put("INFO", Level.INFO);
        LEVELS.put("WARN", Level.WARN);
        LEVELS.put("ERROR", Level.ERROR);
    }

    public static void setConsoleLevel(String level) {
        Level value = LEVELS.get(level.toUpperCase());
        if (!SofaStringUtil.isBlank(level) && value != null) {
            consoleLogLevel = value;
        }
    }

    public static void debug(String msg) {
        if (isDebug()) {
            System.out.println(DEBUG_PREFIX + msg);
        }
    }

    public static void info(String msg) {
        if (isInfo()) {
            System.out.println(INFO_PREFIX + msg);
        }
    }

    public static void warn(String msg) {
        if (isWarn()) {
            System.err.println(WARN_PREFIX + msg);
        }
    }

    public static void warn(String msg, Throwable e) {
        if (isWarn()) {
            System.err.println(WARN_PREFIX + msg);
            if (e != null) {
                e.printStackTrace();
            }
        }
    }

    public static void error(String msg, Throwable throwable) {
        if (isError()) {
            System.err.println(ERR_PREFIX + msg);
            if (throwable != null) {
                throwable.printStackTrace();
            }
        }
    }

    private static boolean isDebug() {
        setConsoleLevel(System.getProperty(SOFA_MIDDLEWARE_LOG_INTERNAL_LEVEL, "WARN"));
        return consoleLogLevel.equals(Level.DEBUG);
    }

    private static boolean isInfo() {
        return isDebug() || consoleLogLevel.equals(Level.INFO);
    }

    private static boolean isWarn() {
        return isInfo() || consoleLogLevel.equals(Level.WARN);
    }

    private static boolean isError() {
        return isWarn() || consoleLogLevel.equals(Level.ERROR);
    }

    enum Level {
        DEBUG, INFO, WARN, ERROR
    }
}

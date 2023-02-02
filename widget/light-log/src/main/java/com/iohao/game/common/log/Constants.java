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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author 渔民小镇
 * @date 2023-01-31
 */
public interface Constants {
    /** default logger, use ROOT logger if not configured in app. */
    Logger DEFAULT_LOG = LoggerFactory.getLogger("com.alipay.sofa.common.log");
    String LOG_START = "*";
    String LOG_DIRECTORY = "log";
    String LOG_XML_CONFIG_FILE_NAME = "log-conf.xml";
    String LOG_XML_CONFIG_FILE_ENV_PATTERN = "log-conf-%s.xml";
    String LOG_CONFIG_PROPERTIES = "config.properties";
    String PRIORITY_KEY = "priority";
    String LOGGER_CONSOLE_WHITE_SET_KEY = "console";
    String LOGGER_CONSOLE_PREFIX_WHITE_SET_KEY = "console.prefix";

    String LOG_PATH = "logging.path";
    String LOG_PATH_PREFIX = "logging.path.";

    String OLD_LOG_PATH = "loggingRoot";
    String LOG_LEVEL = "logging.level";
    String LOG_LEVEL_PREFIX = "logging.level.";
    String LOG_CONFIG_PREFIX = "logging.config.";
    String DEFAULT_MIDDLEWARE_SPACE_LOG_LEVEL = "INFO";
    String IS_DEFAULT_LOG_PATH = "isDefaultLogPath";
    String IS_DEFAULT_LOG_LEVEL = "isDefaultLogLevel";
    /**
     * specify log conf file according different environment, such as log-conf-dev.xml
     * value pattern is similar to log.env.suffix=spaceName:dev&spaceName:test
     */
    String LOG_ENV_SUFFIX = "log.env.suffix";
    /** file encoding configured by VM option */
    String LOG_ENCODING_PROP_KEY = "file.encoding";
    /** disable space log */
    String SOFA_MIDDLEWARE_LOG_DISABLE_PROP_KEY = "sofa.middleware.log.disable";

    boolean SOFA_MIDDLEWARE_LOG_DISABLE = Boolean.TRUE
            .toString()
            .equalsIgnoreCase(System.getProperty(SOFA_MIDDLEWARE_LOG_DISABLE_PROP_KEY));

    /** disable log4j bridge to commons logging */
    String LOG4J_COMMONS_LOGGING_MIDDLEWARE_LOG_DISABLE_PROP_KEY = "log4j.commons.logging.middleware.log.disable";

    boolean LOG4J_COMMONS_LOGGING_MIDDLEWARE_LOG_DISABLE = Boolean.TRUE
            .toString()
            .equalsIgnoreCase(System.getProperty(LOG4J_COMMONS_LOGGING_MIDDLEWARE_LOG_DISABLE_PROP_KEY));

    /** disable log4j2 space factory */
    String LOG4J2_MIDDLEWARE_LOG_DISABLE_PROP_KEY = "log4j2.middleware.log.disable";

    boolean LOG4J2_MIDDLEWARE_LOG_DISABLE = Boolean.TRUE
            .toString()
            .equalsIgnoreCase(System.getProperty(LOG4J2_MIDDLEWARE_LOG_DISABLE_PROP_KEY));

    /** disable logback space factory */
    String LOGBACK_MIDDLEWARE_LOG_DISABLE_PROP_KEY = "logback.middleware.log.disable";

    boolean LOGBACK_MIDDLEWARE_LOG_DISABLE = Boolean.TRUE
            .toString()
            .equalsIgnoreCase(System.getProperty(LOGBACK_MIDDLEWARE_LOG_DISABLE_PROP_KEY));

    String UTF8_STR = "UTF-8";
    /** default log path */
    String LOGGING_PATH_DEFAULT = System.getProperty("user.home") + File.separator + "logs";
    /** default priority, Larger numbers indicate higher priority */
    int DEFAULT_PRIORITY = 0;

    String PROCESS_MARKER = "PID";
    /** config prefix */
    String SOFA_MIDDLEWARE_CONFIG_PREFIX = "sofa.middleware.log.";
    /** global switch to log on console */
    String SOFA_MIDDLEWARE_ALL_LOG_CONSOLE_SWITCH = "sofa.middleware.log.console";
    /** single space switch to log on console */
    String SOFA_MIDDLEWARE_SINGLE_LOG_CONSOLE_SWITCH = "sofa.middleware.log.%s.console";
    /** console string */
    String CONSOLE_SUFFIX = ".console";
    /**
     * sofa-common-tools 自身日志开关
     * internal log level config.
     */
    String SOFA_MIDDLEWARE_LOG_INTERNAL_LEVEL = "sofa.middleware.log.internal.level";
    /** global console log level */
    String SOFA_MIDDLEWARE_ALL_LOG_CONSOLE_LEVEL = "sofa.middleware.log.console.level";
    /** single space console log level */
    String SOFA_MIDDLEWARE_SINGLE_LOG_CONSOLE_LEVEL = "sofa.middleware.log.%s.console.level";
    /** logback log pattern on console */
    String SOFA_MIDDLEWARE_LOG_CONSOLE_LOGBACK_PATTERN = "sofa.middleware.log.console.logback.pattern";
    /** log4j2 log pattern on console */
    String SOFA_MIDDLEWARE_LOG_CONSOLE_LOG4J2_PATTERN = "sofa.middleware.log.console.log4j2.pattern";
    /** default logback log pattern */
    String SOFA_MIDDLEWARE_LOG_CONSOLE_LOGBACK_PATTERN_DEFAULT = "%d{yyyy-MM-dd HH:mm:ss.SSS} %5p ${PID:- } --- [%15.15t] %-40.40logger{39} : %m%n";
    /** default log4j2 log pattern */
    String SOFA_MIDDLEWARE_LOG_CONSOLE_LOG4J2_PATTERN_DEFAULT = "%d{yyyy-MM-dd HH:mm:ss.SSS} %5p %X{PID} --- [%15.15t] %-40.40logger{39} : %m%n";
    /** logging path file */
    String LOGGING_CONFIG_PATH = "logging.config.%s";

    String  LOG4J_MIDDLEWARE_LOG_DISABLE_PROP_KEY                 = "log4j.middleware.log.disable";

    boolean LOG4J_MIDDLEWARE_LOG_DISABLE                          = Boolean.TRUE
            .toString()
            .equalsIgnoreCase(
                    System
                            .getProperty(LOG4J_MIDDLEWARE_LOG_DISABLE_PROP_KEY));

}

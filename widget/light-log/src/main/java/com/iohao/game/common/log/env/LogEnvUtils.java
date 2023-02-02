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
package com.iohao.game.common.log.env;

import com.iohao.game.common.log.CommonLoggingConfigurations;
import com.iohao.game.common.log.utils.*;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static com.iohao.game.common.log.Constants.*;

/**
 * @author 渔民小镇
 * @date 2023-01-31
 */
@UtilityClass
public class LogEnvUtils {

    private static volatile Map<String, String> globalSystemProperties;

    private static volatile boolean useDefaultSystemProperties;

    public boolean isLogbackUsable(ClassLoader spaceClassloader) {
        SofaAssertUtil.notNull(spaceClassloader);
        try {
            return spaceClassloader.loadClass("ch.qos.logback.classic.LoggerContext") != null;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public boolean isLog4j2Usable(ClassLoader spaceClassloader) {
        SofaAssertUtil.notNull(spaceClassloader);
        try {
            return (spaceClassloader.loadClass("org.apache.logging.slf4j.Log4jLoggerFactory") != null);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public boolean isLog4jUsable(ClassLoader spaceClassloader) {
        SofaAssertUtil.notNull(spaceClassloader);
        try {
            return (spaceClassloader.loadClass("org.slf4j.impl.Log4jLoggerFactory") != null);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /***
     * slf4j-jcl
     * commons-logger
     *
     * 默认使用 log4j 作为初始化实现
     * @param spaceClassloader
     * @return
     */
    public boolean isCommonsLoggingUsable(ClassLoader spaceClassloader) {
        SofaAssertUtil.notNull(spaceClassloader);
        try {
            return (spaceClassloader.loadClass("org.slf4j.impl.JCLLoggerAdapter") != null
                    && (spaceClassloader.loadClass("org.apache.commons.logging.impl.Log4JLogger") != null)
                    && (spaceClassloader.loadClass("org.apache.log4j.Logger") != null) && (spaceClassloader
                    .loadClass("org.apache.commons.logging.Log") != null));
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Processing global configuration for sofa-common-logging, priorities as follows:
     * 1. JVM System Properties
     * 2. OS Env Properties
     * 3. External Configurations
     * <p>
     * Some configurations specific to SOFA:
     * 1. logging.path
     * 2. loggingRoot
     * 3. file.encoding, defaults to UTF-8
     * 4. PID
     */
    public Map<String, String> processGlobalSystemLogProperties() {
        if (globalSystemProperties != null) {
            return globalSystemProperties;
        }

        // Firstly, Load from external configurations
        Map<String, String> properties = new HashMap<>(CommonLoggingConfigurations.getExternalConfigurations());

        // Secondly, process configurations from OS env
        for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
            String lowerCaseKey = entry.getKey().toLowerCase();
            if (isSofaCommonLoggingPrefix(lowerCaseKey)) {
                properties.put(lowerCaseKey, entry.getValue());
            }
        }

        // Thirdly, process configurations from JVM System Properties
        final Properties systemProperties = System.getProperties();
        final Set<String> sysKeys = systemProperties.stringPropertyNames();
        for (String key : sysKeys) {
            String value = systemProperties.getProperty(key);
            if (key == null || value == null) {
                continue;
            }
            String lowerCaseKey = key.toLowerCase();
            if (isSofaCommonLoggingPrefix(lowerCaseKey)) {
                properties.put(lowerCaseKey, value);
            }
        }

        properties.put(PROCESS_MARKER, SofaProcessIdUtil.getProcessId());
        if (System.getProperties().contains(LOG_ENCODING_PROP_KEY)) {
            properties.put(LOG_ENCODING_PROP_KEY, System.getProperty(LOG_ENCODING_PROP_KEY));
        } else {
            properties.putIfAbsent(LOG_ENCODING_PROP_KEY, UTF8_STR);
        }

        // logging.path has priority over loggingRoot
        String loggingPath = System.getProperty(LOG_PATH);
        String loggingRoot = System.getProperty(OLD_LOG_PATH);
        if (!SofaStringUtil.isBlank(loggingPath)) {
            loggingRoot = loggingPath;
        } else if (!SofaStringUtil.isBlank(loggingRoot)) {
            loggingPath = loggingRoot;
        }

        if (SofaStringUtil.isNotEmpty(loggingPath)) {
            properties.put(LOG_PATH, loggingPath);
            properties.put(OLD_LOG_PATH, loggingRoot);
            useDefaultSystemProperties = false;
        } else {
            // Defaults to $HOME/logs
            properties.putIfAbsent(LOG_PATH, LOGGING_PATH_DEFAULT);
            properties.putIfAbsent(OLD_LOG_PATH, LOGGING_PATH_DEFAULT);
            useDefaultSystemProperties = true;
        }

        globalSystemProperties = properties;
        keepCompatible(globalSystemProperties, !isLogStarterExist());
        return globalSystemProperties;
    }

    public String getLogConfEnvSuffix(String spaceName) {
        String logEnvConfig = System.getProperty(LOG_ENV_SUFFIX, SofaStringUtil.EMPTY_STRING);
        String[] spaceNameToEnvSuffix = logEnvConfig.split("&");
        String suffix = null;
        for (int i = 0; i < spaceNameToEnvSuffix.length && suffix == null; ++i) {
            String envConf = spaceNameToEnvSuffix[i];
            String[] conf = envConf.split(":");

            /* 配置格式错误或者不是相同的 spaceName, 直接 skip */
            if (conf.length != 2 || !conf[0].equals(spaceName)) {
                continue;
            } else if (!conf[1].isEmpty()) {
                /* .dev .test .product */
                conf[1] = "." + conf[1];
            }

            suffix = conf[1];
        }

        suffix = (suffix == null) ? SofaStringUtil.EMPTY_STRING : suffix;
        if (!suffix.isEmpty()) {
            SofaReportUtil.reportDebug(spaceName + " log configuration: " + LOG_XML_CONFIG_FILE_NAME
                    + suffix);
        }
        return suffix;
    }

    /**
     * keep compatible with previous version. Set system properties of the following attributes:
     * logging.path
     * loggingRoot
     * file.encoding
     */
    public void keepCompatible(Map<String, String> context, boolean keep) {
        if (!keep) {
            return;
        }

        String loggingPath = System.getProperty(LOG_PATH, context.get(LOG_PATH));
        String fileEncoding = System.getProperty(LOG_ENCODING_PROP_KEY,
                context.get(LOG_ENCODING_PROP_KEY));
        System.setProperty(LOG_PATH, loggingPath);
        System.setProperty(OLD_LOG_PATH, System.getProperty(OLD_LOG_PATH, loggingPath));
        System.setProperty(LOG_ENCODING_PROP_KEY, fileEncoding);
    }

    public boolean isLogStarterExist() {
        return SofaClassLoaderUtil.isPresent("com.alipay.sofa.common.boot.logging.Mark");
    }

    private boolean isSofaCommonLoggingPrefix(String key) {
        return key.startsWith(SOFA_MIDDLEWARE_CONFIG_PREFIX)
                || key.startsWith(LOG_LEVEL_PREFIX)
                || key.startsWith(LOG_PATH_PREFIX)
                || key.startsWith(LOG_CONFIG_PREFIX);
    }

    public boolean isSofaCommonLoggingConfig(String key) {
        return isSofaCommonLoggingPrefix(key)
                || key.equals(LOG_PATH)
                || key.equals(OLD_LOG_PATH)
                || key.equals(LOG_ENCODING_PROP_KEY);
    }

    public void clearGlobalSystemProperties() {
        globalSystemProperties = null;
    }

    public boolean isUseDefaultSystemProperties() {
        return useDefaultSystemProperties;
    }
}

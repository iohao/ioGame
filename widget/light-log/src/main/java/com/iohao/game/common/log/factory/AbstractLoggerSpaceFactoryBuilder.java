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
package com.iohao.game.common.log.factory;

import com.iohao.game.common.log.utils.SofaAssertUtil;
import com.iohao.game.common.log.CommonLoggingConfigurations;
import com.iohao.game.common.log.Constants;
import com.iohao.game.common.log.LogSpace;
import com.iohao.game.common.log.env.LogEnvUtils;
import com.iohao.game.common.log.space.SpaceId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import com.iohao.game.common.log.utils.SofaReportUtil;
import com.iohao.game.common.log.utils.SofaStringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import static com.iohao.game.common.log.Constants.*;

/**
 * @author 渔民小镇
 * @date 2023-01-31
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractLoggerSpaceFactoryBuilder implements LoggerSpaceFactoryBuilder {
    SpaceId spaceId;
    LogSpace logSpace;

    String spaceDirectoryPrefix;

    public AbstractLoggerSpaceFactoryBuilder(SpaceId spaceId, LogSpace space) {
        SofaAssertUtil.notNull(space);
        SofaAssertUtil.notNull(spaceId);
        this.spaceId = spaceId;
        this.logSpace = space;
        spaceDirectoryPrefix = spaceId.getSpaceName().replace('.', '/') + "/" + LOG_DIRECTORY + "/"
                + getLoggingToolName() + "/";
    }

    @Override
    public AbstractLoggerSpaceFactory build(String spaceName, ClassLoader spaceClassloader) {
        SofaAssertUtil.hasText(spaceName);
        SofaAssertUtil.notNull(spaceClassloader);
        // load config file
        URL configFileUrl = getSpaceLogConfigFileURL(spaceClassloader, spaceName);
        // set default logging.level and logging.path
        specifySpaceLogConfigProperties(spaceName);
        return doBuild(spaceName, spaceClassloader, configFileUrl);
    }

    private List<URL> getResources(ClassLoader classLoader, String path) {
        List<URL> rtn = new ArrayList<>();
        try {
            Enumeration<URL> allUrls = classLoader.getResources(path);
            if (allUrls != null) {
                while (allUrls.hasMoreElements()) {
                    rtn.add(allUrls.nextElement());
                }
            }
        } catch (IOException e) {
            SofaReportUtil.reportWarn("Fail to get resource of " + path + " from classpath", e);
            return Collections.emptyList();
        }
        return rtn;
    }

    protected URL getSpaceLogConfigFileURL(ClassLoader spaceClassloader, String spaceName) {
        String loggingConfig = logSpace.getProperty(String.format(Constants.LOGGING_CONFIG_PATH,
                spaceId.getSpaceName()));
        if (SofaStringUtil.isNotEmpty(loggingConfig)) {
            return spaceClassloader.getResource(loggingConfig);
        }

        String suffix = LogEnvUtils.getLogConfEnvSuffix(spaceName);

        String logConfigLocation = spaceDirectoryPrefix + LOG_XML_CONFIG_FILE_NAME + suffix;

        String logConfigPropertyLocation = spaceDirectoryPrefix + LOG_CONFIG_PROPERTIES + suffix;

        URL configFileUrl = null;
        List<URL> logConfigFileUrls = getResources(spaceClassloader, logConfigLocation);
        List<URL> logConfigPropertyFileUrls = getResources(spaceClassloader,
                logConfigPropertyLocation);
        try {
            configFileUrl = getResourceByPriority(logConfigFileUrls, logConfigPropertyFileUrls);

            // recommend this pattern "log-conf-console.xml"
            if (configFileUrl == null && SofaStringUtil.isNotEmpty(suffix)) {
                // try again with another env profile file pattern;
                logConfigLocation = spaceDirectoryPrefix
                        + String.format(LOG_XML_CONFIG_FILE_ENV_PATTERN,
                        suffix.substring(1));
                configFileUrl = spaceClassloader.getResource(logConfigLocation);
            }
        } catch (Exception e) {
            SofaReportUtil.reportError("Error when get resources of " + spaceName + " from classpath.",
                    e);
        }

        SofaAssertUtil.state(configFileUrl != null, this + " build error: No " + getLoggingToolName()
                + " config file (" + configFileUrl + ") found!");
        return configFileUrl;
    }

    protected URL getResourceByPriority(List<URL> logConfigFileUrls, List<URL> logConfigPropertyFileUrls) throws IOException {
        if (logConfigFileUrls == null || logConfigFileUrls.isEmpty()) {
            return null;
        } else {
            List<ConfigFile> configFiles = new ArrayList<>();
            for (URL logConfigUrl : logConfigFileUrls) {
                int priority = DEFAULT_PRIORITY;

                if (logConfigPropertyFileUrls != null) {
                    for (URL configPropertyUrl : logConfigPropertyFileUrls) {
                        final String absoluteConfigPath = new File(configPropertyUrl.getFile())
                                .getParentFile().getAbsolutePath();
                        final String absoluteLogPath = new File(logConfigUrl.getFile())
                                .getParentFile().getAbsolutePath();
                        if (absoluteConfigPath.equals(absoluteLogPath)) {
                            InputStream inputStream = null;
                            try {
                                URLConnection uConn = configPropertyUrl.openConnection();
                                uConn.setUseCaches(false);
                                inputStream = uConn.getInputStream();
                                Properties properties = new Properties();
                                properties.load(inputStream);
                                String priorityStr = properties.getProperty(PRIORITY_KEY);
                                if (SofaStringUtil.isNotEmpty(priorityStr)) {
                                    priority = Integer.parseInt(priorityStr);
                                }

                                String loggerConsoleWhiteSetStr = properties.getProperty(LOGGER_CONSOLE_WHITE_SET_KEY);
                                if (SofaStringUtil.isNotEmpty(loggerConsoleWhiteSetStr)) {
                                    for (String logger : loggerConsoleWhiteSetStr.split(",")) {
                                        CommonLoggingConfigurations.appendConsoleLoggerName(logger);
                                    }
                                }

                                String loggerConsolePrefixSetStr = properties.getProperty(LOGGER_CONSOLE_PREFIX_WHITE_SET_KEY);
                                if (SofaStringUtil.isNotEmpty(loggerConsolePrefixSetStr)) {
                                    for (String logger : loggerConsolePrefixSetStr.split(",")) {
                                        CommonLoggingConfigurations.appendConsolePrefixWhiteLoggerName(logger);
                                    }
                                }
                            } finally {
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                            }
                            break;
                        }
                    }
                }

                ConfigFile configFile = new ConfigFile(priority, logConfigUrl);
                SofaReportUtil.reportDebug("Find URL " + logConfigUrl + ", priority is " + priority);
                configFiles.add(configFile);
            }

            // In descending order
            configFiles.sort((o1, o2) -> -Integer.compare(o1.priority, o2.priority));
            return configFiles.get(0).url;
        }
    }

    private void specifySpaceLogConfigProperties(String spaceName) {
        /*
         * 1.space's logger path
         */
        String loggingPathKey = LOG_PATH_PREFIX + spaceName;
        String defaultLoggingPath = logSpace.getProperty(LOG_PATH);
        if (logSpace.getProperty(loggingPathKey) == null) {
            logSpace.setProperty(IS_DEFAULT_LOG_PATH, Boolean.TRUE.toString());
            logSpace.setProperty(loggingPathKey, defaultLoggingPath);
        }

        /*
         * 2.space's logger level
         */
        String loggingLevelKey = LOG_LEVEL_PREFIX + spaceName;
        if (logSpace.getProperty(loggingLevelKey) == null) {
            logSpace.setProperty(IS_DEFAULT_LOG_LEVEL, Boolean.TRUE.toString());
            logSpace.setProperty(loggingLevelKey, DEFAULT_MIDDLEWARE_SPACE_LOG_LEVEL);
            for (int i = LOG_LEVEL.length(); i < loggingLevelKey.length(); ++i) {
                if (loggingLevelKey.charAt(i) == '.') {
                    String level = logSpace.getProperty(loggingLevelKey.substring(0, i + 1)
                            + LOG_START);
                    if (!SofaStringUtil.isBlank(level)) {
                        logSpace.setProperty(loggingLevelKey, level);
                    }
                }
            }
        }

    }

    protected abstract AbstractLoggerSpaceFactory doBuild(String spaceName,
                                                          ClassLoader spaceClassloader,
                                                          URL confFileUrl);

    protected abstract String getLoggingToolName();

    protected SpaceId getSpaceId() {
        return spaceId;
    }

    protected Properties getProperties() {
        return logSpace.properties();
    }

    private record ConfigFile(int priority, URL url) {
    }
}

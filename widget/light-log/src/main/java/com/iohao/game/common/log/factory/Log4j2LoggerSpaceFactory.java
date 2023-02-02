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

import com.iohao.game.common.log.CommonLoggingConfigurations;
import com.iohao.game.common.log.Constants;
import com.iohao.game.common.log.adapter.level.AdapterLevel;
import com.iohao.game.common.log.space.SpaceId;
import com.iohao.game.common.log.utils.SofaStringUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.slf4j.Log4jLogger;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author 渔民小镇
 * @date 2023-01-31
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Log4j2LoggerSpaceFactory extends AbstractLoggerSpaceFactory {

    final ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap<>();
    final SpaceId spaceId;
    final Properties properties;
    final LoggerContext loggerContext;
    final URL confFile;

    /**
     * key: loggerName, value: consoleAppender
     * each logger have their own consoleAppender if had configured
     **/
    final ConcurrentMap<String, ConsoleAppender> consoleAppendMap = new ConcurrentHashMap<>();

    /**
     * @param source logback,log4j2,log4j,temp,nop
     */
    public Log4j2LoggerSpaceFactory(SpaceId spaceId, Properties properties, URL confFile,
                                    String source) throws Throwable {
        super(source);
        this.spaceId = spaceId;
        this.properties = properties;
        this.confFile = confFile;
        this.loggerContext = initialize();
        attachConsoleAppender();
    }

    private LoggerContext initialize() throws Throwable {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            ThreadContext.put((String) entry.getKey(),
                    properties.getProperty((String) entry.getKey()));
        }

        LoggerContext context = new LoggerContext(spaceId.getSpaceName(), null, confFile.toURI());

        Configuration config;
        ConfigurationFactory configurationFactory = ConfigurationFactory.getInstance();

        try {
            // log4j-core 2.3 version
            Method getConfigurationMethod = configurationFactory.getClass().getMethod(
                    "getConfiguration", String.class, URI.class, ClassLoader.class);
            config = (Configuration) getConfigurationMethod.invoke(configurationFactory,
                    spaceId.getSpaceName(), confFile.toURI(), this.getClass().getClassLoader());
        } catch (NoSuchMethodException noSuchMethodException) {
            // log4j-core 2.7+ version
            Method getConfigurationMethod = configurationFactory.getClass().getMethod(
                    "getConfiguration", LoggerContext.class, String.class, URI.class, ClassLoader.class);
            config = (Configuration) getConfigurationMethod.invoke(configurationFactory, context,
                    spaceId.getSpaceName(), confFile.toURI(), this.getClass().getClassLoader());
        }

        if (config == null) {
            throw new RuntimeException("No log4j2 configuration are found.");
        }

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            config.getProperties().put((String) entry.getKey(), (String) entry.getValue());
        }

        final Properties systemProperties = System.getProperties();
        final Set<String> sysKeys = systemProperties.stringPropertyNames();

        for (String key : sysKeys) {
            String value = systemProperties.getProperty(key);
            if (key == null || value == null) {
                continue;
            }
            config.getProperties().put(key, value);
        }

        context.start(config);
        return context;
    }

    private void attachConsoleAppender() {
        String value = properties.getProperty(String.format(
                Constants.SOFA_MIDDLEWARE_SINGLE_LOG_CONSOLE_SWITCH, spaceId.getSpaceName()));

        if (SofaStringUtil.isEmpty(value)) {
            value = properties.getProperty(Constants.SOFA_MIDDLEWARE_ALL_LOG_CONSOLE_SWITCH);
        }

        if (Boolean.TRUE.toString().equalsIgnoreCase(value)) {
            loggerContext.addFilter(new AbstractFilter() {
                private void process(org.apache.logging.log4j.core.Logger logger) {
                    ConsoleAppender appender = getOrCreateConsoleAppender(logger.getName());
                    if (CommonLoggingConfigurations.shouldAttachConsoleAppender(logger.getName())
                            && !logger.getAppenders().containsKey(AbstractLoggerSpaceFactory.CONSOLE)) {
                        logger.addAppender(appender);
                    }
                }

                @Override
                public Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, Message msg,
                                     Throwable t) {
                    process(logger);
                    return Result.NEUTRAL;
                }

                @Override
                public Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, Object msg,
                                     Throwable t) {
                    process(logger);
                    return Result.NEUTRAL;
                }

                @Override
                public Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, String msg,
                                     Object... params) {
                    process(logger);
                    return Result.NEUTRAL;
                }
            });
        }
    }

    @Override
    public void setLevel(String loggerName, AdapterLevel adapterLevel) {
        final String key = Logger.ROOT_LOGGER_NAME.equals(loggerName)
                ? LogManager.ROOT_LOGGER_NAME
                : loggerName;

        org.apache.logging.log4j.core.Logger log4j2Logger = loggerContext.getLogger(key);
        org.apache.logging.log4j.Level log4j2Level = this.toLog4j2Level(adapterLevel);
        log4j2Logger.setLevel(log4j2Level);
        getLogger(loggerName);
    }

    @Override
    public Logger getLogger(String name) {
        Logger logger = loggerMap.get(name);
        if (logger != null) {
            return logger;
        }

        loggerMap.putIfAbsent(name, newLogger(name, loggerContext));
        return loggerMap.get(name);
    }

    private ConsoleAppender getOrCreateConsoleAppender(String loggerName) {
        return consoleAppendMap.computeIfAbsent(loggerName, k -> {

            String logPattern = properties.getProperty(
                    Constants.SOFA_MIDDLEWARE_LOG_CONSOLE_LOG4J2_PATTERN,
                    Constants.SOFA_MIDDLEWARE_LOG_CONSOLE_LOG4J2_PATTERN_DEFAULT);

            Level level = getConsoleLevel();
            PatternLayout patternLayout = PatternLayout.newBuilder().withPattern(logPattern).build();
            Filter filter = ThresholdFilter.createFilter(level, Filter.Result.NEUTRAL, Filter.Result.DENY);

            ConsoleAppender.Builder<?> builder = ConsoleAppender.newBuilder()
                    .setLayout(patternLayout)
                    .setName(CONSOLE)
                    .setFilter(filter);

            ConsoleAppender appender = builder.build();
            appender.start();
            return appender;
        });
    }

    private Level getConsoleLevel() {
        String defaultLevel = properties.getProperty(Constants.SOFA_MIDDLEWARE_ALL_LOG_CONSOLE_LEVEL, "INFO");
        String level = properties.getProperty(String.format(Constants.SOFA_MIDDLEWARE_SINGLE_LOG_CONSOLE_LEVEL, spaceId), defaultLevel);
        return Level.toLevel(level);
    }

    private Logger newLogger(String name, LoggerContext loggerContext) {
        final String key = Logger.ROOT_LOGGER_NAME.equals(name) ? LogManager.ROOT_LOGGER_NAME
                : name;
        return new Log4jLogger(loggerContext.getLogger(key), name);
    }

    private Level toLog4j2Level(AdapterLevel adapterLevel) {
        if (adapterLevel == null) {
            throw new IllegalStateException("AdapterLevel is NULL when adapter to log4j2.");
        }

        return switch (adapterLevel) {
            case TRACE -> Level.TRACE;
            case DEBUG -> Level.DEBUG;
            case INFO -> Level.INFO;
            case WARN -> Level.WARN;
            case ERROR -> Level.ERROR;
            default -> throw new IllegalStateException(adapterLevel
                    + " is unknown when adapter to log4j2.");
        };
    }

}

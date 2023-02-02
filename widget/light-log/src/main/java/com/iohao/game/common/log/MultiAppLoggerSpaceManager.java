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

import com.iohao.game.common.log.adapter.level.AdapterLevel;
import com.iohao.game.common.log.env.LogEnvUtils;
import com.iohao.game.common.log.factory.*;
import com.iohao.game.common.log.proxy.TemporaryLoggerFactoryPool;
import com.iohao.game.common.log.space.SpaceId;
import com.iohao.game.common.log.utils.SofaClassLoaderUtil;
import com.iohao.game.common.log.utils.SofaReportUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.iohao.game.common.log.Constants.*;

/**
 * @author 渔民小镇
 * @date 2023-01-31
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MultiAppLoggerSpaceManager {

    static final AbstractLoggerSpaceFactory NOP_LOGGER_FACTORY = new AbstractLoggerSpaceFactory("nop") {
        @Override
        public Logger getLogger(String name) {
            return Constants.DEFAULT_LOG;
        }
    };

    static final Map<SpaceId, LogSpace> LOG_FACTORY_MAP = new ConcurrentHashMap<>();

    /**
     * Invoke this method before using if some special configurations for the log space are needed.
     * This method isn't mandatory because MultiAppLoggerSpaceManager will initialize an LogSpace with empty config map
     *
     * @param spaceName space name
     * @param props     properties used to populate log context
     */
    public static void init(String spaceName, Map<String, String> props) {
        init(new SpaceId(spaceName), props, SofaClassLoaderUtil.getCallerClassLoader());
    }

    /**
     * Invoke this method before using if some special configurations for the log space are needed.
     * This method isn't mandatory because MultiAppLoggerSpaceManager will initialize an LogSpace with empty config map
     *
     * @param spaceId space identity
     * @param props   properties used to populate log context
     */
    public static void init(SpaceId spaceId, Map<String, String> props, ClassLoader spaceClassloader) {
        if (isSpaceInitialized(spaceId)) {
            SofaReportUtil.reportWarn("Logger space: \"" + spaceId.getSpaceName()
                    + "\" is already initialized!");
            return;
        }

        synchronized (spaceId) {
            if (isSpaceInitialized(spaceId)) {
                SofaReportUtil.reportWarn("Logger space: \"" + spaceId.getSpaceName()
                        + "\" is already initialized!");
                return;
            }

            doInit(spaceId, props, spaceClassloader);
        }

        SofaReportUtil.reportInfo("Logger Space: \"" + spaceId.toString() + "\" init ok.");
    }

    static void doInit(String spaceName, Map<String, String> props, ClassLoader spaceClassloader) {
        doInit(SpaceId.withSpaceName(spaceName), props, spaceClassloader);
    }

    /**
     * This method execute the actual initializing steps.
     * Before invoking this method, make sure necessary synchronization mechanism is followed.
     *
     * @param spaceId          space identification
     * @param props            properties used to populate log context
     * @param spaceClassloader the class loader used to load resources
     */
    static void doInit(SpaceId spaceId, Map<String, String> props, ClassLoader spaceClassloader) {
        LogSpace logSpace = new LogSpace(props, spaceClassloader);
        LOG_FACTORY_MAP.putIfAbsent(spaceId, logSpace);
    }

    /**
     * Get logger from specified spaceName
     * The return logger is obtained from corresponding LoggerFactory which is configured by its own log configs
     *
     * @param name      logger name
     * @param spaceName space name
     * @return logger of org.slf4j.Logger type
     */
    public static Logger getLoggerBySpace(String name, String spaceName) {
        ClassLoader callerClassLoader = SofaClassLoaderUtil.getCallerClassLoader();
        return getLoggerBySpace(name, new SpaceId(spaceName), callerClassLoader);
    }

    /**
     * Get logger from specified spaceName
     * The return logger is obtained from corresponding LoggerFactory which is configured by its own log configs
     *
     * @param name    logger name
     * @param spaceId space identification
     * @return logger of org.slf4j.Logger type
     */
    public static Logger getLoggerBySpace(String name, SpaceId spaceId) {
        ClassLoader callerClassLoader = SofaClassLoaderUtil.getCallerClassLoader();
        return getLoggerBySpace(name, spaceId, callerClassLoader);
    }

    /**
     * Get logger from specified spaceName
     * The return logger is obtained from corresponding LoggerFactory which is configured by its own log configs
     *
     * @param name             logger name
     * @param spaceName        space name
     * @param spaceClassloader the class loader used to load resources
     * @return logger of org.slf4j.Logger type
     */
    public static Logger getLoggerBySpace(String name, String spaceName, ClassLoader spaceClassloader) {
        return getLoggerBySpace(name, new SpaceId(spaceName), spaceClassloader);
    }

    /**
     * Get logger from specified spaceName
     * The return logger is obtained from corresponding LoggerFactory which is configured by its own log configs
     *
     * @param name             logger name
     * @param spaceId          space identification
     * @param spaceClassloader the class loader used to load resources
     * @return logger of org.slf4j.Logger type
     */
    public static Logger getLoggerBySpace(String name, SpaceId spaceId, ClassLoader spaceClassloader) {

        var abstractLoggerSpaceFactory = getILoggerFactoryBySpaceName(spaceId, spaceClassloader);

        return abstractLoggerSpaceFactory.getLogger(name);
    }

    private static AbstractLoggerSpaceFactory getILoggerFactoryBySpaceName(
            SpaceId spaceId,
            ClassLoader spaceClassloader) {

        if (!isSpaceInitialized(spaceId)) {
            return TemporaryLoggerFactoryPool.get(spaceId, spaceClassloader);
        }

        AbstractLoggerSpaceFactory factory = NOP_LOGGER_FACTORY;
        LogSpace space = LOG_FACTORY_MAP.get(spaceId);
        if (!isSpaceILoggerFactoryExisted(spaceId)) {
            synchronized (space) {
                if (!isSpaceILoggerFactoryExisted(spaceId)) {
                    factory = createILoggerFactory(spaceId, space, spaceClassloader);
                    space.setAbstractLoggerSpaceFactory(factory);
                }
            }
        } else {
            factory = LOG_FACTORY_MAP.get(spaceId).getAbstractLoggerSpaceFactory();
        }


        return factory;
    }

    public static Logger setLoggerLevel(String loggerName, String spaceName, AdapterLevel adapterLevel) {
        return setLoggerLevel(loggerName, new SpaceId(spaceName), adapterLevel);
    }

    public static Logger setLoggerLevel(String loggerName,
                                        SpaceId spaceId,
                                        AdapterLevel adapterLevel) {

        ClassLoader callerClassLoader = SofaClassLoaderUtil.getCallerClassLoader();
        AbstractLoggerSpaceFactory abstractLoggerSpaceFactory = getILoggerFactoryBySpaceName(
                spaceId, callerClassLoader);
        try {
            abstractLoggerSpaceFactory.setLevel(loggerName, adapterLevel);
        } catch (Exception e) {
            SofaReportUtil.reportError("SetLoggerLevel Error : ", e);
        }

        return abstractLoggerSpaceFactory.getLogger(loggerName);
    }


    public static ILoggerFactory removeILoggerFactoryBySpaceName(String spaceName) {
        return removeILoggerFactoryBySpaceId(new SpaceId(spaceName));
    }

    public static ILoggerFactory removeILoggerFactoryBySpaceId(SpaceId spaceId) {

        if (spaceId == null) {
            return null;
        }

        LogSpace logSpace = LOG_FACTORY_MAP.get(spaceId);

        if (logSpace == null) {
            return null;
        }

        AbstractLoggerSpaceFactory oldFactory = logSpace.getAbstractLoggerSpaceFactory();
        LOG_FACTORY_MAP.remove(spaceId);
        SofaReportUtil.reportWarn("Log Space Name[" + spaceId.getSpaceName()
                + "] is Removed from Current Log Space Manager!");

        return oldFactory;
    }


    public static boolean isSpaceInitialized(String spaceName) {
        return isSpaceInitialized(new SpaceId(spaceName));
    }

    public static boolean isSpaceInitialized(SpaceId spaceId) {
        return LOG_FACTORY_MAP.containsKey(spaceId);
    }


    private static boolean isSpaceILoggerFactoryExisted(SpaceId spaceId) {
        return isSpaceInitialized(spaceId)
                && LOG_FACTORY_MAP.get(spaceId).getAbstractLoggerSpaceFactory() != null;
    }

    private static AbstractLoggerSpaceFactory createILoggerFactory(SpaceId spaceId,
                                                                   LogSpace logSpace,
                                                                   ClassLoader spaceClassloader) {
        if (SOFA_MIDDLEWARE_LOG_DISABLE) {
            SofaReportUtil.reportWarn("Sofa-Middleware-Log is disabled!  -D"
                    + SOFA_MIDDLEWARE_LOG_DISABLE_PROP_KEY + "=true");
            return NOP_LOGGER_FACTORY;
        }

        // Configurations programmed manually will be overridden by following operation if keys are same.
        logSpace.putAll(LogEnvUtils.processGlobalSystemLogProperties());

        try {
            if (LOGBACK_MIDDLEWARE_LOG_DISABLE) {
                SofaReportUtil.reportWarn("Logback-Sofa-Middleware-Log is disabled! -D"
                        + LOGBACK_MIDDLEWARE_LOG_DISABLE_PROP_KEY + "=true");
            } else {
                if (LogEnvUtils.isLogbackUsable(spaceClassloader)) {
                    SofaReportUtil.reportDebug("Actual binding is of type [ " + spaceId.toString()
                            + " Logback ]");
                    LoggerSpaceFactoryBuilder loggerSpaceFactory4LogbackBuilder = new LoggerSpaceFactory4LogbackBuilder(
                            spaceId, logSpace);

                    return loggerSpaceFactory4LogbackBuilder.build(spaceId.getSpaceName(),
                            spaceClassloader);
                }
            }

            if (LOG4J_MIDDLEWARE_LOG_DISABLE) {
                SofaReportUtil.reportWarn("Log4j-Sofa-Middleware-Log is disabled!  -D"
                        + LOG4J_MIDDLEWARE_LOG_DISABLE_PROP_KEY + "=true");
            } else {
                if (LogEnvUtils.isLog4jUsable(spaceClassloader)) {
                    SofaReportUtil.reportDebug("Actual binding is of type [ " + spaceId.toString()
                            + " Log4j ]");
                    LoggerSpaceFactoryBuilder loggerSpaceFactory4Log4jBuilder = new LoggerSpaceFactory4Log4jBuilder(
                            spaceId, logSpace);

                    return loggerSpaceFactory4Log4jBuilder.build(spaceId.getSpaceName(),
                            spaceClassloader);
                }
            }

            if (LOG4J2_MIDDLEWARE_LOG_DISABLE) {
                SofaReportUtil.reportWarn("Log4j2-Sofa-Middleware-Log is disabled!  -D"
                        + LOG4J2_MIDDLEWARE_LOG_DISABLE_PROP_KEY + "=true");
            } else {
                if (LogEnvUtils.isLog4j2Usable(spaceClassloader)) {
                    SofaReportUtil.reportDebug("Actual binding is of type [ " + spaceId.toString()
                            + " Log4j2 ]");
                    LoggerSpaceFactoryBuilder loggerSpaceFactory4Log4j2Builder = new LoggerSpaceFactory4Log4j2Builder(
                            spaceId, logSpace);

                    return loggerSpaceFactory4Log4j2Builder.build(spaceId.getSpaceName(),
                            spaceClassloader);
                }
            }

            // DO not delete this, this is used in multi-classloader scenario for compatibility of commons-logging
            if (LOG4J_COMMONS_LOGGING_MIDDLEWARE_LOG_DISABLE) {
                SofaReportUtil.reportWarn("Log4j-Sofa-Middleware-Log(commons-logging) is disabled!  -D"
                        + LOG4J_COMMONS_LOGGING_MIDDLEWARE_LOG_DISABLE_PROP_KEY + "=true");
            } else {
                if (LogEnvUtils.isCommonsLoggingUsable(spaceClassloader)) {
                    SofaReportUtil.reportDebug("Actual binding is of type [ " + spaceId.toString()
                            + " Log4j (Adapter commons-logging to slf4j)]");

                    LoggerSpaceFactoryBuilder loggerSpaceFactory4Log4jBuilder = new LoggerSpaceFactory4CommonsLoggingBuilder(
                            spaceId, logSpace);
                    return loggerSpaceFactory4Log4jBuilder.build(spaceId.getSpaceName(),
                            spaceClassloader);
                }
            }

            SofaReportUtil.reportWarn("[" + spaceId.toString() + "] No log util is usable, Default app logger will be used.");
        } catch (Throwable e) {
            SofaReportUtil.reportError("[" + spaceId.toString() + "] Build ILoggerFactory error! Default app logger will be used.",
                    e);
        }

        return NOP_LOGGER_FACTORY;
    }
}

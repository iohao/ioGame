/*
 * ioGame
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
package com.alipay.sofa.common.log.factory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.classic.util.LogbackMDCAdapter;
import ch.qos.logback.core.joran.spi.JoranException;
import com.alipay.sofa.common.log.SpaceInfo;
import com.alipay.sofa.common.log.adapter.level.AdapterLevel;
import com.iohao.game.common.kit.exception.CommonIllegalArgumentException;
import com.iohao.game.common.kit.exception.ThrowKit;
import org.slf4j.Logger;

import java.net.URL;

/**
 * 兼容 slf4j 2.0.x、logback 1.4.x ...等系列。
 * <p>
 * 原因：LoggerSpaceManager 、MultiAppLoggerSpaceManager 没有提供扩展点。
 * <p>
 * 这里使用 class 覆盖的方式做兼容支持。
 *
 * @author 渔民小镇
 * @date 2024-01-07
 */
public class LoggerSpaceFactory4LogbackBuilder extends AbstractLoggerSpaceFactoryBuilder {
    public LoggerSpaceFactory4LogbackBuilder(SpaceInfo spaceInfo) {
        super(spaceInfo);
    }

    @Override
    public AbstractLoggerSpaceFactory doBuild(String spaceName, ClassLoader spaceClassloader, URL url) {

        final LoggerContext loggerContext = new LoggerContext();

        for (var entry : getProperties().entrySet()) {
            loggerContext.putProperty((String) entry.getKey(), (String) entry.getValue());
        }

        if (url != null) {
            try {
                loggerContext.setMDCAdapter(new LogbackMDCAdapter());
                new ContextInitializer(loggerContext);

                var configurator = new JoranConfigurator();
                configurator.setContext(loggerContext);
                configurator.doConfigure(url);
            } catch (JoranException e) {
                ThrowKit.ofIllegalArgumentException("Logback loggerSpaceFactory build error", e);
            }
        }

        return new AbstractLoggerSpaceFactory(getLoggingToolName()) {
            @Override
            public Logger setLevel(String loggerName, AdapterLevel adapterLevel) {
                var logbackLogger = (ch.qos.logback.classic.Logger) this.getLogger(loggerName);

                ch.qos.logback.classic.Level logbackLevel = this.toLogbackLevel(adapterLevel);
                logbackLogger.setLevel(logbackLevel);

                return logbackLogger;
            }

            @Override
            public Logger getLogger(String name) {
                return loggerContext.getLogger(name);
            }

            private ch.qos.logback.classic.Level toLogbackLevel(AdapterLevel adapterLevel) {
                if (adapterLevel == null) {
                    throw new CommonIllegalArgumentException("AdapterLevel is NULL when adapter to logback.");
                }

                return switch (adapterLevel) {
                    case TRACE -> ch.qos.logback.classic.Level.TRACE;
                    case DEBUG -> ch.qos.logback.classic.Level.DEBUG;
                    case INFO -> ch.qos.logback.classic.Level.INFO;
                    case WARN -> ch.qos.logback.classic.Level.WARN;
                    case ERROR -> ch.qos.logback.classic.Level.ERROR;
                };
            }
        };
    }

    @Override
    protected String getLoggingToolName() {
        return "logback";
    }
}

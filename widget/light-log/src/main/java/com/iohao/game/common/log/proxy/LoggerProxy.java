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
package com.iohao.game.common.log.proxy;

import com.iohao.game.common.log.utils.SofaAssertUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * @author 渔民小镇
 * @date 2023-01-31
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoggerProxy implements Logger {

    final TemporaryLoggerFactory.LoggerSelector loggerSelector;
    final String name;

    public LoggerProxy(TemporaryLoggerFactory.LoggerSelector loggerSelector, String name) {
        SofaAssertUtil.notNull(loggerSelector);
        this.loggerSelector = loggerSelector;
        this.name = name;
    }

    private Logger getLoggerDelegator() {
        return loggerSelector.select(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isTraceEnabled() {
        return getLoggerDelegator().isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        getLoggerDelegator().trace(msg);
    }

    @Override
    public void trace(String format, Object arg) {
        getLoggerDelegator().trace(format, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        getLoggerDelegator().trace(format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        getLoggerDelegator().trace(format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        getLoggerDelegator().trace(msg, t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return getLoggerDelegator().isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        getLoggerDelegator().trace(marker, msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        getLoggerDelegator().trace(marker, format, arg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        getLoggerDelegator().trace(marker, format, arg1, arg2);
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        getLoggerDelegator().trace(marker, format, argArray);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        getLoggerDelegator().trace(marker, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return getLoggerDelegator().isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        getLoggerDelegator().debug(msg);

    }

    @Override
    public void debug(String format, Object arg) {
        getLoggerDelegator().debug(format, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        getLoggerDelegator().debug(format, arg1, arg2);

    }

    @Override
    public void debug(String format, Object... arguments) {
        getLoggerDelegator().debug(format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        getLoggerDelegator().debug(msg, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return getLoggerDelegator().isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        getLoggerDelegator().debug(marker, msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        getLoggerDelegator().debug(marker, format, arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        getLoggerDelegator().debug(marker, format, arg1, arg2);
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        getLoggerDelegator().debug(marker, format, arguments);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        getLoggerDelegator().debug(marker, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return getLoggerDelegator().isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        getLoggerDelegator().info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        getLoggerDelegator().info(format, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        getLoggerDelegator().info(format, arg1, arg2);
    }

    @Override
    public void info(String format, Object... arguments) {
        getLoggerDelegator().info(format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        getLoggerDelegator().info(msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return getLoggerDelegator().isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        getLoggerDelegator().info(marker, msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        getLoggerDelegator().info(marker, format, arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        getLoggerDelegator().info(marker, format, arg1, arg2);
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        getLoggerDelegator().info(marker, format, arguments);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        getLoggerDelegator().info(marker, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return getLoggerDelegator().isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        getLoggerDelegator().warn(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        getLoggerDelegator().warn(format, arg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        getLoggerDelegator().warn(format, arguments);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        getLoggerDelegator().warn(format, arg1, arg2);
    }

    @Override
    public void warn(String msg, Throwable t) {
        getLoggerDelegator().warn(msg, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return getLoggerDelegator().isWarnEnabled();
    }

    @Override
    public void warn(Marker marker, String msg) {
        getLoggerDelegator().warn(marker, msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        getLoggerDelegator().warn(marker, format, arg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        getLoggerDelegator().warn(marker, format, arg1, arg2);
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        getLoggerDelegator().warn(marker, format, arguments);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        getLoggerDelegator().warn(marker, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return getLoggerDelegator().isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        getLoggerDelegator().error(msg);
    }

    @Override
    public void error(String format, Object arg) {
        getLoggerDelegator().error(format, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        getLoggerDelegator().error(format, arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        getLoggerDelegator().error(format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        getLoggerDelegator().error(msg, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return getLoggerDelegator().isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        getLoggerDelegator().error(marker, msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        getLoggerDelegator().error(marker, format, arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        getLoggerDelegator().error(marker, format, arg1, arg2);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        getLoggerDelegator().error(marker, format, arguments);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        getLoggerDelegator().error(marker, msg, t);
    }
}

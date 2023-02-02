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
package com.iohao.game.common.log.utils;

import com.iohao.game.common.log.LogLog;
import lombok.experimental.UtilityClass;

/**
 * @author 渔民小镇
 * @date 2023-01-31
 */
@UtilityClass
public class SofaReportUtil {
    /**
     * print Debug message
     *
     * @param msg message
     */
    public void reportDebug(String msg) {
        LogLog.debug(msg);
    }

    /**
     * print Info message
     *
     * @param msg message
     */
    public void reportInfo(String msg) {
        LogLog.info(msg);
    }

    /**
     * print Warn message
     *
     * @param msg message
     */
    public void reportWarn(String msg) {
        LogLog.warn(msg);
    }

    public void reportWarn(String msg, Throwable e) {
        LogLog.warn(msg, e);
    }

    public void reportError(String msg, Throwable throwable) {
        LogLog.error(msg, throwable);
    }
}

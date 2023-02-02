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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author 渔民小镇
 * @date 2023-01-31
 */
public class SofaProcessIdUtil {
    private static volatile String processId;

    public static String getProcessId() {
        try {
            if (!SofaStringUtil.isBlank(processId)) {
                return processId;
            }
            // LOG4J2-2126 use reflection to improve compatibility with Android Platform which does not support JMX extensions
            Class<?> managementFactoryClass = Class
                    .forName("java.lang.management.ManagementFactory");
            Method getRuntimeMXBean = managementFactoryClass.getDeclaredMethod("getRuntimeMXBean");
            Class<?> runtimeMXBeanClass = Class.forName("java.lang.management.RuntimeMXBean");
            Method getName = runtimeMXBeanClass.getDeclaredMethod("getName");

            Object runtimeMXBean = getRuntimeMXBean.invoke(null);
            String name = (String) getName.invoke(runtimeMXBean);
            // likely works on most platforms
            processId = name.split("@")[0];
            return processId;
        } catch (final Exception ex) {
            try {
                // try a Linux-specific way
                processId = new File("/proc/self").getCanonicalFile().getName();
                return processId;
            } catch (final IOException ignoredUseDefault) {
                // Ignore exception.
            }
        }
        return "-";
    }
}

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

import lombok.experimental.UtilityClass;

/**
 * @author 渔民小镇
 * @date 2023-01-31
 */
@UtilityClass
public class SofaClassLoaderUtil {
    public ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public ClassLoader getCallerClassLoader() {
        try {
            String callerClassName = Thread.currentThread().getStackTrace()[3].getClassName();

            try {
                return Class.forName(callerClassName).getClassLoader();
            } catch (ClassNotFoundException e) {
                return Class.forName(callerClassName, true, getContextClassLoader())
                        .getClassLoader();
            }
        } catch (Throwable t) {
            throw new RuntimeException("Failed to get caller classloader ", t);
        }
    }

    public boolean isPresent(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (Throwable throwable) {
            return false;
        }
    }
}

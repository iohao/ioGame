/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.common.validation;

import com.iohao.game.common.kit.ClassScanner;
import com.iohao.game.common.kit.io.ResourceKit;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 数据校验器管理
 *
 * @author shenjk
 * @date 2022-09-26
 */
@Slf4j
@UtilityClass
public class Validation {
    final String fileName = "META-INF/ioGame/com.iohao.game.common.validation.Validator";
    final String defaultValidator = "com.iohao.game.common.validation.support.JakartaValidator";

    private volatile Validator validator;

    Lock lock = new ReentrantLock();

    /**
     * 获取当前配置的数据校验器
     *
     * @return 数据校验器
     **/
    public Validator getValidator() throws Exception {
        if (validator != null) {
            return validator;
        }

        /*
         * 实际上不加锁也不影响，因为业务框架有个初始化的过程，
         * 在业务框架初始化时，会用到 validator 变量，
         * 也就是说在项目启动的过程中，validator 的初始化已经做好了；
         *
         * 这里加锁也是为了让验证模块，在其他系统中可以单独的使用，即不依赖 ioGame 也可以安全的使用；
         */

        lock.lock();

        try {

            if (validator != null) {
                return validator;
            }

            final String className = getValidatorClassName();
            final String packageName = getValidatorPackage(className);

            ClassScanner classScanner = new ClassScanner(packageName, clazz -> clazz.getName().equals(className));
            List<Class<?>> classList = classScanner.listScan();
            if (classList.isEmpty()) {
                throw new Exception("缺少类: " + className);
            }

            Class<?> clazz = classList.get(0);
            validator = (Validator) clazz.getConstructor().newInstance();

        } finally {
            lock.unlock();
        }

        return validator;
    }

    /**
     * 获取数据校验器的类名(fullName)
     *
     * @return 校验器的类名
     */
    private String getValidatorClassName() {
        String className = null;

        try {
            className = ResourceKit.readStr(fileName, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.info("读取 {} 失败，将使用默认类 {} 来处理", fileName, defaultValidator);
        }

        if (StringUtils.isBlank(className)) {
            className = defaultValidator;
        }

        return StringUtils.trim(className);
    }

    /**
     * 获取数据校验器的package
     *
     * @return packageName;
     */
    private String getValidatorPackage(String className) {
        List<String> segments = Arrays.stream(className.split("\\.")).toList();

        return segments.stream()
                .limit(segments.size() - 1)
                .collect(Collectors.joining("/"));
    }
}
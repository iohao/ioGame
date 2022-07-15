/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.common.kit;

import com.iohao.game.action.skeleton.annotation.ActionController;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Predicate;

@Slf4j
public class ClassScannerTest {

//    @Test
    public void scan() {
        Predicate<Class<?>> predicateFilter = (clazz) -> {
            ActionController annotation = clazz.getAnnotation(ActionController.class);
            return annotation != null;
        };

        String packagePath = "com.iohao.game";
        packagePath = "com.iohao.game.action.skeleton.core.action";
        ClassScanner scanner = new ClassScanner(packagePath, predicateFilter);

        List<Class<?>> classList = scanner.listScan();

        for (Class<?> clazz : classList) {
            log.info("clazz: {}", clazz);
        }


    }
}
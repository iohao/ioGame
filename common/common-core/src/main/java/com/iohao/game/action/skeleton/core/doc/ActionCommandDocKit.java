/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General  License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General  License for more details.
 *
 * You should have received a copy of the GNU General  License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.action.skeleton.core.doc;

import com.iohao.game.common.kit.ClassScanner;
import com.iohao.game.common.kit.io.FileKit;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 渔民小镇
 * @date 2022-01-28
 */
@UtilityClass
public class ActionCommandDocKit {
    static final Logger log = IoGameLoggerFactory.getLoggerCommonStdout();

    /**
     * java class doc map
     * <pre>
     *     key : java class name (YourJavaFile.class)
     *     value : {@link JavaClassDocInfo}
     * </pre>
     *
     * @param controllerList classList
     * @return map
     */
    public Map<String, JavaClassDocInfo> getJavaClassDocInfoMap(List<Class<?>> controllerList) {
        JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
        final Map<String, JavaClassDocInfo> javaClassDocInfoMap = new HashMap<>(controllerList.size());

        for (Class<?> actionClazz : controllerList) {

            try {
                String packagePath = actionClazz.getPackageName();
                ClassScanner classScanner = new ClassScanner(packagePath, null);
                List<URL> resources = classScanner.listResource();

                for (URL resource : resources) {

                    String path = resource.getPath();
                    String srcPath = path.replace("target/classes", "src/main/java");

                    File file = new File(srcPath);
                    if (!FileKit.exist(file)) {
                        continue;
                    }

                    javaProjectBuilder.addSourceTree(file);
                }

                Collection<JavaClass> classes = javaProjectBuilder.getClasses();

                for (JavaClass javaClass : classes) {

                    JavaClassDocInfo javaClassDocInfo = new JavaClassDocInfo(javaClass);

                    javaClassDocInfoMap.put(javaClass.toString(), javaClassDocInfo);
                }

            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }

        }

        return javaClassDocInfoMap;

    }

}

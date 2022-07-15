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
package com.iohao.game.widget.light.profile;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 渔民小镇
 * @date 2022-01-02
 */
@Slf4j
class ResourcePatternResolverProfile {
    /** 默认主目录 */
    @Setter
    private static String rootDir = "conf";
    /** 默认加载主目录-子目录 */
    @Setter
    private static String defaultDir = "common";
    /** 默认加载指定后缀文件 */
    @Setter
    private static String suffix = ".props";

    /** 需要加载的目录列表 */
    private LinkedList<String> dirNameList = new LinkedList<>();

    /**
     * 添加需要加载的目录
     *
     * @param dir 目录路径
     */
    public void addDir(String dir) {
        dirNameList.add(dir);
    }

    /**
     * 资源url
     *
     * @return 一定不为null
     */
    public List<URL> toUrls() {

        // 优先加载 common 目录
        dirNameList.addFirst(defaultDir);

        List<URL> files = new LinkedList<>();

        String locationPatternTemplate = "classpath*:%s/%s/*%s";
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            for (String dir : dirNameList) {
                String locationPattern = String.format(locationPatternTemplate, rootDir, dir, suffix);

                Resource[] resources = resolver.getResources(locationPattern);
                log.debug("locationPattern: {}", locationPattern);
                for (Resource resource : resources) {
                    URL url = resource.getURL();
                    files.add(url);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }


        return files;
    }

}

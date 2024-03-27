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

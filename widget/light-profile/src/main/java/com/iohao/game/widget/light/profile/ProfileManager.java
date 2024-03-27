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

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * profile 配置与构建 <BR>
 *
 * @author 渔民小镇
 * @date 2022-01-02
 */
@Slf4j
@UtilityClass
public final class ProfileManager {
    /** 主配置key */
    final String MAIN_CONFIG = "main_config";
    /**
     * <pre>
     *     key : profileName
     *     value : profile
     * </pre>
     */
    private final Map<String, Profile> profileMap = new ConcurrentHashMap<>();


    public Profile profile() {
        return profile(MAIN_CONFIG);
    }

    public Profile profile(final String key) {
        Profile profile = profileMap.get(key);

        // 无锁化
        if (Objects.isNull(profile)) {
            profile = new Profile();
            profile.key = key;
            profile = profileMap.putIfAbsent(key, profile);

            if (Objects.isNull(profile)) {
                profile = profileMap.get(key);
            }
        }

        return profile;
    }

    /**
     * 加载配置文件, /resources/conf下面的目录
     * <pre>
     * 数据格式以,分割.  例如: blocks,local (假设参数传入是这个字符串)
     * 优先加载 blocks 目录下的配置文件.
     * 然后加载 local 目录下的配置文件.
     * 如果两个目录中有相同的配置项, 那么后面的会覆盖前面的配置项
     *
     * </pre>
     *
     * @param profileConfigName 数据格式以,分割. 例如: blocks,local
     */
    public void loadMainProfile(String profileConfigName) {
        Optional<String> name = Optional.ofNullable(profileConfigName);

        final String separator = ",";
        List<String> configNameList = Arrays.stream(name.orElse("")
                        .split(separator))
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        log.debug("加载的目录列表 - size {} - {}", configNameList.size(), configNameList);

        // 加载配置文件
        ResourcePatternResolverProfile resolverConfig = new ResourcePatternResolverProfile();
        configNameList.forEach(resolverConfig::addDir);
        List<URL> urlList = resolverConfig.toUrls();

        // 将配置文件内容加载的 Profile 中
        Profile profile = profile();
        profile.load(urlList);

        log.debug("配置内容 - size:{} - {}", ProfileManager.profile().map.size(), ProfileManager.profile());

    }


}

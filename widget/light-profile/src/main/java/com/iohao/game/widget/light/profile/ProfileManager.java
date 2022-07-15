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
                .filter(s -> s.length() > 0)
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

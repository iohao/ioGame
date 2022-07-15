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

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author 渔民小镇
 * @date 2022-01-02
 */
@Slf4j
public class ProfileManagerTest {

    @Test
    public void profile() {
        // 加载环境配置
        String profileConfigName = "local";
        ProfileManager.loadMainProfile(profileConfigName);

        Profile profile = ProfileManager.profile();
        // 调用配置文件中的配置
        String jdbcDriver = profile.get("jdbcDriver");
        boolean devMode = profile.getBool("devMode");
        int maxVip = profile.getInt("maxVip");

        log.info("profile::: {} {} {}", jdbcDriver, devMode, maxVip);
    }
}
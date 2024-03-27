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

        int externalPort = profile.getInt("external.port");

        System.out.println();

        log.info("jdbcDriver : {}", jdbcDriver);
        log.info("devMode : {}", devMode);
        log.info("maxVip : {}", maxVip);
        log.info("externalPort : {}", externalPort);
    }
}
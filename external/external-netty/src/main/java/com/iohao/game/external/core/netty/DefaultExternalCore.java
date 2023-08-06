/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
package com.iohao.game.external.core.netty;

import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.common.consts.IoGameLogName;
import com.iohao.game.common.kit.PresentKit;
import com.iohao.game.external.core.ExternalCore;
import com.iohao.game.external.core.config.ExternalJoinEnum;
import com.iohao.game.external.core.hook.UserHook;
import com.iohao.game.external.core.hook.internal.DefaultUserHook;
import com.iohao.game.external.core.micro.MicroBootstrap;
import com.iohao.game.external.core.micro.join.ExternalJoinSelector;
import com.iohao.game.external.core.micro.join.ExternalJoinSelectors;
import com.iohao.game.external.core.session.UserSessionOption;
import com.iohao.game.external.core.session.UserSessions;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;

/**
 * netty ExternalCore
 *
 * @author 渔民小镇
 * @date 2023-02-19
 */
@Slf4j(topic = IoGameLogName.CommonStdout)
public final class DefaultExternalCore implements ExternalCore {
    final DefaultExternalCoreSetting setting;

    DefaultExternalCore(DefaultExternalCoreSetting setting) {
        this.setting = setting;
    }

    @Override
    public MicroBootstrap createMicroBootstrap() {
        check();

        defaultSetting();

        final int externalCorePort = this.setting.getExternalCorePort();

        if (IoGameGlobalConfig.openLog) {
            log.info("启动游戏对外服 port: [{}] 连接方式: [{}] "
                    , externalCorePort
                    , setting.getExternalJoinEnum().getName());
        }

        aware();

        // 此服务器是和真实用户连接的
        MicroBootstrap microBootstrap = this.setting.getMicroBootstrap();
        microBootstrap.setExternalCoreSetting(this.setting);

        return microBootstrap;
    }

    private void check() {

        int externalCorePort = setting.getExternalCorePort();
        if (externalCorePort <= 0) {
            throw new IllegalArgumentException("游戏对外服端口必须 >0 " + externalCorePort);
        }

        Objects.requireNonNull(setting.getExternalJoinEnum()
                , "需要设置一种连接方式:" + Arrays.toString(ExternalJoinEnum.values()));
    }

    /**
     * 一些默认值设置，由于连接类型的代码并不多，就在这里硬编码了。
     */
    private void defaultSetting() {
        // 根据当前的连接方式得到 ExternalJoinSelector
        ExternalJoinEnum joinEnum = this.setting.getExternalJoinEnum();
        ExternalJoinSelector externalJoinSelector = ExternalJoinSelectors.getExternalJoinSelector(joinEnum);
        // 初始化一些数据
        externalJoinSelector.defaultSetting(this.setting);

        // ================== 以下是在各连接方式下都通用的设置 ==================

        // UserHook 钩子接口；如果开发者没有手动赋值，则给一个默认的
        PresentKit.ifNull(this.setting.getUserHook(), () -> this.setting.setUserHook(new DefaultUserHook()));
        UserSessions<?, ?> userSessions = this.setting.getUserSessions();
        userSessions.setUserHook(this.setting.getUserHook());

        // 当前游戏对外服所使用的连接方式
        userSessions.option(UserSessionOption.externalJoin, joinEnum);
    }

    private void aware() {
        // 玩家上线、下线钩子接口
        UserHook userHook = setting.getUserHook();
        var userSessions = setting.getUserSessions();
        userSessions.setUserHook(userHook);
    }
}

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
package com.iohao.game.core.common.client;

import com.iohao.game.action.skeleton.core.IoGameCommonCoreConfig;

/**
 * <pre>
 *     开发者扩展时，用正数的业务码
 *     框架会从负数开始使用
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-12-11
 */
public interface ExternalBizCodeCont {
    /** 用户（玩家）是否在线，ExistUserExternalBizRegion */
    int existUser = -1;
    /** 强制用户（玩家）下线，ForcedOfflineExternalBizRegion */
    int forcedOffline = -2;

    /** 用户（玩家）的元信息同步，AttachmentExternalBizRegion */
    int attachment = IoGameCommonCoreConfig.ExternalBizCode.attachment;
    /** 用户（玩家）在游戏对外服的 HeadMetadata 信息 */
    int userHeadMetadata = -4;
}

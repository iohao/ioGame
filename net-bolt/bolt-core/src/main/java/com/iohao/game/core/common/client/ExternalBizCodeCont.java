/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.core.common.client;

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
    int attachment = -3;
}

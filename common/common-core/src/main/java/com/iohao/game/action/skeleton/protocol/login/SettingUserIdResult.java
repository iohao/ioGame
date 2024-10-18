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
package com.iohao.game.action.skeleton.protocol.login;

import com.iohao.game.common.kit.exception.CommonRuntimeException;

/**
 * SettingUserIdResult
 *
 * @param success   true: login successful
 * @param exception If success is true, the exception is null.
 * @author 渔民小镇
 * @date 2024-10-18
 * @since 21.19
 */
public record SettingUserIdResult(boolean success, Exception exception) {

    public static final SettingUserIdResult SUCCESS = new SettingUserIdResult(true, null);

    public static SettingUserIdResult ofError(Exception exception) {
        return new SettingUserIdResult(false, exception);
    }

    public static SettingUserIdResult ofError(String message) {
        return ofError(new CommonRuntimeException(message));
    }
}

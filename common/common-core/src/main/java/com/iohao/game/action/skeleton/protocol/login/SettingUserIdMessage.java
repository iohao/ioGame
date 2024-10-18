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

import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

/**
 * Set the userId to the gameExternalServer
 *
 * @author 渔民小镇
 * @date 2022-01-18
 */
@Getter
@FieldDefaults(makeFinal = true)
public final class SettingUserIdMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = -7385687951893601229L;
    /** userId */
    long userId;

    HeadMetadata headMetadata;

    private SettingUserIdMessage(long userId, HeadMetadata headMetadata) {
        this.userId = userId;
        this.headMetadata = headMetadata;
    }

    public static SettingUserIdMessage of(long userId, HeadMetadata headMetadata) {
        return new SettingUserIdMessage(userId, headMetadata);
    }
}

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
package com.iohao.game.common.kit.system;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OsInfo {
    final String osName = InternalSystemPropsKit.get("os.name", false);
    final boolean linux = getOsMatches("Linux") || getOsMatches("LINUX");
    final boolean mac = getOsMatches("Mac") || getOsMatches("Mac OS X");


    private boolean getOsMatches(String osNamePrefix) {
        if (osName == null) {
            return false;
        }

        return osName.startsWith(osNamePrefix);
    }


    private OsInfo() {

    }

    public static OsInfo me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final OsInfo ME = new OsInfo();
    }
}

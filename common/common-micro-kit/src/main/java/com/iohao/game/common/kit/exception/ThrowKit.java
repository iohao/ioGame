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
package com.iohao.game.common.kit.exception;

import lombok.experimental.UtilityClass;

/**
 * 实验性工具，仅限内部使用
 *
 * @author 渔民小镇
 * @date 2024-08-01
 * @since 21.14
 */
@UtilityClass
public class ThrowKit {
    public void ofIllegalArgumentException(String msg) throws CommonIllegalArgumentException {
        throw new CommonIllegalArgumentException(msg);
    }

    public void ofIllegalArgumentException(String msg, Exception e) throws CommonIllegalArgumentException {
        throw new CommonIllegalArgumentException(msg, e);
    }

    public void ofRuntimeException(String msg) throws CommonRuntimeException {
        throw new CommonRuntimeException(msg);
    }

    public void ofRuntimeException(Throwable e) throws CommonRuntimeException {
        throw new CommonRuntimeException(e.getMessage(), e);
    }

    public void ofNullPointerException(String msg) throws NullPointerException {
        throw new NullPointerException(msg);
    }
}

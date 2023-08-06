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
package com.iohao.game.action.skeleton.core.flow.interal;

import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.core.exception.MsgException;
import com.iohao.game.action.skeleton.core.flow.ActionMethodExceptionProcess;
import lombok.extern.slf4j.Slf4j;

/**
 * default 异常处理
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
@Slf4j
public final class DefaultActionMethodExceptionProcess implements ActionMethodExceptionProcess {
    @Override
    public MsgException processException(final Throwable e) {

        if (e instanceof MsgException msgException) {
            return msgException;
        }

        // 到这里，一般不是用户自定义的错误，很可能是开发者引入的第三方包或自身未捕获的错误等情况
        log.error(e.getMessage(), e);

        return new MsgException(ActionErrorEnum.systemOtherErrCode);
    }
}

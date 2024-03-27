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
package com.iohao.game.widget.light.domain.event.user;

import com.iohao.game.widget.light.domain.event.message.DomainEventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户登录就发送 email
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
@Slf4j
public class UserLoginEmailEventHandler implements DomainEventHandler<UserLogin> {
    @Override
    public void onEvent(UserLogin userLogin, boolean endOfBatch) {
        log.info("给登录用户发送 email ! {}", userLogin);
    }
}

/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
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

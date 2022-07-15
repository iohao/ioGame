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
package com.iohao.game.widget.light.domain.event.student;

import lombok.extern.slf4j.Slf4j;
import com.iohao.game.widget.light.domain.event.annotation.DomainEvent;
import com.iohao.game.widget.light.domain.event.message.DomainEventHandler;

/**
 * 学生睡觉
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
@Slf4j
@DomainEvent
public final class StudentSleepEventHandler3 implements DomainEventHandler<StudentEo> {
    @Override
    public void onEvent(StudentEo studentEo, boolean endOfBatch) {
        log.debug("学生睡觉: {}", studentEo);
    }
}

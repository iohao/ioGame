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
package com.iohao.game.widget.light.timer.task.example;

import com.iohao.game.widget.light.timer.task.AbstractTimerTask;
import com.iohao.game.widget.light.timer.task.TimerTaskRegion;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务示例 业务类
 *
 * @author 渔民小镇
 * @date 2021-12-25
 */
@Slf4j
@Accessors(chain = true)
public class HelloTask extends AbstractTimerTask {

    /** 说话内容 */
    @Setter
    private String sayContent;

    @Override
    public void execute() {
        log.info("对着河道说: {}", sayContent);
    }


    @Override
    protected TimerTaskRegion getTimerTaskRegion() {
        return TimerTaskEnum.HELLO;
    }
}

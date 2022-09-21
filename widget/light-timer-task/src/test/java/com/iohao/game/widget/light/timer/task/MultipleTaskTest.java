/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.widget.light.timer.task;

import com.iohao.game.widget.light.timer.task.example.StudentTask;
import com.iohao.game.widget.light.timer.task.example.TimerTaskEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ConcurrentMap;

/**
 * @author 渔民小镇
 * @date 2022-09-21
 */
@Slf4j
public class MultipleTaskTest {
    @Test
    public void moreTask() throws InterruptedException {

        int loop = 20_000;

        for (int i = 0; i < loop; i++) {
            new StudentTask()
                    .setDelayExecutionTime(1000)
                    .task();
        }

        log.info("----start");
        print();
        Thread.sleep(13000);

        log.info("StudentTask.longAdder : {}", StudentTask.longAdder);
    }

    void print() {
        new Thread(() -> {
            while (true) {
                ConcurrentMap<String, TimerTask> stringTimerTaskConcurrentMap = TimerTaskEnum.STUDENT.getCache().asMap();

                try {
                    log.info("Thread -- StudentTask.longAdder : {}", StudentTask.longAdder);
                    log.info("Thread -- 剩余任务数量 : {}", stringTimerTaskConcurrentMap.size());

                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

        }).start();
    }
}

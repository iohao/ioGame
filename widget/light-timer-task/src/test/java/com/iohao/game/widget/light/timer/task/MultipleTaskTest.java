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
package com.iohao.game.widget.light.timer.task;

import com.iohao.game.widget.light.timer.task.example.StudentTask;
import com.iohao.game.widget.light.timer.task.example.TimerTaskEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author 渔民小镇
 * @date 2022-09-21
 */
@Slf4j
public class MultipleTaskTest {
    @Test
    public void moreTask() throws InterruptedException {
        /*
         * 默认情况下，延时器的执行上限是 10_000 ， 如果你的业务中有更大的需求量，
         * 可以通过 Cache2Kit.createCacheBuilder 构建器来调整。
         * 如果有少许误差，可以研究 cache2k 的内核，或对 cache2k 进行升级。
         */
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

                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }

            }

        }).start();
    }
}

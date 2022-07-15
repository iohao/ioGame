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
package com.iohao.game.widget.light.timer.task;

import com.iohao.game.widget.light.timer.task.example.HelloTask;
import com.iohao.game.widget.light.timer.task.example.TimerTaskEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author 渔民小镇
 * @date 2021-12-25
 */
@Slf4j
public class TimerTaskTest {

    @Test
    public void demoTimerTask() throws InterruptedException {
        // 设置缓存key。
        String cacheKey = "1";

        new HelloTask()
                // 设置业务内容
                .setSayContent("卡莉斯塔去哪儿了!!")
                // 1秒后触发
                .setDelayExecutionTime(1000)
                // 如果不指定key, 会默认给个 uuid
                .setCacheKey(cacheKey)
                //启动定时器任务
                .task();

        log.info("----start");
        Thread.sleep(3000);
    }

    @Test
    public void pause() throws InterruptedException {
        log.info("----暂停任务");
        TimerTask task = new HelloTask()
                // 设置业务内容
                .setSayContent("塔姆哪儿了!")
                // 1秒后触发
                .setDelayExecutionTime(1000)
                //启动定时器任务
                .task();

        log.info("----start");
        // 暂停任务 1 秒
        task.pause(1000);
        log.info("----end");

        Thread.sleep(3000);
    }


    @Test
    public void task() throws InterruptedException {
        log.info("两秒后执行 定时任务");

        new HelloTask()
                .setSayContent("卡莉斯塔去哪儿了!")
                .setDelayExecutionTime(2000)
                .task();

        for (int i = 0; i < 10; i++) {
            HelloTask task = new HelloTask()
                    .setSayContent("卡莉斯塔去哪儿了!" + i)
                    .setDelayExecutionTime(2000)
                    .task();
        }

        Thread.sleep(3000);
        System.out.println("------------");
    }

    @Test
    public void coverTask() throws InterruptedException {
        log.info("覆盖定时器任务");

        // key
        String cacheKey = "1";

        new HelloTask()
                .setSayContent("卡莉斯塔去哪儿了!")
                .setDelayExecutionTime(2000)
                .setCacheKey(cacheKey)
                .task();

        Thread.sleep(1000);

        // 该定时器会覆盖上一个同值 key 的定时器
        new HelloTask()
                .setSayContent("卡莉斯塔去哪儿了! ~~~~~ ")
                .setDelayExecutionTime(2000)
                .setCacheKey(cacheKey)
                .task();

        Thread.sleep(3000);
    }

    @Test
    public void removeTask() throws InterruptedException {
        log.info("这个示例是取消 定时任务");
        String cacheKey = "abc";
        new HelloTask()
                .setSayContent("卡莉斯塔去哪儿了!")
                .setDelayExecutionTime(2000)
                .setCacheKey(cacheKey)
                .task();

        Thread.sleep(1000);
        log.info("睡眠一秒后, 满足某个业务条件, 我不想执行定时任务了");
        TimerTaskEnum.HELLO.removeTimerTask(cacheKey);
        Thread.sleep(3000);
    }

}
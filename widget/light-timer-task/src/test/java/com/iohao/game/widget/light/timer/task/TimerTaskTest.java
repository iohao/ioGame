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

        new HelloTask()
                // 设置业务内容
                .setSayContent("卡莉斯塔去哪儿了!!")
                // 1秒后触发
                .setDelayExecutionTime(1000)
                //启动任务延时器
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
                //启动任务延时器
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
            new HelloTask()
                    .setSayContent("卡莉斯塔去哪儿了!" + i)
                    .setDelayExecutionTime(2000)
                    .task();
        }

        Thread.sleep(3000);
        System.out.println("------------");
    }

    @Test
    public void coverTask() throws InterruptedException {
        log.info("覆盖任务延时器");

        // 设置缓存key。
        String cacheKey = "1";

        new HelloTask()
                .setSayContent("卡莉斯塔去哪儿了!")
                .setDelayExecutionTime(2000)
                // 如果不指定key, 会默认给个 uuid
                .setCacheKey(cacheKey)
                .task();

        Thread.sleep(1000);

        // 该任务延时器会覆盖上一个同值 key 的任务延时器
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
        // 根据 key 移除任务延时器
        TimerTaskEnum.HELLO.removeTimerTask(cacheKey);
        Thread.sleep(3000);
    }

}
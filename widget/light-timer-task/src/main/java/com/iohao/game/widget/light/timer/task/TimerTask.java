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

import org.cache2k.expiry.ValueWithExpiryTime;

import java.io.Serializable;

/**
 * 定时任务
 *
 * @author 渔民小镇
 * @date 2021-12-25
 */
public interface TimerTask extends ValueWithExpiryTime, Serializable {
    /**
     * 执行方法
     * <pre>
     *     时间到就执行, 否则就不执行
     * </pre>
     */
    void execute();

    /**
     * 启动定时器任务
     *
     * @return me
     */
    <T extends TimerTask> T task();

    /**
     * 取消定时任务
     */
    void cancel();

    /**
     * 暂停任务一段时间
     * <pre>
     *     连续调用暂停时间不会累加, 需要到任务加入到队列中
     * </pre>
     *
     * @param stopTimeMillis 暂停的时间
     */
    void pause(long stopTimeMillis);

}

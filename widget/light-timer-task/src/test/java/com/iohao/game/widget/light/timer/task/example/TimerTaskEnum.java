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
package com.iohao.game.widget.light.timer.task.example;

import com.iohao.game.widget.light.timer.task.Cache2Kit;
import com.iohao.game.widget.light.timer.task.TimerTask;
import com.iohao.game.widget.light.timer.task.TimerTaskRegion;
import lombok.Getter;
import org.cache2k.Cache;

/**
 * 任务延时器域 - 枚举
 * <pre>
 *     1. 如果枚举类实现 TimerSerializable 接口,
 *     可以将任务序列化和反序列化到数据库中[redis] (实现该接口不是必须的)
 *
 *     2. 必须提供 cache 属性, 用于管理定时任务类
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-25
 */
public enum TimerTaskEnum implements TimerTaskRegion {
    /**
     * 任务延时器管理域 - 说话
     * <pre>
     *     key is uuid
     *     value is {@link HelloTask}
     * </pre>
     */
    HELLO,

    STUDENT;

    /** 每个枚举对应一个缓存管理器 */
    @Getter
    private final Cache<String, TimerTask> cache = Cache2Kit.createCache();
}


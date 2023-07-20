/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
package com.iohao.game.bolt.broker.client.external.bootstrap.heart;

import com.iohao.game.bolt.broker.client.external.bootstrap.handler.IdleHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 心跳相关设置
 * <pre>
 *     关于心跳相关的可以参考这里：
 *     https://www.yuque.com/iohao/game/uueq3i
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-03-13
 */
@ToString
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(fluent = true)
public class IdleProcessSetting {
    long idleTime = 5;
    /** 读 - 心跳时间 */
    long readerIdleTime = idleTime;
    /** 写 - 心跳时间 */
    long writerIdleTime = idleTime;
    /** all - 心跳时间 */
    long allIdleTime = idleTime;
    /** 心跳时间单位 - 默认秒单位 */
    TimeUnit timeUnit = TimeUnit.SECONDS;
    /** 心跳钩子事件回调， 如果对触发心跳有特殊的业务，用户可以重写这个接口 */
    IdleHook idleHook = new IdleHookDefault();
    /** 心跳处理，默认使用 IdleHandler */
    ChannelHandler idleHandler;
    /** true : 响应心跳给客户端 */
    boolean pong = true;

    public void idlePipeline(ChannelPipeline pipeline) {

        this.defaultSetting();

        // 心跳处理
        pipeline.addLast("idleStateHandler",
                new IdleStateHandler(
                        this.readerIdleTime,
                        this.writerIdleTime,
                        this.allIdleTime,
                        this.timeUnit));

        // 心跳业务
        pipeline.addLast("idleHandler", this.idleHandler);
    }

    private void defaultSetting() {
        if (Objects.isNull(idleHandler)) {
            idleHandler = new IdleHandler(this.idleHook, this.pong);
        }
    }

}

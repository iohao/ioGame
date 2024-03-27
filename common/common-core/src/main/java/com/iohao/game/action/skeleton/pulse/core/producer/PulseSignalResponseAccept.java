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
package com.iohao.game.action.skeleton.pulse.core.producer;

import com.iohao.game.action.skeleton.pulse.core.PulseChannel;
import com.iohao.game.action.skeleton.pulse.message.PulseSignalResponse;

/**
 * 接收-脉冲信号响应
 * <pre>
 *     脉冲生产者回调
 *     当接收到脉冲消费者的响应后，会使用此接口来接收响应数据
 * </pre>
 *
 * @param <T> 可以明确响应的具体业务数据类型
 * @author 渔民小镇
 * @date 2023-04-23
 */

public interface PulseSignalResponseAccept<T> extends PulseChannel {
    /**
     * 接收脉冲信号响应，由脉冲消费者响应的数据
     *
     * @param message  业务数据
     * @param response 脉冲信号响应
     */
    void accept(T message, PulseSignalResponse response);
}

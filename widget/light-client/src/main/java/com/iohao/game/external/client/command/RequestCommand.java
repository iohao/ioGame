/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.external.client.command;

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.protocol.wrapper.IntValue;
import com.iohao.game.action.skeleton.protocol.wrapper.LongValue;
import com.iohao.game.external.client.user.ClientUserChannel;
import com.iohao.game.external.client.user.ClientUserInputCommands;

/**
 * 请求命令执行。用于请求服务器的命令，业务数据需要在调用 request 方法时传入。
 *
 * @author 渔民小镇
 * @date 2023-07-14
 */
public record RequestCommand(InputCommand inputCommand, ClientUserInputCommands clientUserInputCommands) {

    public void request(long value) {
        LongValue longValue = LongValue.of(value);
        this.request(longValue);
    }

    public void request(int value) {
        IntValue intValue = IntValue.of(value);
        this.request(intValue);
    }

    public void request() {
        this.request(null);
    }

    /**
     * 向服务器发起请求
     *
     * @param requestData 请求业务参数
     */
    public void request(Object requestData) {
        CmdInfo cmdInfo = inputCommand.getCmdInfo();
        Class<?> responseClass = inputCommand.getResponseClass();
        InputCallback callback = inputCommand.getCallback();
        ClientUserChannel clientUserChannel = clientUserInputCommands.getClientUserChannel();
        clientUserChannel.request(cmdInfo, requestData, responseClass, callback);
    }

    /**
     * 向服务器发起请求，会根据模拟请求配置来生成请求参数
     * <pre>
     *     当对应的模拟请求当配置了 inputRequestData 动态请求参数生成时，优先动态生成；
     *     否则使用配置时的 requestData 对象
     * </pre>
     */
    public void requestInput() {
        // 动态请求参数生成
        Object requestData = inputCommand.getRequestData();
        request(requestData);
    }
}

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
package com.iohao.game.external.client.command;

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.protocol.wrapper.IntValue;
import com.iohao.game.action.skeleton.protocol.wrapper.LongValue;
import com.iohao.game.external.client.user.ClientUserChannel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * 请求命令执行。用于请求服务器的命令，业务数据需要在调用 request 方法时传入。
 *
 * @author 渔民小镇
 * @date 2023-07-14
 */
@Getter
@Setter
@Accessors(chain = true)
public class RequestCommand {
    ClientUserChannel clientUserChannel;
    int cmdMerge;
    String title = "... ...";
    /** 请求参数 */
    RequestDataDelegate requestData;
    CallbackDelegate callback;
    @Deprecated
    Class<?> responseClass;

    @Deprecated
    public void request(long value) {
        LongValue longValue = LongValue.of(value);
        this.request(longValue);
    }

    @Deprecated
    public void request(int value) {
        IntValue intValue = IntValue.of(value);
        this.request(intValue);
    }

    /**
     * 向服务器发起请求，会根据模拟请求配置来生成请求参数
     * <pre>
     *     当对应的模拟请求当配置了 inputRequestData 动态请求参数生成时，优先动态生成；
     *     否则使用配置时的 requestData 对象
     * </pre>
     */
    @Deprecated
    public void request() {

        Object data = null;
        if (Objects.nonNull(requestData)) {
            data = requestData.createRequestData();
        }

        this.request(data);
    }

    /**
     * 向服务器发起请求
     *
     * @param data 请求业务参数
     */
    @Deprecated
    public void request(Object data) {
        CmdInfo cmdInfo = CmdInfo.of(this.cmdMerge);
        clientUserChannel.request(cmdInfo, data, responseClass, callback);
    }

    public void execute() {
        clientUserChannel.execute(this);
    }
}

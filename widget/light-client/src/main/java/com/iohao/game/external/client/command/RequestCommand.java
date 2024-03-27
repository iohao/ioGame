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
package com.iohao.game.external.client.command;

import com.iohao.game.external.client.user.ClientUserChannel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
    /** 响应回调 */
    CallbackDelegate callback;

    /**
     * 执行请求命令
     */
    public void execute() {
        clientUserChannel.execute(this);
    }
}

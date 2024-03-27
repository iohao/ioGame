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

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.common.kit.StrKit;
import com.iohao.game.external.client.kit.ClientKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * 模拟命令
 * example:
 * <pre>{@code
 *         ofCommand(DemoCmd.here).setTitle("here").setRequestData(() -> {
 *             YourMsg msg = ...
 *             return msg;
 *         }).callback(result -> {
 *              HelloReq value = result.getValue(HelloReq.class);
 *              log.info("value : {}", value);
 *          });
 *
 *         ofCommand(DemoCmd.list).setTitle("list").callback(result -> {
 *             // 得到 list 数据
 *             List<HelloReq> list = result.listValue(HelloReq.class);
 *             log.info("list : {}", list);
 *         });
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-07-08
 */
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InputCommand {

    final String inputName;
    final CmdInfo cmdInfo;

    /** 模拟请求命令的描述 */
    String title = "... ...";
    /** 描述的前缀 */
    String cmdName = "";

    /** 请求参数 */
    RequestDataDelegate requestData;
    /** 回调接口 */
    @Setter(AccessLevel.PRIVATE)
    CallbackDelegate callback;

    public InputCommand(CmdInfo cmdInfo) {
        this.inputName = ClientKit.toInputName(cmdInfo);
        this.cmdInfo = cmdInfo;
    }

    public InputCommand setRequestData(RequestDataDelegate requestData) {
        this.requestData = requestData;
        return this;
    }

    public InputCommand callback(CallbackDelegate callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public String toString() {
        if (StrKit.isEmpty(cmdName)) {
            var format = "%s    :    %s";
            return String.format(format, inputName, title);
        }

        var format = "%s    :    [%s] - %s";
        return String.format(format, inputName, cmdName, title);
    }
}

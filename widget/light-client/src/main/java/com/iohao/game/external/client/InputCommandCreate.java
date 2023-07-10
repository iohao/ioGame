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
package com.iohao.game.external.client;

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.protocol.wrapper.LongValue;
import com.iohao.game.external.client.input.*;
import com.iohao.game.external.client.kit.AssertKit;
import com.iohao.game.external.client.kit.ClientKit;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2023-07-09
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PUBLIC)
public class InputCommandCreate {
    int cmd = -1;
    /** true 相同路由的 InputCommand 只能存在一个 */
    boolean uniqueInputCommand = true;

    public CmdInfo getCmdInfo(int subCmd) {
        AssertKit.assertTrue(cmd >= 0, "cmd 不能小于 0");
        return CmdInfo.getCmdInfo(cmd, subCmd);
    }

    /**
     * 向服务器发起请求
     *
     * @param subCmd 请求子路由
     */
    public void request(int subCmd) {
        CmdInfo cmdInfo = getCmdInfo(subCmd);
        InputCommands.request(cmdInfo);
    }

    public InputCommand createInputCommand(int subCmd, String desc) {
        return createInputCommand(subCmd, desc, null);
    }

    public InputCommand createInputCommand(int subCmd,
                                           String desc,
                                           InputRequestData inputRequestData) {

        CmdInfo cmdInfo = getCmdInfo(subCmd);

        // 先检查命令是否存在
        if (uniqueInputCommand) {
            var inputName = InputCommands.toInputName(cmdInfo);
            InputCommand inputCommand = InputCommands.getInputCommand(inputName);
            if (Objects.nonNull(inputCommand)) {
                throw new RuntimeException("存在重复的路由命令 : " + cmdInfo);
            }
        }

        return InputCommands.createCommand(cmdInfo)
                .setDescription(desc)
                .setInputRequestData(inputRequestData);
    }

    public InputCommand createInputCommandLong(int subCmd, String desc) {
        InputRequestData inputRequestData = createNextUserId();
        return createInputCommand(subCmd, desc, inputRequestData);
    }

    public InputRequestData createNextUserId() {
        return createNextLong("对方的 userId");
    }

    public InputRequestData createNextLong(String requestDataDescription) {
        return () -> {
            String info = "请输入{} | 参数类型 : {}";
            log.info(info, requestDataDescription, long.class);

            String s = ClientKit.scanner.nextLine();
            long longValue = Long.parseLong(s);
            return LongValue.of(longValue);
        };
    }

    /**
     * 广播监听
     * <pre>
     *     监听游戏服务器广播的消息
     * </pre>
     *
     * @param subCmd        子路由
     * @param responseClass 响应后使用这个 class 来解析 data 数据
     * @param callback      结果回调（游戏服务器回传的结果）
     * @param description   描述
     */
    public void listenBroadcast(Class<?> responseClass, InputCallback callback, int subCmd, String description) {
        CmdInfo cmdInfo = getCmdInfo(subCmd);
        ExecuteCommandKit.listenBroadcast(cmdInfo, responseClass, callback, description);
    }

    /**
     * 广播监听
     * <pre>
     *     监听游戏服务器广播的消息
     * </pre>
     *
     * @param subCmd        子路由
     * @param responseClass 响应后使用这个 class 来解析 data 数据
     * @param callback      结果回调（游戏服务器回传的结果）
     */
    public void listenBroadcast(Class<?> responseClass, InputCallback callback, int subCmd) {
        listenBroadcast(responseClass, callback, subCmd, null);
    }
}

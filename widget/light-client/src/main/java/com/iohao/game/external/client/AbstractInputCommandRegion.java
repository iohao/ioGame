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
package com.iohao.game.external.client;

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.external.client.command.CallbackDelegate;
import com.iohao.game.external.client.command.InputCommand;
import com.iohao.game.external.client.command.ListenCommand;
import com.iohao.game.external.client.command.RequestCommand;
import com.iohao.game.external.client.user.ClientUser;
import com.iohao.game.external.client.user.ClientUserChannel;
import com.iohao.game.external.client.user.ClientUserInputCommands;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * @author 渔民小镇
 * @date 2023-07-10
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractInputCommandRegion implements InputCommandRegion {
    /** 模块域 */
    InputCommandCreate inputCommandCreate = new InputCommandCreate();
    ClientUser clientUser;
    long userId;

    @Override
    public void setClientUser(ClientUser clientUser) {
        this.userId = clientUser.getUserId();
        this.clientUser = clientUser;

        ClientUserInputCommands clientUserInputCommands = clientUser.getClientUserInputCommands();
        inputCommandCreate.setClientUserInputCommands(clientUserInputCommands);
    }

    /**
     * 创建模拟命令
     *
     * @param subCmd 子路由
     * @return InputCommand
     */
    protected InputCommand ofCommand(int subCmd) {
        return this.inputCommandCreate.ofInputCommand(subCmd);
    }

    /**
     * 创建模拟命令，在使用命令时需要在控制台输入 long 类型的请求参数
     *
     * @param subCmd 子路由
     * @return InputCommand
     */
    protected InputCommand ofCommandLong(int subCmd) {
        return this.inputCommandCreate.ofInputCommandLong(subCmd);
    }

    /**
     * 创建模拟命令，在使用命令时需要在控制台输入 long 类型的 userId 请求参数
     *
     * @param subCmd 子路由
     * @return InputCommand
     */
    protected InputCommand ofCommandUserId(int subCmd) {
        return this.inputCommandCreate.ofInputCommandUserId(subCmd);
    }

    /**
     * 创建模拟命令，在使用命令时需要在控制台输入 int 类型的请求参数
     *
     * @param subCmd 子路由
     * @return InputCommand
     */
    protected InputCommand ofCommandInt(int subCmd) {
        return inputCommandCreate.ofInputCommandInt(subCmd);
    }

    /**
     * 创建模拟命令，在使用命令时需要在控制台输入 String 类型的请求参数
     *
     * @param subCmd 子路由
     * @return InputCommand
     */
    protected InputCommand ofCommandString(int subCmd) {
        return this.inputCommandCreate.ofInputCommandString(subCmd);
    }

    /**
     * 广播监听
     * <pre>
     *     监听游戏服务器广播的消息
     * </pre>
     *
     * <pre>
     *     请使用 {@code this.ofListen} 代替
     * </pre>
     * example:
     * <pre>{@code
     *             ofListen(result -> {
     *                 DemoBroadcastMessage value = result.getValue(DemoBroadcastMessage.class);
     *                 log.info("broadcastMessage ========== \n{}", value);
     *             }, DemoBroadcastCmd.broadcastMsg, "helloBroadcast1");
     * }
     * </pre>
     *
     * @param subCmd        子路由
     * @param responseClass 响应后使用这个 class 来解析 data 数据
     * @param callback      结果回调（游戏服务器回传的结果）
     * @param title         描述
     */
    @Deprecated
    protected void listenBroadcast(Class<?> responseClass, CallbackDelegate callback, int subCmd, String title) {
        ofListen(subCmd)
                .setTitle(title)
                .setResponseClass(responseClass)
                .setCallback(callback);
    }

    protected void ofListen(CallbackDelegate callback, int subCmd, String title) {
        this.ofListen(subCmd)
                .setCallback(callback)
                .setTitle(title);
    }

    private ListenCommand ofListen(int subCmd) {
        CmdInfo cmdInfo = inputCommandCreate.ofCmdInfo(subCmd);
        ListenCommand listenCommand = new ListenCommand(cmdInfo);

        ClientUserChannel clientUserChannel = this.clientUser.getClientUserChannel();
        clientUserChannel.addListen(listenCommand);

        return listenCommand;
    }

    /**
     * 创建请求命令执行
     *
     * @param subCmd 子路由
     * @return 请求命令执行
     */
    public RequestCommand ofRequestCommand(int subCmd) {
        CmdInfo cmdInfo = this.inputCommandCreate.getCmdInfo(subCmd);
        ClientUserInputCommands clientUserInputCommands = this.inputCommandCreate.clientUserInputCommands;
        return clientUserInputCommands.ofRequestCommand(cmdInfo);
    }

    /**
     * 创建请求命令执行
     *
     * @param cmdInfo 路由信息
     * @return 请求命令执行
     */
    public RequestCommand ofRequestCommand(CmdInfo cmdInfo) {
        ClientUserInputCommands clientUserInputCommands = this.inputCommandCreate.clientUserInputCommands;
        return clientUserInputCommands.ofRequestCommand(cmdInfo);
    }
}

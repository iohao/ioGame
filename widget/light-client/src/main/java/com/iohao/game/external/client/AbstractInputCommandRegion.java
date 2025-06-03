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

    protected void ofListen(CallbackDelegate callback, int subCmd, String title) {
        this.ofListen(subCmd)
                .setCallback(callback)
                .setTitle(title);
    }

    protected void ofListen(CallbackDelegate callback, CmdInfo cmd, String title) {
        this.ofListen(cmd.getSubCmd())
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
        CmdInfo cmdInfo = this.inputCommandCreate.ofCmdInfo(subCmd);
        return this.ofRequestCommand(cmdInfo);
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

    public void executeCommand(int subCmd) {
        this.ofRequestCommand(subCmd).execute();
    }
}

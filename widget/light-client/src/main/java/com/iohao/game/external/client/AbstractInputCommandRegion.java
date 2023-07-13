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

import com.iohao.game.external.client.input.InputCallback;
import com.iohao.game.external.client.input.InputCommand;
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
     * @param subCmd        子路由
     * @param responseClass 响应后使用这个 class 来解析 data 数据
     * @param callback      结果回调（游戏服务器回传的结果）
     * @param description   描述
     */
    protected void listenBroadcast(Class<?> responseClass, InputCallback callback, int subCmd, String description) {
        this.inputCommandCreate.listenBroadcast(responseClass, callback, subCmd, description);
    }

    /**
     * 向服务器发起请求
     *
     * @param subCmd 请求子路由
     */
    protected void request(int subCmd) {
        this.inputCommandCreate.request(subCmd);
    }

    public InputCommand getInputCommand(int subCmd) {
        return inputCommandCreate.getInputCommand(subCmd);
    }
}

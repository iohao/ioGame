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
import com.iohao.game.external.client.kit.ClientKit;
import com.iohao.game.external.client.user.ClientUserChannel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * 广播监听
 *
 * @author 渔民小镇
 * @date 2023-07-09
 */
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListenCommand {
    final CmdInfo cmdInfo;
    String title;
    CallbackDelegate callback;
    ClientUserChannel clientUserChannel;

    @Deprecated
    Class<?> responseClass;

    public ListenCommand(CmdInfo cmdInfo) {
        this.cmdInfo = cmdInfo;
    }

    public void listen() {
        clientUserChannel.addListen(this);
    }

    @Override
    public String toString() {
        String inputName = ClientKit.toInputName(this.cmdInfo);
        return inputName + "    :    " + title;
    }
}

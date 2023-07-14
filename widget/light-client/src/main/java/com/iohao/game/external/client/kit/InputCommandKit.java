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
package com.iohao.game.external.client.kit;

import com.iohao.game.action.skeleton.core.CmdInfo;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 渔民小镇
 * @date 2023-07-09
 */
@Slf4j
@UtilityClass
public class InputCommandKit {
    public String toInputName(CmdInfo cmdInfo) {
        return cmdInfo.getCmd() + "-" + cmdInfo.getSubCmd();
    }

//
//    public InputCommand createInputCommand(CmdInfo cmdInfo, String desc) {
//        return createInputCommand(cmdInfo, desc, null);
//    }
//
//    public InputCommand createInputCommand(CmdInfo cmdInfo,
//                                           String desc,
//                                           InputRequestData inputRequestData) {
//
//        return InputCommands.createCommand(cmdInfo)
//                .setDescription(desc)
//                .setInputRequestData(inputRequestData);
//    }
//
//    public InputCommand createLongInputCommand(CmdInfo cmdInfo, String desc) {
//        InputRequestData inputRequestData = InputCommandKit.createNextUserId();
//        return createInputCommand(cmdInfo, desc, inputRequestData);
//    }
//
//    public InputRequestData createNextUserId() {
//        return createNextLong("对方的 userId");
//    }
//
//    public InputRequestData createNextLong(String requestDataDescription) {
//        return () -> {
//            String info = "请输入{} | 参数类型 : {}";
//            log.info(info, requestDataDescription, long.class);
//
//            String s = ClientKit.scanner.nextLine();
//            long userId = Long.parseLong(s);
//            return LongValue.of(userId);
//        };
//    }
}

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
package com.iohao.game.external.client.input;

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.common.kit.StrKit;
import com.iohao.game.external.client.kit.ClientKit;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2023-07-08
 */
@Slf4j
@UtilityClass
public class InputCommands {

    Map<String, InputCommand> inputCommandMap = new NonBlockingHashMap<>();

    public void addCommand(InputCommand inputCommand) {

        Objects.requireNonNull(inputCommand);
        String inputName = inputCommand.getInputName();

        // 验证输入命令的正确性
        inputCommandMap.put(inputName, inputCommand);
    }

    public InputCommand createCommand(CmdInfo cmdInfo) {
        InputCommand inputCommand = new InputCommand(cmdInfo);
        addCommand(inputCommand);
        return inputCommand;
    }

    public InputCommand getInputCommand(String inputName) {
        return inputCommandMap.get(inputName);
    }

    public void help() {
        System.out.println("---------- cmd help ----------");
        inputCommandMap.forEach((s, inputCommand) -> System.out.println(inputCommand.toString()));
        System.out.println("------------------------------");
    }

    public void start() {

        String input = "";

        while (!input.equalsIgnoreCase("q")) {

            System.out.println("提示：[命令执行 : cmd-subCmd] [退出 : q] [帮助 : help]");

            input = ClientKit.scanner.nextLine();
            input = input.trim();

            if (StrKit.isEmpty(input)) {
                continue;
            }

            InputCommand inputCommand = getInputCommand(input);

            if (Objects.isNull(inputCommand)) {
                help();
                continue;
            }

            System.out.println(inputCommand);

            try {
                // 发起请求
                ExecuteCommandKit.request(inputCommand);
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public static void main(String[] args) {

        InputCommand inputCommand0 = new InputCommand(126, 0);
        inputCommand0.setDescription("登录");

        InputCommand inputCommand1 = new InputCommand(126, 1);
        inputCommand1.setDescription("添加好友");

        addCommand(inputCommand0);
        addCommand(inputCommand1);

        start();
    }

}

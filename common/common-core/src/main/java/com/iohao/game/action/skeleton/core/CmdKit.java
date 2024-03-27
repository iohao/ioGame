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
package com.iohao.game.action.skeleton.core;

import lombok.experimental.UtilityClass;

/**
 * Cmd 工具
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
@UtilityClass
public class CmdKit {
    /**
     * 得到主路由
     * 从 cmdMerge 中获取 [高16位] 的数值
     *
     * @param cmdMerge 合并路由 cmdMerge
     * @return [高16位] 的数值
     */
    public int getCmd(int cmdMerge) {
        return cmdMerge >> 16;
    }

    /**
     * 得到子路由
     * 从 cmdMerge 中获取 [低16位] 的数值
     *
     * @param cmdMerge 合并路由 cmdMerge
     * @return [低16位] 的数值
     */
    public int getSubCmd(int cmdMerge) {
        return cmdMerge & 0xFFFF;
    }

    /**
     * 合并两个参数,分别存放在 [高16 和 低16]
     * <pre>
     *     cmd - 高16
     *     subCmd - 低16
     *     例如 cmd = 1; subCmd = 1;
     *     mergeCmd 的结果: 65537
     *     那么 mergeCmd 对应的二进制是: [0000 0000 0000 0001] [0000 0000 0000 0001]
     * </pre>
     *
     * @param cmd    主路由存放于合并结果的高16位, 该参数不得大于 32767
     * @param subCmd 子路由存放于合并结果的低16位, 该参数不得大于 65535
     * @return 合并的结果
     */
    public int merge(int cmd, int subCmd) {
        return (cmd << 16) + subCmd;
    }

    public String mergeToString(int cmdMerge) {
        int cmd = getCmd(cmdMerge);
        int subCmd = getSubCmd(cmdMerge);
        String template = "[cmd:%d - subCmd:%d - cmdMerge:%d]";
        return String.format(template, cmd, subCmd, cmdMerge);
    }

    public String mergeToShort(int cmdMerge) {
        int cmd = getCmd(cmdMerge);
        int subCmd = getSubCmd(cmdMerge);
        return String.format("[cmd:%d-%d %d]", cmd, subCmd, cmdMerge);
    }

    public String toString(int cmdMerge) {
        int cmd = getCmd(cmdMerge);
        int subCmd = getSubCmd(cmdMerge);
        String template = "cmd[%d - %d]";

        return String.format(template, cmd, subCmd);
    }
}

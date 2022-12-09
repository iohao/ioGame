/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
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
}

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

import lombok.Getter;

/**
 * cmdInfo 命令路由信息
 * <pre>
 *     平常大部分框架使用一个 cmd 来约定协议
 *     这里使用cmd,subCmd是为了模块的划分清晰, 当然这样规划还有更多好处
 *
 *     参考：
 *     https://www.yuque.com/iohao/game/soxp4u
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
@Getter
public final class CmdInfo {
    /** 业务主路由 */
    final int cmd;
    /** 业务子路由 */
    final int subCmd;

    /**
     * 合并两个参数,分别存放在 [高16 和 低16]
     * <pre>
     *     cmd - 高16
     *     subCmd - 低16
     *     例如 cmd = 600; subCmd = 700;
     *     merge 的结果: 39322300
     *     那么 cmdMerge 对应的二进制是: [0000 0010 0101 1000] [0000 0010 1011 1100]
     * </pre>
     */
    final int cmdMerge;

    final String info;

    CmdInfo(int cmd, int subCmd) {
        // -------------- 路由相关 --------------
        this.cmd = cmd;
        this.subCmd = subCmd;
        this.cmdMerge = CmdKit.merge(cmd, subCmd);

        this.info = CmdKit.mergeToString(this.cmdMerge);
    }

    public static CmdInfo getCmdInfo(int cmd, int subCmd) {
        return CmdInfoFlyweightFactory.me().getCmdInfo(cmd, subCmd);
    }

    @Override
    public String toString() {
        return info;
    }
}

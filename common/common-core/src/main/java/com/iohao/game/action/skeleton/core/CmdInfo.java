/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General  License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General  License for more details.
 *
 * You should have received a copy of the GNU General  License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.action.skeleton.core;

import lombok.Getter;

/**
 * cmdInfo 命令路由信息
 * <pre>
 *     平常大部分框架使用一个 cmd 来约定协议
 *     这里使用cmd,subCmd是为了模块的划分清晰, 当然这样规划还有更多好处
 *
 *     cmdInfo 的创建可以通过：
 *     1 {@link CmdInfoFlyweightFactory} 来完成
 *     2 {@link CmdInfo#getCmdInfo(int, int)} 静态方法来完成
 *
 *     其他参考：
 *     <a href="https://www.yuque.com/iohao/game/soxp4u">文档 - 路由信息</a>
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

    CmdInfo(int cmd, int subCmd) {
        // -------------- 路由相关 --------------
        this.cmd = cmd;
        this.subCmd = subCmd;
        this.cmdMerge = CmdKit.merge(cmd, subCmd);
    }

    /**
     * 获取 cmdInfo
     * <pre>
     *     内部使用享元工厂来获取 cmdInfo
     * </pre>
     *
     * @param cmd    主路由
     * @param subCmd 子路由
     * @return 路由信息
     */
    public static CmdInfo getCmdInfo(int cmd, int subCmd) {
        return CmdInfoFlyweightFactory.me().getCmdInfo(cmd, subCmd);
    }

    @Override
    public String toString() {
        return CmdKit.mergeToString(this.cmdMerge);
    }
}

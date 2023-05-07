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

import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.Objects;

/**
 * 享元工厂
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public final class CmdInfoFlyweightFactory {
    /**
     * <pre>
     * key : cmdMerge
     * value : cmdInfo
     * </pre>
     */
    final Map<Integer, CmdInfo> cmdInfoMap = new NonBlockingHashMap<>();

    /**
     * 获取路由信息
     *
     * @param cmd    主路由
     * @param subCmd 子路由
     * @return 路由信息
     */
    public CmdInfo getCmdInfo(int cmd, int subCmd) {
        int cmdMerge = CmdKit.merge(cmd, subCmd);
        return getCmdInfo(cmdMerge);
    }

    /**
     * 获取路由信息
     *
     * @param cmdMerge 主路由(高16) + 子路由(低16)
     * @return 路由信息
     */
    public CmdInfo getCmdInfo(int cmdMerge) {

        CmdInfo cmdInfo = cmdInfoMap.get(cmdMerge);

        // 无锁化
        if (Objects.isNull(cmdInfo)) {
            int cmd = CmdKit.getCmd(cmdMerge);
            int subCmd = CmdKit.getSubCmd(cmdMerge);

            cmdInfo = new CmdInfo(cmd, subCmd);
            cmdInfo = cmdInfoMap.putIfAbsent(cmdMerge, cmdInfo);
            if (Objects.isNull(cmdInfo)) {
                cmdInfo = cmdInfoMap.get(cmdMerge);
            }
        }

        return cmdInfo;
    }

    public static CmdInfoFlyweightFactory me() {
        return Holder.ME;
    }

    private CmdInfoFlyweightFactory() {
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final CmdInfoFlyweightFactory ME = new CmdInfoFlyweightFactory();
    }
}

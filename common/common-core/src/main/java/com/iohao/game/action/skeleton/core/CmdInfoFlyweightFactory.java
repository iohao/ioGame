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
        return getCmdInfo(cmd, subCmd, cmdMerge);
    }

    /**
     * 获取路由信息
     *
     * @param cmdMerge 主路由(高16) + 子路由(低16)
     * @return 路由信息
     */
    public CmdInfo getCmdInfo(int cmdMerge) {
        int cmd = CmdKit.getCmd(cmdMerge);
        int subCmd = CmdKit.getSubCmd(cmdMerge);
        return getCmdInfo(cmd, subCmd, cmdMerge);
    }

    private CmdInfo getCmdInfo(int cmd, int subCmd, int cmdMerge) {
        CmdInfo cmdInfo = cmdInfoMap.get(cmdMerge);

        // 无锁理念
        if (Objects.isNull(cmdInfo)) {
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

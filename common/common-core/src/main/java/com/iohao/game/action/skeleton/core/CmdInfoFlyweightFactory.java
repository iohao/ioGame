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
    static final Map<Integer, CmdInfo> cmdInfoMap = new NonBlockingHashMap<>();

    /**
     * 获取路由信息
     * <pre>
     *     将在下个版本移除，请使用 of 系列代替
     * </pre>
     *
     * @param cmd    主路由
     * @param subCmd 子路由
     * @return 路由信息
     */
    @Deprecated
    public static CmdInfo getCmdInfo(int cmd, int subCmd) {
        return of(cmd, subCmd);
    }

    /**
     * 获取路由信息
     * <pre>
     *     将在下个版本移除，请使用 of 系列代替
     * </pre>
     *
     * @param cmdMerge 主路由(高16) + 子路由(低16)
     * @return 路由信息
     */
    @Deprecated
    public static CmdInfo getCmdInfo(int cmdMerge) {
        return of(cmdMerge);
    }


    /**
     * 获取路由信息
     * <pre>
     *     如果不存在，就新建
     * </pre>
     *
     * @param cmd    主路由
     * @param subCmd 子路由
     * @return 路由信息
     */
    public static CmdInfo of(int cmd, int subCmd) {
        int cmdMerge = CmdKit.merge(cmd, subCmd);
        return of(cmdMerge);
    }

    /**
     * 获取路由信息
     * <pre>
     *     如果不存在，就新建
     * </pre>
     *
     * @param cmdMerge 主路由(高16) + 子路由(低16)
     * @return 路由信息
     */
    public static CmdInfo of(int cmdMerge) {

        CmdInfo cmdInfo = cmdInfoMap.get(cmdMerge);

        // 无锁化
        if (Objects.isNull(cmdInfo)) {
            cmdInfo = cmdInfoMap.putIfAbsent(cmdMerge, new CmdInfo(cmdMerge));
            if (Objects.isNull(cmdInfo)) {
                cmdInfo = cmdInfoMap.get(cmdMerge);
            }
        }

        return cmdInfo;
    }

    /**
     * 请直接使用静态方法
     * <pre>
     *     将在下个大版本中移除
     * </pre>
     *
     * @return me
     */
    @Deprecated
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

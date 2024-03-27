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

import com.iohao.game.common.kit.MoreKit;
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
            var newValue = new CmdInfo(cmdMerge);
            return MoreKit.putIfAbsent(cmdInfoMap, cmdMerge, newValue);
        }

        return cmdInfo;
    }
}

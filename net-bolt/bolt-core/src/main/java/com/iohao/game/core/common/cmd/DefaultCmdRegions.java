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
package com.iohao.game.core.common.cmd;


import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.common.kit.MoreKit;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;
import org.jctools.maps.NonBlockingHashSet;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author 渔民小镇
 * @date 2023-04-30
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class DefaultCmdRegions implements CmdRegions {
    /**
     * 路由与区域映射
     * <pre>
     *     key : cmdMerge
     *     value : CmdRegion
     * </pre>
     */
    final Map<Integer, CmdRegion> cmdRegionMap = new NonBlockingHashMap<>();

    /**
     * 游戏逻辑服与对应的 CmdRegion set
     * <pre>
     *     key : 游戏逻辑服 id
     *     value : CmdRegion set
     * </pre>
     */
    final Map<Integer, Set<CmdRegion>> logicServerCmdRegionMap = new NonBlockingHashMap<>();

    @Override
    public void loading(BrokerClientModuleMessage moduleMessage) {
        String id = moduleMessage.getId();
        int idHash = moduleMessage.getIdHash();
        BrokerClientId brokerClientId = new BrokerClientId(idHash, id);

        for (Integer cmdMerge : moduleMessage.getCmdMergeList()) {
            CmdRegion cmdRegion = this.getCmdRegion(cmdMerge);
            cmdRegion.addIdHash(brokerClientId);

            Set<CmdRegion> cmdRegionSet = this.getCmdRegionSet(idHash);
            cmdRegionSet.add(cmdRegion);
        }
    }

    @Override
    public void unLoading(BrokerClientId brokerClientId) {
        int idHash = brokerClientId.idHash();
        // 只简单的移除 idHash 不销毁对象
        this.getCmdRegionSet(idHash).forEach(cmdRegion -> cmdRegion.removeIdHash(brokerClientId));
    }

    @Override
    public boolean existCmdMerge(int cmdMerge) {
        CmdRegion cmdRegion = this.cmdRegionMap.get(cmdMerge);
        // cmdRegion 存在，并且是有游戏逻辑服 id 的
        return Objects.nonNull(cmdRegion) && cmdRegion.hasIdHash();
    }

    @Override
    public int endPointLogicServerId(int cmdMerge, int[] idHashArray) {

        CmdRegion cmdRegion = this.cmdRegionMap.get(cmdMerge);
        if (Objects.isNull(cmdRegion)) {
            return 0;
        }

        return cmdRegion.endPointLogicServerId(idHashArray);
    }

    private Set<CmdRegion> getCmdRegionSet(int idHash) {
        Set<CmdRegion> cmdRegionSet = this.logicServerCmdRegionMap.get(idHash);

        // 无锁化
        if (Objects.isNull(cmdRegionSet)) {
            Set<CmdRegion> newValue = new NonBlockingHashSet<>();
            return MoreKit.putIfAbsent(this.logicServerCmdRegionMap, idHash, newValue);
        }

        return cmdRegionSet;
    }

    private CmdRegion getCmdRegion(int cmdMerge) {
        // cmdMerge 与 idHash 关联
        CmdRegion cmdRegion = this.cmdRegionMap.get(cmdMerge);

        // 无锁化
        if (Objects.isNull(cmdRegion)) {
            CmdRegion newValue = new DefaultCmdRegion(cmdMerge);
            return MoreKit.putIfAbsent(cmdRegionMap, cmdMerge, newValue);
        }

        return cmdRegion;
    }
}

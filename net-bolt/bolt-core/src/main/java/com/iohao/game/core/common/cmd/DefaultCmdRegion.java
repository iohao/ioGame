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

import com.iohao.game.action.skeleton.core.CmdKit;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashSet;

import java.util.Set;

/**
 * @author 渔民小镇
 * @date 2023-04-30
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class DefaultCmdRegion implements CmdRegion {
    final Set<BrokerClientId> clientIdSet = new NonBlockingHashSet<>();
    final Set<Integer> clientIdHashSet = new NonBlockingHashSet<>();

    final int cmdMerge;

    public DefaultCmdRegion(int cmdMerge) {
        this.cmdMerge = cmdMerge;
    }

    @Override
    public void addIdHash(BrokerClientId brokerClientId) {
        this.clientIdSet.add(brokerClientId);
        this.clientIdHashSet.add(brokerClientId.idHash());
    }

    @Override
    public void removeIdHash(BrokerClientId brokerClientId) {
        this.clientIdSet.remove(brokerClientId);
        this.clientIdHashSet.remove(brokerClientId.idHash());
    }

    @Override
    public int endPointLogicServerId(int[] idHashArray) {
        for (int idHash : idHashArray) {
            if (this.clientIdHashSet.contains(idHash)) {
                return idHash;
            }
        }

        return 0;
    }

    @Override
    public boolean hasIdHash() {
        return !this.clientIdSet.isEmpty();
    }

    @Override
    public String toString() {
        return "CmdRegion {" +
                CmdKit.toString(cmdMerge) +
                " -- BrokerClientIdSet : " + clientIdSet +
                '}';
    }
}

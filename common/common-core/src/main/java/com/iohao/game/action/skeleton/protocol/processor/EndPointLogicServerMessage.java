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
package com.iohao.game.action.skeleton.protocol.processor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 玩家绑定逻辑服
 *
 * @author 渔民小镇
 * @date 2022-05-28
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EndPointLogicServerMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = -281565377520818401L;

    /** 用户 id */
    List<Long> userList;

    /** 需要绑定的多个游戏逻辑服 id */
    Set<String> logicServerIdSet;

    EndPointOperationEnum operation;

    /**
     * 添加需要绑定的游戏逻辑服 id
     *
     * @param logicServerId 需要绑定的游戏逻辑服 id
     * @return this
     */
    public EndPointLogicServerMessage addLogicServerId(String logicServerId) {
        Objects.requireNonNull(logicServerId);
        this.initSet();
        this.logicServerIdSet.add(logicServerId);
        return this;
    }

    /**
     * 添加需要绑定的游戏逻辑服 id
     * <p>
     * 使用示例
     * <pre>
     *     new EndPointLogicServerMessage().addLogicServerId(Set.of("1-1", "5-1"));
     * </pre>
     *
     * @param idSet 需要绑定的游戏逻辑服 id Set
     * @return this
     */
    public EndPointLogicServerMessage addLogicServerId(Set<String> idSet) {
        Objects.requireNonNull(idSet);
        this.initSet();
        this.logicServerIdSet.addAll(idSet);
        return this;
    }

    private void initSet() {
        if (Objects.isNull(this.logicServerIdSet)) {
            this.logicServerIdSet = new HashSet<>();
        }
    }
}

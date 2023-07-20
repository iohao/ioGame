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
package com.iohao.game.bolt.broker.core.message;

import com.iohao.game.bolt.broker.core.client.BrokerClientType;
import com.iohao.game.common.kit.HashKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 模块信息 （游戏服的信息、逻辑服）
 * <pre>
 *     一个逻辑服表示一个模块
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrokerClientModuleMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = -1570849960266785141L;
    /** 服务器唯一标识 */
    String id;
    /** 服务器唯一标识 hash */
    int idHash;
    /** 模块名 */
    String name;
    /** 逻辑服地址 */
    String address;
    List<Integer> cmdMergeList;
    /** 逻辑服类型 */
    BrokerClientType brokerClientType = BrokerClientType.LOGIC;

    /**
     * 逻辑服标签 （tag 相当于归类）
     * <pre>
     *     用于逻辑服的归类
     *     假设逻辑服： 战斗逻辑服 启动了两台或以上，为了得到启动连接的逻辑服，我们可以通过 tag 在后台查找
     *     相同的逻辑服一定要用相同的 tag
     *
     *     注意，如果没设置这个值，会使用 this.name 的值
     * </pre>
     */
    String tag;
    /** 逻辑服状态 */
    int status;
    int withNo;

    public BrokerClientModuleMessage setId(String id) {
        this.id = id;
        this.idHash = HashKit.hash32(id);
        return this;
    }

    @Override
    public String toString() {
        return "BrokerClientModuleMessage{" +
                "name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", id='" + id + '\'' +
                ", idHash=" + idHash +
                ", address='" + address + '\'' +
                ", brokerClientType=" + brokerClientType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof BrokerClientModuleMessage that)) {
            return false;
        }

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

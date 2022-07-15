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
package com.iohao.game.bolt.broker.core.message;

import com.iohao.game.bolt.broker.core.client.BrokerClientType;
import com.iohao.game.common.kit.MurmurHash3;
import com.iohao.game.common.kit.ToJson;
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
public class BrokerClientModuleMessage implements Serializable, ToJson {
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

    public BrokerClientModuleMessage setId(String id) {
        this.id = id;
        this.idHash = MurmurHash3.hash32(id);
        return this;
    }

    @Override
    public String toString() {
        return "BrokerClientMessage{" +
                "name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", brokerClientType=" + brokerClientType +
                ", address='" + address + '\'' +
                ", cmdMergeSize=" + (Objects.isNull(cmdMergeList) ? 0 : cmdMergeList.size()) +
                ", id='" + id + '\'' +
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

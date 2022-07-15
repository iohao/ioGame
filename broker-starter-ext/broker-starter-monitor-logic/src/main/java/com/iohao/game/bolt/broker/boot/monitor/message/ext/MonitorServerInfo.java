/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.bolt.broker.boot.monitor.message.ext;

import com.iohao.game.bolt.broker.core.client.BrokerClientType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 服务器信息
 *
 * @author 渔民小镇
 * @date 2022-06-04
 */
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonitorServerInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -6727104440916358990L;

    /** 服务器唯一标识 */
    String id;
    /**
     * 逻辑服标签 （tag 相当于归类）
     * <pre>
     *     用于逻辑服的归类
     *     假设逻辑服： 战斗逻辑服 启动了两台或以上，为了得到启动连接的逻辑服，我们可以通过 tag 在后台查找
     *     相同的逻辑服一定要用相同的 tag
     *
     *     注意，如果没设置这个值，会使用 this.appName 的值
     * </pre>
     */
    String tag;
    /** 模块名 */
    String name;
    /** 逻辑服类型 */
    BrokerClientType brokerClientType;
    /** 当前逻辑服 action 数据 */
    List<Integer> cmdMergeList;
    /** 对外服扩展属性 */
    MonitorExternalExt monitorExternalExt;
}

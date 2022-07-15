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
package com.iohao.game.action.skeleton.protocol.processor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

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
    /**
     * 绑定的逻辑服id
     * <pre>
     *     由 逻辑服的 id 转为 hash32
     *     see {@link com.iohao.game.common.kit.MurmurHash3#hash32(String)}
     * </pre>
     */
    String logicServerId;

    /** 用户 id */
    List<Long> userList;

    /**
     * 绑定类型
     * <pre>
     *     true 绑定逻辑服id
     *     false 清除绑定的逻辑服id
     * </pre>
     */
    boolean binding;
}

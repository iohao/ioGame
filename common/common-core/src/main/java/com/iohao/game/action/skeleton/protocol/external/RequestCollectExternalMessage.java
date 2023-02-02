/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.action.skeleton.protocol.external;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

/**
 * 游戏逻辑服访问游戏对外服，同时访问多个游戏对外服 - 请求
 * <pre>
 *     游戏逻辑服访问游戏对外服，因为只有游戏对外服持有这些数据
 *     把多个游戏对外服的结果聚合在一起
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-07-27
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class RequestCollectExternalMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = -1661393033598374514L;

    /**
     * 业务码
     * <pre>
     *     游戏开发者从正数开始使用
     *     框架会使用负数
     *
     *     游戏开发者可以通过自定义业务码，来获取一些对外服的业务数据，方便进行一些特殊的业务
     * </pre>
     */
    int bizCode;
    /** 请求业务数据 */
    Serializable data;

    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return (T) data;
    }
}

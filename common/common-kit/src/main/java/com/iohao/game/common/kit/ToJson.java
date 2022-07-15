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
package com.iohao.game.common.kit;


/**
 * 对象 json 相关
 *
 * @author 渔民小镇
 * @date 2022-03-29
 */
public interface ToJson {
    /**
     * 将对象转换成 json 格式
     *
     * @return json String
     */
    default String toJson() {
        return JsonKit.toJson(this);
    }

    /**
     * 将对象转换成 json Pretty 格式
     *
     * @return json
     */
    default String toJsonPretty() {
        return JsonKit.toJsonPretty(this);
    }
}

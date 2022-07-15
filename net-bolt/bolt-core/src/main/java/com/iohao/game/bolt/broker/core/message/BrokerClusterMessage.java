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
package com.iohao.game.bolt.broker.core.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 通知客户端有 broker 上线或下线
 *
 * @author 渔民小镇
 * @date 2022-05-15
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrokerClusterMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 2753485289174578530L;

    List<BrokerMessage> brokerMessageList;

    public int count() {
        if (Objects.isNull(brokerMessageList)) {
            return 0;
        }

        return this.brokerMessageList.size();
    }
}

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
package com.iohao.game.bolt.broker.core.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
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
    String name;
    List<BrokerMessage> brokerMessageList;

    public int count() {
        if (Objects.isNull(brokerMessageList)) {
            return 0;
        }

        return this.brokerMessageList.size();
    }

    public List<BrokerMessage> getBrokerMessageList() {
        if (Objects.isNull(this.brokerMessageList)) {
            return Collections.emptyList();
        }

        return this.brokerMessageList;
    }
}

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
package com.iohao.game.action.skeleton.eventbus;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * 事件总线逻辑服相关信息
 *
 * @author 渔民小镇
 * @date 2023-12-24
 * @since 21
 */
@Data
public final class EventBrokerClientMessage implements Serializable {
    final String appName;
    final String tag;
    final String brokerClientType;
    final String brokerClientId;
    transient boolean remote;
    /** 订阅者主题 */
    transient EventTopicMessage eventTopicMessage;

    public EventBrokerClientMessage(String appName, String tag, String brokerClientType, String brokerClientId) {
        this.appName = appName;
        this.tag = tag;
        this.brokerClientType = brokerClientType;
        this.brokerClientId = Objects.requireNonNull(brokerClientId);
    }

    public Collection<String> getTopics() {
        return eventTopicMessage.topicSet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof EventBrokerClientMessage that)) {
            return false;
        }

        return brokerClientId.equals(that.brokerClientId);
    }

    @Override
    public int hashCode() {
        return brokerClientId.hashCode();
    }
}

/*
 * ioGame
 * Copyright (C) 2021 - 2024  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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

import com.iohao.game.common.kit.MoreKit;
import lombok.experimental.UtilityClass;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2024-01-21
 */
@UtilityClass
class EventBusAnyTagRegion {
    /**
     * key : EventBrokerClientMessage tag
     */
    Map<BrokerClientTag, AnyTagBrokerClient> map = new NonBlockingHashMap<>();

    AnyTagView anyTagView = new AnyTagView();

    AnyTagViewData getAnyTagData(EventBusMessage message) {
        return anyTagView.getAnyTagData(message);
    }

    void add(EventBrokerClientMessage eventBrokerClientMessage) {
        // 将当前进程和远程的 EventBrokerClientMessage 添加到对应的 tag 中
        BrokerClientTag tag = BrokerClientTag.of(eventBrokerClientMessage.tag);

        AnyTagBrokerClient anyTagBrokerClient = getAnyTagBrokerClient(tag);
        anyTagBrokerClient.add(eventBrokerClientMessage);

        reload();
    }

    void remove(EventBrokerClientMessage eventBrokerClientMessage) {
        BrokerClientTag tag = BrokerClientTag.of(eventBrokerClientMessage.tag);

        AnyTagBrokerClient anyTagBrokerClient = getAnyTagBrokerClient(tag);
        anyTagBrokerClient.remove(eventBrokerClientMessage);

        // 如果 tag 没有任何数据时，从 map 中移除
        if (anyTagBrokerClient.isEmpty()) {
            map.remove(tag);
        }

        reload();
    }

    private AnyTagBrokerClient getAnyTagBrokerClient(BrokerClientTag tag) {
        AnyTagBrokerClient anyTagBrokerClient = map.get(tag);

        if (Objects.isNull(anyTagBrokerClient)) {
            AnyTagBrokerClient region = new AnyTagBrokerClient();
            return MoreKit.firstNonNull(map.putIfAbsent(tag, region), region);
        }

        return anyTagBrokerClient;
    }

    private void reload() {
        // 重新加载视图
        EventBusKit.executeSafe(() -> anyTagView.reload(map.values()));
    }
}

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

import com.esotericsoftware.reflectasm.MethodAccess;
import com.iohao.game.common.kit.TraceKit;
import org.slf4j.MDC;

import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2024-01-12
 */
record DefaultSubscriberInvoke(Subscriber subscriber) implements SubscriberInvoke {
    @Override
    public void invoke(EventBusMessage eventBusMessage) {
        var eventSource = eventBusMessage.getEventSource();
        var traceId = eventBusMessage.getTraceId();

        final boolean test = Objects.isNull(traceId) || Objects.nonNull(MDC.get(TraceKit.traceName));
        if (test) {
            this.invoke(eventSource);
            return;
        }

        try {
            // traceId
            MDC.put(TraceKit.traceName, traceId);
            this.invoke(eventSource);
        } finally {
            MDC.clear();
        }
    }

    void invoke(Object param) {
        MethodAccess methodAccess = this.subscriber.getMethodAccess();
        Object target = this.subscriber.getTarget();
        int methodIndex = this.subscriber.getMethodIndex();

        // 调用开发者在 action 类中编写的业务方法，即 action
        methodAccess.invoke(target, methodIndex, param);
    }
}

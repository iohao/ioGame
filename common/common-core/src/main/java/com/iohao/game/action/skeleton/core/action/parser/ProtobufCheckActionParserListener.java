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
package com.iohao.game.action.skeleton.core.action.parser;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashSet;

import java.util.Set;
import java.util.function.Predicate;

/**
 * proto 协议类型添检测
 *
 * @author 渔民小镇
 * @date 2024-05-02
 * @since 21.7
 */
@Slf4j
public final class ProtobufCheckActionParserListener implements ActionParserListener {
    final Set<Class<?>> protoSet = new NonBlockingHashSet<>();

    @Override
    public void onActionCommand(ActionParserContext context) {
        // 添加了 ProtobufClass 注解的类
        Predicate<Class<?>> protobufClassPredicate = c -> c.getAnnotation(ProtobufClass.class) == null;
        ProtobufActionParserListener.collect(context, protobufClassPredicate, protoSet);
    }

    @Override
    public void onAfter(BarSkeleton barSkeleton) {
        if (this.protoSet.isEmpty()) {
            return;
        }

        log.error("======== 注意，协议类没有添加 ProtobufClass 注解 ========");
        for (Class<?> protoClass : this.protoSet) {
            log.error(protoClass.toString());
        }
    }

    private ProtobufCheckActionParserListener() {
    }

    public static ProtobufCheckActionParserListener me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final ProtobufCheckActionParserListener ME = new ProtobufCheckActionParserListener();
    }
}
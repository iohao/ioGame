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
package com.iohao.game.action.skeleton.core;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.action.skeleton.core.action.parser.ActionParserContext;
import com.iohao.game.action.skeleton.core.action.parser.ActionParserListener;
import com.iohao.game.action.skeleton.i18n.Bundle;
import com.iohao.game.action.skeleton.i18n.MessageKey;
import com.iohao.game.action.skeleton.protocol.wrapper.*;
import com.iohao.game.common.kit.ProtoKit;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashSet;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Prepared action proto
 *
 * @author 渔民小镇
 * @date 2024-05-01
 * @since 21.7
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
final class ProtobufActionParserListener implements ActionParserListener {
    static final Set<Class<?>> protoSet = new NonBlockingHashSet<>();

    static {
        // create a protobuf proxy class
        ProtoKit.create(ByteValueList.class);

        ProtoKit.create(IntValue.class);
        ProtoKit.create(IntValueList.class);

        ProtoKit.create(BoolValue.class);
        ProtoKit.create(BoolValueList.class);

        ProtoKit.create(LongValue.class);
        ProtoKit.create(LongValueList.class);

        ProtoKit.create(StringValue.class);
        ProtoKit.create(StringValueList.class);
    }

    @Override
    public void onActionCommand(ActionParserContext context) {
        // 添加了 ProtobufClass 注解的类
        Predicate<Class<?>> protobufClassPredicate = c -> Objects.nonNull(c.getAnnotation(ProtobufClass.class));
        collect(context, protobufClassPredicate, protoSet);
    }

    static void collect(ActionParserContext context, Predicate<Class<?>> protobufClassPredicate, Set<Class<?>> protoSet) {
        // 将 action 的方法参数与返回值添加了 ProtobufClass 注解的类信息收集到 protoSet 中
        ActionCommand actionCommand = context.getActionCommand();
        // action 参数相关
        actionCommand.streamParamInfo()
                // 只处理业务参数
                .filter(ActionCommand.ParamInfo::isBizData)
                // 得到参数类型
                .map(ActionCommand.ParamInfo::getActualTypeArgumentClazz)
                // 协议碎片类型不做处理
                .filter(clazz -> !WrapperKit.isWrapper(clazz))
                // 添加了 ProtobufClass 注解的类
                .filter(protobufClassPredicate)
                .forEach(protoSet::add);

        // action 返回值相关
        Optional
                .ofNullable(actionCommand.getActionMethodReturnInfo())
                // void 不处理
                .filter(actionMethodReturnInfo -> !actionMethodReturnInfo.isVoid())
                .map(ActionCommand.ActionMethodReturnInfo::getActualTypeArgumentClazz)
                // 协议碎片类型不做处理
                .filter(clazz -> !WrapperKit.isWrapper(clazz))
                // 添加了 ProtobufClass 注解的类
                .filter(protobufClassPredicate)
                .ifPresent(protoSet::add);
    }

    @Override
    public void onAfter(BarSkeleton barSkeleton) {
        protoSet.forEach(ProtoKit::create);
    }
}

/**
 * proto 协议类型添检测
 *
 * @author 渔民小镇
 * @date 2024-05-02
 * @since 21.7
 */
@Slf4j
final class ProtobufCheckActionParserListener implements ActionParserListener {
    static final Set<Class<?>> protoSet = new NonBlockingHashSet<>();

    @Override
    public void onActionCommand(ActionParserContext context) {
        // 添加了 ProtobufClass 注解的类
        Predicate<Class<?>> protobufClassPredicate = c -> c.getAnnotation(ProtobufClass.class) == null;
        ProtobufActionParserListener.collect(context, protobufClassPredicate, protoSet);
    }

    @Override
    public void onAfter(BarSkeleton barSkeleton) {
        if (protoSet.isEmpty()) {
            return;
        }

        log.error(Bundle.getMessage(MessageKey.protobufAnnotationCheck));
        for (Class<?> protoClass : protoSet) {
            log.error(protoClass.toString());
        }
    }
}
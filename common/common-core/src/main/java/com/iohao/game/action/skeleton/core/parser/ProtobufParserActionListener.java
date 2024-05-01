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
package com.iohao.game.action.skeleton.core.parser;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.core.codec.ProtoDataCodec;
import com.iohao.game.action.skeleton.protocol.wrapper.*;
import com.iohao.game.common.kit.ProtoKit;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashSet;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author 渔民小镇
 * @date 2024-05-01
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ProtobufParserActionListener implements ParserActionListener {
    final Set<Class<?>> protoSet = new NonBlockingHashSet<>();

    @Override
    public void onActionCommand(ParserListenerContext context) {
        if (isNotProtoCodec()) {
            return;
        }

        // 将 action 的方法参数与返回值添加了 ProtobufClass 注解的类信息收集到 protoSet 中
        ActionCommand actionCommand = context.getActionCommand();

        actionCommand.streamParamInfo()
                .map(ActionCommand.ParamInfo::getParamClazz)
                .filter(paramClazz -> Objects.nonNull(paramClazz.getAnnotation(ProtobufClass.class)))
                .forEach(this.protoSet::add);

        // action 返回值相关
        ActionCommand.ActionMethodReturnInfo actionMethodReturnInfo = actionCommand.getActionMethodReturnInfo();
        Optional.ofNullable(actionMethodReturnInfo.getActualTypeArgumentClazz())
                .filter(actualTypeArgumentClazz -> Objects.nonNull(actualTypeArgumentClazz.getAnnotation(ProtobufClass.class)))
                .ifPresent(this.protoSet::add);
    }

    @Override
    public void onAfter(BarSkeleton barSkeleton) {
        if (isNotProtoCodec()) {
            return;
        }

        this.protoSet.forEach(ProtoKit::create);
    }

    private boolean isNotProtoCodec() {
        return !(DataCodecKit.getDataCodec() instanceof ProtoDataCodec);
    }

    private ProtobufParserActionListener() {
        if (isNotProtoCodec()) {
            return;
        }

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

    public static ProtobufParserActionListener me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final ProtobufParserActionListener ME = new ProtobufParserActionListener();
    }
}
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
package com.iohao.game.action.skeleton.core.doc;

import com.iohao.game.action.skeleton.annotation.DocActionSend;
import com.iohao.game.action.skeleton.annotation.DocActionSends;
import com.iohao.game.action.skeleton.core.CmdKit;
import lombok.Getter;
import org.jctools.maps.NonBlockingHashMap;
import com.iohao.game.action.skeleton.core.BarSkeletonBuilder;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author 渔民小镇
 * @date 2022-02-01
 * @deprecated 请使用 {@link BarSkeletonBuilder#addBroadcastDoc(BroadcastDocBuilder)}
 */
@Getter
@Deprecated
public final class ActionSendDocs {

    Map<Integer, ActionSendDoc> actionSendDocMap = new NonBlockingHashMap<>();

    public void add(ActionSendDoc actionSendDoc) {
        this.put(actionSendDoc);
    }

    private void put(ActionSendDoc actionSendDoc) {
        int cmdMerge = CmdKit.merge(actionSendDoc.getCmd(), actionSendDoc.getSubCmd());
        actionSendDocMap.put(cmdMerge, actionSendDoc);
    }

    public void buildActionSendDoc(List<Class<?>> actionSendClassList) {

        Set<Class<?>> classSet = new HashSet<>(actionSendClassList);

        // 条件: 类上配置了 ActionController 注解
        Predicate<Class<?>> predicate = controllerClazz -> Objects.nonNull(controllerClazz.getAnnotation(DocActionSends.class));

        classSet.stream().filter(predicate).forEach(actionSendClass -> {
            DocActionSends annotation = actionSendClass.getAnnotation(DocActionSends.class);
            DocActionSend[] docActionSends = annotation.value();
            for (DocActionSend docActionSend : docActionSends) {
                ActionSendDoc actionSendDoc = new ActionSendDoc(docActionSend);
                this.put(actionSendDoc);
            }
        });
    }
}

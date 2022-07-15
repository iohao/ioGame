/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.action.skeleton.core.doc;

import com.iohao.game.action.skeleton.annotation.DocActionSend;
import com.iohao.game.action.skeleton.annotation.DocActionSends;
import com.iohao.game.action.skeleton.core.CmdKit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author 渔民小镇
 * @date 2022-02-01
 */
@Slf4j
public class ActionSendDocs {

    @Getter
    Map<Integer, ActionSendDoc> actionSendDocMap = new NonBlockingHashMap<>();

    private void put(ActionSendDoc actionSendDoc) {
        int cmdMerge = CmdKit.merge(actionSendDoc.getCmd(), actionSendDoc.getSubCmd());
        actionSendDocMap.put(cmdMerge, actionSendDoc);
    }

    public ActionSendDoc getActionSendDoc(int cmd, int subCmd) {
        return this.getActionSendDoc(CmdKit.merge(cmd, subCmd));
    }

    public ActionSendDoc getActionSendDoc(int cmdMerge) {
        ActionSendDoc actionSendDoc = actionSendDocMap.get(cmdMerge);

        if (Objects.nonNull(actionSendDoc)) {
            actionSendDoc.setRead(true);
        }

        return actionSendDoc;
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

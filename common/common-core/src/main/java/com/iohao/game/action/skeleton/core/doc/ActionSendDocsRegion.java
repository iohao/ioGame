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

import com.iohao.game.action.skeleton.core.CmdKit;
import org.jctools.maps.NonBlockingHashMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2022-02-01
 */
public class ActionSendDocsRegion {
    Map<Integer, ActionSendDoc> actionSendDocMap = new NonBlockingHashMap<>();

    public void addActionSendDocs(ActionSendDocs actionSendDocs) {
        this.actionSendDocMap.putAll(actionSendDocs.getActionSendDocMap());
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

    public List<ActionSendDoc> listActionSendDoc() {
        return this.actionSendDocMap.values()
                .stream()
                .filter(actionSendDoc -> !actionSendDoc.isRead())
                .toList();
    }

}

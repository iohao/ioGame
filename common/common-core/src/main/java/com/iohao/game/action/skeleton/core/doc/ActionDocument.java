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

import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.core.CmdInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

/**
 * Action Document
 *
 * @author 渔民小镇
 * @date 2024-06-26
 * @since 21.11
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ActionDocument {
    final ActionDoc actionDoc;
    final TypeMappingDocument typeMappingDocument;

    List<ActionMemberCmdDocument> actionMemberCmdDocumentList = new ArrayList<>();
    List<ActionMethodDocument> actionMethodDocumentList = new ArrayList<>();

    ActionDocument(ActionDoc actionDoc, TypeMappingDocument typeMappingDocument) {
        this.actionDoc = actionDoc;
        this.typeMappingDocument = typeMappingDocument;
    }

    void analyse() {
        // action 方法
        actionDoc.stream().forEach(actionCommandDoc -> {
            // 成员变量
            actionMemberCmdDocumentList.add(generateMemberCmdCode(actionCommandDoc));
            // 方法
            var actionMethodDocument = new ActionMethodDocument(actionCommandDoc, typeMappingDocument);
            actionMethodDocumentList.add(actionMethodDocument);
        });
    }

    private ActionMemberCmdDocument generateMemberCmdCode(ActionCommandDoc actionCommandDoc) {
        ActionCommand actionCommand = actionCommandDoc.getActionCommand();

        CmdInfo cmdInfo = actionCommand.getCmdInfo();
        int cmd = cmdInfo.getCmd();
        int subCmd = cmdInfo.getSubCmd();

        String comment = actionCommandDoc.getComment();
        String actionMethodName = actionCommand.getActionMethodName();
        String memberName = "%s_%d_%d".formatted(actionMethodName, cmd, subCmd);

        return new ActionMemberCmdDocument(cmd, subCmd, memberName, comment);
    }
}

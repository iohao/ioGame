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
import com.iohao.game.common.kit.StrKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2024-06-26
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PACKAGE)
public final class ActionMethodDocument {
    final ActionCommandDoc actionCommandDoc;
    final TypeMappingDocument typeMappingDocument;
    /** 类名 */
    final String actionSimpleName;
    /** 方法名 */
    final String actionMethodName;
    /** 方法的注释 */
    final String methodComment;

    /** true 表示方法有参数 */
    final boolean hasBizData;
    /** 方法参数的名字 */
    String bizDataName;
    /** 方法参数的 */
    String bizDataType;
    /** 方法参数的注释 */
    String bizDataComment;
    /** true 表示参数是 List 类型 */
    boolean bizDataTypeIsList;
    /** true 表示协议碎片，false 表示开发者自定义的协议 */
    boolean bizDataTypeIsInternal;
    /** 参数类型（原始的，即使参数是 List，也会取泛型） */
    String actualTypeName;

    /** 使用的路由成员变量名 */
    String memberCmdName;
    /** 使用的 SDK api 名 */
    String sdkMethodName;

    boolean isVoid;
    /** 方法返回值的注释 */
    String returnComment;
    String returnDataName;
    /** 返回值类型（原始的，即使参数是 List，也会取泛型） */
    String returnDataActualTypeName;

    boolean returnDataIsList;
    /** true 表示协议碎片，false 表示开发者自定义的协议 */
    boolean returnDataTypeIsInternal;
    /** sdk result get 方法名 */
    String resultMethodTypeName;
    /** sdk result get list 方法名 */
    String resultMethodListTypeName;

    public ActionMethodDocument(ActionCommandDoc actionCommandDoc, TypeMappingDocument typeMappingDocument) {
        this.actionCommandDoc = actionCommandDoc;
        this.typeMappingDocument = typeMappingDocument;

        ActionCommand actionCommand = actionCommandDoc.getActionCommand();
        this.actionSimpleName = actionCommand.getActionControllerClazz().getSimpleName();

        // 方法名
        this.actionMethodName = StrKit.firstCharToUpperCase(actionCommand.getActionMethodName());
        // 方法注释
        this.methodComment = this.actionCommandDoc.getComment();

        CmdInfo cmdInfo = actionCommand.getCmdInfo();
        this.memberCmdName = "%s_%d_%d".formatted(actionCommand.getActionMethodName(), cmdInfo.getCmd(), cmdInfo.getSubCmd());

        // --------- 方法返回值相关 ---------
        extractedReturnInfo(actionCommand);

        // --------- 方法参数相关 ---------
        ActionCommand.ParamInfo bizParam = getBizParam(actionCommand);
        this.hasBizData = Objects.nonNull(bizParam);
        if (this.hasBizData) {
            extractedParamInfo(bizParam, actionCommandDoc);
        }
    }

    private void extractedReturnInfo(ActionCommand actionCommand) {
        // 方法返回值注释
        this.returnComment = actionCommandDoc.getMethodReturnComment();

        ActionCommand.ActionMethodReturnInfo returnInfo = actionCommand.getActionMethodReturnInfo();
        this.isVoid = returnInfo.isVoid();
        this.returnDataIsList = returnInfo.isList();

        Class<?> returnTypeClazz = returnInfo.getActualTypeArgumentClazz();
        var typeMappingRecord = typeMappingDocument.getTypeMappingRecord(returnTypeClazz);
        this.returnDataName = typeMappingRecord.getParamTypeName();
        this.returnDataTypeIsInternal = typeMappingRecord.isInternalType();
        this.resultMethodTypeName = typeMappingRecord.getResultMethodTypeName();
        this.resultMethodListTypeName = typeMappingRecord.getResultMethodListTypeName();

        this.returnDataActualTypeName = typeMappingRecord.getParamTypeName();
    }

    private void extractedParamInfo(ActionCommand.ParamInfo paramInfo, ActionCommandDoc actionCommandDoc) {
        Class<?> actualTypeArgumentClazz = paramInfo.getActualTypeArgumentClazz();
        // 方法参数类型
        var typeMappingRecord = this.typeMappingDocument.getTypeMappingRecord(actualTypeArgumentClazz);
        this.bizDataTypeIsList = paramInfo.isList();
        this.bizDataTypeIsInternal = typeMappingRecord.isInternalType();

        // sdk 方法名
        this.sdkMethodName = typeMappingRecord.getOfMethodTypeName(this.bizDataTypeIsList);

        this.bizDataType = typeMappingRecord.getParamTypeName(this.bizDataTypeIsList);
        this.bizDataName = paramInfo.getName();
        this.bizDataComment = actionCommandDoc.getMethodParamComment();

        this.actualTypeName = typeMappingRecord.getParamTypeName();
    }

    ActionCommand.ParamInfo getBizParam(ActionCommand actionCommand) {
        return actionCommand.streamParamInfo()
                // 只处理业务参数
                .filter(ActionCommand.ParamInfo::isBizData)
                .findAny().orElse(null);
    }
}
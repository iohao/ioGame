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
import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.core.exception.MsgExceptionInfo;
import com.iohao.game.common.kit.CollKit;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.expression.Expression;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 渔民小镇
 * @date 2024-06-26
 */
@Slf4j
@UtilityClass
class DocumentAnalyseKit {
    List<ActionDocument> analyseActionDocument(IoGameDocument ioGameDocument, TypeMappingDocument typeMappingDocument) {
        // 数据类型对应的映射值
        return ioGameDocument.getActionDocList().stream().map(actionDoc -> {
            // 生成 action 文件
            ActionDocument actionDocument = new ActionDocument(actionDoc, typeMappingDocument);
            actionDocument.analyse();
            return actionDocument;
        }).filter(actionDocument -> {
            // action 方法列表不为空
            List<ActionMethodDocument> actionMethodDocumentList = actionDocument.getActionMethodDocumentList();
            return !actionMethodDocumentList.isEmpty();
        }).toList();
    }

    List<ErrorCodeDocument> analyseErrorCodeDocument(Class<? extends MsgExceptionInfo> clazz) {

        var analyseJavaClassRecord = analyseJavaClass(clazz);
        if (!analyseJavaClassRecord.exists) {
            // 框架内置错误码的特殊处理。因为编译后已经没有源码了，所以无法获取框架的 ActionErrorEnum 相关源码。
            return analyseActionErrorEnumDocument(clazz);
        }

        return analyseErrorCodeDocument(analyseJavaClassRecord.javaClass());
    }

    private List<ErrorCodeDocument> analyseActionErrorEnumDocument(Class<? extends MsgExceptionInfo> clazz) {

        if (!ActionErrorEnum.class.equals(clazz)) {
            return Collections.emptyList();
        }

        return Arrays.stream(ActionErrorEnum.values()).map(code -> {
            ErrorCodeDocument errorCodeDocument = new ErrorCodeDocument();
            errorCodeDocument.setName(code.name());
            errorCodeDocument.setValue(code.getCode());
            errorCodeDocument.setDescription(code.getMsg());

            // i18n
            if (Locale.getDefault() == Locale.US) {
                errorCodeDocument.setDescription(code.name());
            }

            return errorCodeDocument;
        }).toList();
    }

    AnalyseJavaClassRecord analyseJavaClass(Class<?> clazz) {

        URL resource = clazz.getResource(clazz.getSimpleName() + ".class");
        String srcPath = ActionCommandDocKit.sourceFilePathFun.apply(resource).replace("class", "java");

        JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();

        File file = new File(srcPath);
        // 源码在此包才做处理
        boolean exists = file.exists();
        if (exists) {
            javaProjectBuilder.addSourceTree(file);
        }

        if (!exists && !ActionErrorEnum.class.equals(clazz)) {
            log.warn("无法获取 {} 相关源码", clazz);
        }

        JavaClass javaClass = javaProjectBuilder.getClassByName(clazz.getName());

        return new AnalyseJavaClassRecord(exists, javaClass);
    }

    record AnalyseJavaClassRecord(boolean exists, JavaClass javaClass) {

    }

    private final AtomicInteger gameCodeOrdinal = new AtomicInteger(0);

    private List<ErrorCodeDocument> analyseErrorCodeDocument(JavaClass javaClass) {
        return javaClass.getFields().stream().map(field -> {
            List<Expression> enumConstantArguments = field.getEnumConstantArguments();
            if (CollKit.isEmpty(enumConstantArguments)) {
                return null;
            }

            int gameCode = 0;
            if (enumConstantArguments.size() == 1) {
                gameCode = gameCodeOrdinal.getAndIncrement();
            } else if (enumConstantArguments.size() == 2) {
                Object enumValue = enumConstantArguments.getFirst().getParameterValue();
                gameCode = Integer.parseInt(enumValue.toString());
            }

            String name = field.getName();

            ErrorCodeDocument errorCodeDocument = new ErrorCodeDocument();
            errorCodeDocument.setName(name);
            errorCodeDocument.setValue(gameCode);

            String description = enumConstantArguments.getLast().getParameterValue().toString();
            description = description.substring(1, description.length() - 1);
            errorCodeDocument.setDescription(description);

            return errorCodeDocument;
        }).filter(Objects::nonNull).toList();
    }

    ActionCommand.ParamInfo getBizParam(ActionCommand actionCommand) {
        return actionCommand.streamParamInfo()
                // 只处理业务参数
                .filter(ActionCommand.ParamInfo::isBizData)
                .findAny().orElse(null);
    }
}

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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

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
            return errorCodeDocument;
        }).toList();
    }

    AnalyseJavaClassRecord analyseJavaClass(Class<?> clazz) {

        URL resource = clazz.getResource(clazz.getSimpleName() + ".class");
        String srcPath = sourceFilePathFun.apply(resource).replace("class", "java");

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

    private final Function<URL, String> sourceFilePathFun = resourceUrl -> {
        String path = resourceUrl.getPath();

        return path.contains("target/classes")
                // maven
                ? path.replace("target/classes", "src/main/java")
                // gradle
                : path.replace("build/classes", "src/main/java");
    };

    private List<ErrorCodeDocument> analyseErrorCodeDocument(JavaClass javaClass) {
        return javaClass.getFields().stream().map(field -> {
            String name = field.getName();
            List<Expression> enumConstantArguments = field.getEnumConstantArguments();
            if (CollKit.isEmpty(enumConstantArguments) || enumConstantArguments.size() != 2) {
                return null;
            }

            ErrorCodeDocument errorCodeDocument = new ErrorCodeDocument();
            errorCodeDocument.setName(name);

            Object enumValue = enumConstantArguments.getFirst().getParameterValue();
            errorCodeDocument.setValue(Integer.parseInt(enumValue.toString()));

            String description = enumConstantArguments.getLast().getParameterValue().toString();
            description = description.substring(1, description.length() - 1);
            errorCodeDocument.setDescription(description);

            return errorCodeDocument;
        }).filter(Objects::nonNull).toList();
    }
}

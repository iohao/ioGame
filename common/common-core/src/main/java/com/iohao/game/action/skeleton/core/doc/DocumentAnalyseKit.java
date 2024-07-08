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

import com.iohao.game.action.skeleton.core.exception.MsgExceptionInfo;
import com.iohao.game.common.kit.CollKit;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.expression.Expression;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author 渔民小镇
 * @date 2024-06-26
 */
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
        JavaClass javaClass = analyseJavaClass(clazz);
        return analyseErrorCodeDocument(javaClass);
    }

    JavaClass analyseJavaClass(Class<?> clazz) {
        JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();

        URL resource = clazz.getResource(clazz.getSimpleName() + ".class");
        String srcPath = sourceFilePathFun.apply(resource).replace("class", "java");

        File file = new File(srcPath);
        // 源码在此包才做处理
        if (file.exists()) {
            javaProjectBuilder.addSourceTree(file);
        }

        return javaProjectBuilder.getClassByName(clazz.getName());
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

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

import com.iohao.game.action.skeleton.annotation.ActionMethod;
import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.kit.StrKit;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 渔民小镇
 * @date 2022-01-28
 */
public final class JavaClassDocInfo {
    final JavaClass javaClass;
    Map<String, JavaMethod> javaMethodMap = new HashMap<>();

    public JavaClassDocInfo(JavaClass javaClass) {
        this.javaClass = javaClass;

        List<JavaMethod> methods = javaClass.getMethods();
        for (JavaMethod method : methods) {
            javaMethodMap.put(method.toString(), method);
        }
    }

    public ActionCommandDoc createActionCommandDoc(Method method) {
        JavaMethod javaMethod = javaMethodMap.get(method.toString());
        int subCmd = method.getAnnotation(ActionMethod.class).value();

        ActionCommandDoc actionCommandDoc = new ActionCommandDoc();
        actionCommandDoc.setSubCmd(subCmd);
        actionCommandDoc.setClassComment(this.javaClass.getComment());
        actionCommandDoc.setClassLineNumber(this.javaClass.getLineNumber());
        actionCommandDoc.setComment(javaMethod.getComment());
        actionCommandDoc.setLineNumber(javaMethod.getLineNumber());

        if (actionCommandDoc.getClassComment() == null) {
            actionCommandDoc.setClassComment("");
        }

        if (actionCommandDoc.getComment() == null) {
            actionCommandDoc.setComment("");
        }

        methodParamReturnComment(actionCommandDoc, javaMethod);

        return actionCommandDoc;
    }

    private void methodParamReturnComment(ActionCommandDoc actionCommandDoc, JavaMethod javaMethod) {
        List<DocletTag> tags = javaMethod.getTags();
        if (CollKit.isEmpty(tags)) {
            return;
        }

        for (DocletTag tag : tags) {
            String name = tag.getName();
            String value = tag.getValue();

            if (StrKit.isEmpty(value) || value.contains("flowContext")) {
                continue;
            }

            int paramIndex = value.indexOf(" ");
            if ("param".equals(name) && paramIndex != -1) {
                String trim = value.substring(paramIndex).trim();
                actionCommandDoc.setMethodParamComment(trim);
            } else if ("return".equals(name)) {
                actionCommandDoc.setMethodReturnComment(value);
            }
        }
    }

    public String getComment() {
        return this.javaClass.getComment();
    }
}

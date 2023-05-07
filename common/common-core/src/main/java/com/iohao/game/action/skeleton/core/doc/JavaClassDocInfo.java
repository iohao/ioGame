/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General  License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General  License for more details.
 *
 * You should have received a copy of the GNU General  License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.action.skeleton.core.doc;

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
public class JavaClassDocInfo {
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

        ActionCommandDoc actionCommandDoc = new ActionCommandDoc();

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

        return actionCommandDoc;
    }

    public String getComment() {
        return this.javaClass.getComment();
    }
}

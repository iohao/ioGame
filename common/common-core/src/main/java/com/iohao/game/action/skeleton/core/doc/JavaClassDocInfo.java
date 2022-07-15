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
}

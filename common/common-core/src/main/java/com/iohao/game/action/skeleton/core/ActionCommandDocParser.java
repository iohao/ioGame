/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import com.iohao.game.action.skeleton.core.doc.ActionCommandDoc;
import com.iohao.game.action.skeleton.core.doc.ActionCommandDocKit;
import com.iohao.game.action.skeleton.core.doc.JavaClassDocInfo;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * source doc 解析类
 *
 * @author 渔民小镇
 * @date 2022-12-09
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
class ActionCommandDocParser {
    final ActionCommandParser actionCommandParser;

    Map<Integer, ActionCommandDoc> actionCommandDocMap = new NonBlockingHashMap<>();

    ActionCommandDocParser(ActionCommandParser actionCommandParser, List<Class<?>> controllerList) {
        this.actionCommandParser = actionCommandParser;
        this.buildSourceDoc(controllerList);
    }

    private void buildSourceDoc(List<Class<?>> controllerList) {

        var actionCommandRegions = this.actionCommandParser.getActionCommandRegions();

        // java source
        Map<String, JavaClassDocInfo> javaClassDocInfoMap = ActionCommandDocKit.getJavaClassDocInfoMap(controllerList);

        this.actionCommandParser.getActionControllerStream(controllerList).forEach(controllerClazz -> {
            // java source
            JavaClassDocInfo javaClassDocInfo = javaClassDocInfoMap.get(controllerClazz.toString());

            // 主路由 (类上的路由)
            int cmd = controllerClazz.getAnnotation(ActionController.class).value();

            var actionCommandRegion = actionCommandRegions.getActionCommandRegion(cmd);
            actionCommandRegion.setActionControllerClazz(controllerClazz);
            actionCommandRegion.setJavaClassDocInfo(javaClassDocInfo);

            this.actionCommandParser.getMethodStream(controllerClazz).forEach(method -> {
                ActionCommandDoc actionCommandDoc = getActionCommandDoc(javaClassDocInfo, method);
                // 目标子路由 (方法上的路由)
                int subCmd = method.getAnnotation(ActionMethod.class).value();

                int cmdMerge = CmdKit.merge(cmd, subCmd);
                // 将数据保存在这里。
                actionCommandDocMap.put(cmdMerge, actionCommandDoc);
            });
        });
    }

    ActionCommandDoc getActionCommandDoc(int cmd, int subCmd) {
        int cmdMerge = CmdKit.merge(cmd, subCmd);
        return actionCommandDocMap.get(cmdMerge);
    }

    private ActionCommandDoc getActionCommandDoc(JavaClassDocInfo javaClassDocInfo, Method method) {
        if (javaClassDocInfo != null) {
            return javaClassDocInfo.createActionCommandDoc(method);
        }

        return new ActionCommandDoc();
    }
}

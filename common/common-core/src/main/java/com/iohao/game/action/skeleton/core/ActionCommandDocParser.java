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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import com.iohao.game.action.skeleton.core.doc.*;
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
final class ActionCommandDocParser {
    final ActionCommandParser actionCommandParser;

    final Map<Integer, ActionCommandDoc> actionCommandDocMap = new NonBlockingHashMap<>();

    final ActionCommandDoc emptyActionCommandDoc = new ActionCommandDoc();

    ActionCommandDocParser(ActionCommandParser actionCommandParser, List<Class<?>> controllerList, boolean parseDoc) {
        this.actionCommandParser = actionCommandParser;
        if (parseDoc) {
            this.buildSourceDoc(controllerList);
        }
    }

    private void buildSourceDoc(List<Class<?>> controllerList) {

        var actionCommandRegions = this.actionCommandParser.getActionCommandRegions();

        // java source
        Map<String, JavaClassDocInfo> javaClassDocInfoMap = ActionCommandDocKit.getJavaClassDocInfoMap(controllerList);

        this.actionCommandParser.getActionControllerStream(controllerList).parallel().forEach(controllerClazz -> {
            // java source
            JavaClassDocInfo javaClassDocInfo = javaClassDocInfoMap.get(controllerClazz.toString());

            // 主路由 (类上的路由)
            int cmd = controllerClazz.getAnnotation(ActionController.class).value();

            // 过期的方法，将来需要删除的部分
            extractedDeprecate(actionCommandRegions, controllerClazz, javaClassDocInfo, cmd);

            // action 文档
            ActionDoc actionDoc = IoGameDocumentHelper.ofActionDoc(cmd, controllerClazz);
            actionDoc.setJavaClassDocInfo(javaClassDocInfo);

            this.actionCommandParser.getMethodStream(controllerClazz).forEach(method -> {
                ActionCommandDoc actionCommandDoc = getActionCommandDoc(javaClassDocInfo, method);
                // 目标子路由 (方法上的路由)
                int subCmd = method.getAnnotation(ActionMethod.class).value();

                int cmdMerge = CmdKit.merge(cmd, subCmd);
                // 将数据保存在这里。
                actionCommandDocMap.put(cmdMerge, actionCommandDoc);
                actionDoc.addActionCommandDoc(actionCommandDoc);
            });
        });
    }

    private static void extractedDeprecate(ActionCommandRegions actionCommandRegions, Class<?> controllerClazz, JavaClassDocInfo javaClassDocInfo, int cmd) {
        var actionCommandRegion = actionCommandRegions.getActionCommandRegion(cmd);
        actionCommandRegion.setActionControllerClazz(controllerClazz);
        actionCommandRegion.setJavaClassDocInfo(javaClassDocInfo);
    }

    ActionCommandDoc getActionCommandDoc(int cmd, int subCmd) {
        int cmdMerge = CmdKit.merge(cmd, subCmd);
        return actionCommandDocMap.getOrDefault(cmdMerge, emptyActionCommandDoc);
    }

    private ActionCommandDoc getActionCommandDoc(JavaClassDocInfo javaClassDocInfo, Method method) {

        if (javaClassDocInfo != null) {
            return javaClassDocInfo.createActionCommandDoc(method);
        }

        return new ActionCommandDoc();
    }
}

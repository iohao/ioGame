/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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

import org.jctools.maps.NonBlockingHashMap;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * action 文档管理
 *
 * @author 渔民小镇
 * @date 2023-07-13
 */
public class ActionDocs {
    /**
     * action 文档
     * <pre>
     *     使用 static 避免同进程启动多个相同游戏逻辑服时，重复生成文档
     *     key : action class
     *     value: ActionDoc
     * </pre>
     */
    static final Map<Class<?>, ActionDoc> actionDocMap = new NonBlockingHashMap<>();

    /**
     * 获取 ActionDoc，如果 ActionDoc 不存在则创建
     *
     * @param cmd             主路由
     * @param controllerClazz action class
     * @return 一定不为 null
     */
    public static ActionDoc ofActionDoc(int cmd, Class<?> controllerClazz) {
        ActionDoc actionDocRegion = actionDocMap.get(controllerClazz);

        if (Objects.isNull(actionDocRegion)) {
            actionDocRegion = new ActionDoc(cmd, controllerClazz);

            actionDocRegion = actionDocMap.putIfAbsent(controllerClazz, actionDocRegion);
            if (Objects.isNull(actionDocRegion)) {
                actionDocRegion = actionDocMap.get(controllerClazz);
            }
        }

        return actionDocRegion;
    }

    static Stream<ActionDoc> stream() {
        // 先按 cmd 排序
        // 若 cmd 相同，则按 className 排序
        return actionDocMap
                .values()
                .stream()
                .sorted(
                        Comparator
                                .comparingInt(ActionDoc::getCmd)
                                .thenComparing(o -> o.getControllerClazz().getName())
                );
    }
}

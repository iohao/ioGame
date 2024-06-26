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

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 文档相关信息，如 action 相关、广播相关、错误码相关。
 *
 * @author 渔民小镇
 * @date 2024-06-25
 */
@Getter
@Setter
public final class IoGameDocument {
    /**
     * action 文档相关信息
     * <pre>
     *      key : action controller
     *      value : action 文档
     *  </pre>
     */
    Map<Class<?>, ActionDoc> actionDocMap;
    /** 错误码域 */
    ErrorCodeDocsRegion errorCodeDocsRegion;

    /** 已经解析好的广播文档 */
    BroadcastDocumentRegion broadcastDocumentRegion;
    /** 已经解析好的错误码文档 */
    List<ErrorCodeDocument> errorCodeDocumentList;

    public Stream<ActionDoc> streamActionDoc() {
        return this.actionDocMap
                .values()
                .stream()
                .sorted(Comparator
                        // 先按 cmd 排序
                        .comparingInt(ActionDoc::getCmd)
                        // 若 cmd 相同，则按 className 排序
                        .thenComparing(o -> o.getControllerClazz().getName())
                );
    }
}

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

import java.util.List;
import java.util.Map;

/**
 * 类型映射
 *
 * @author 渔民小镇
 * @date 2024-06-26
 */
public interface TypeMappingDocument {
    Map<Class<?>, TypeMappingRecord> getMap();

    default TypeMappingRecord getTypeMappingRecord(Class<?> protoTypeClazz) {
        var map = getMap();
        if (map.containsKey(protoTypeClazz)) {
            return map.get(protoTypeClazz);
        }

        String simpleName = protoTypeClazz.getSimpleName();

        return new TypeMappingRecord()
                .setInternalType(false)
                .setParamTypeName(simpleName).setListParamTypeName("List<%s>".formatted(simpleName))
                .setOfMethodTypeName("").setOfMethodListTypeName("ValueList");
    }

    default void mapping(TypeMappingRecord record, List<Class<?>> clazzList) {
        for (Class<?> clazz : clazzList) {
            this.getMap().put(clazz, record);
        }
    }
}

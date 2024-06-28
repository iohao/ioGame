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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * 类型映射记录
 *
 * @author 渔民小镇
 * @date 2024-06-26
 */
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class TypeMappingRecord {
    @Getter
    String paramTypeName;
    /** list 参数类型名 */
    String listParamTypeName;

    /** sdk 方法名 */
    String ofMethodTypeName;
    /** sdk list 参数的方法名 */
    String ofMethodListTypeName;

    /** sdk result get 方法名 */
    @Getter
    String resultMethodTypeName;
    /** sdk result get list 方法名 */
    @Getter
    String resultMethodListTypeName;

    /** 内置扩展类型 */
    @Getter
    boolean internalType = true;

    public String getParamTypeName(boolean isList) {
        return isList ? listParamTypeName : paramTypeName;
    }

    public String getOfMethodTypeName(boolean isList) {
        return isList ? ofMethodListTypeName : ofMethodTypeName;
    }
}

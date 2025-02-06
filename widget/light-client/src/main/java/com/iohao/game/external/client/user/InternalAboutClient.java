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
package com.iohao.game.external.client.user;

import com.iohao.game.action.skeleton.core.flow.parser.MethodParsers;

import java.util.List;
import java.util.Objects;

final class ParseClientRequestDataKit {
    static Object parse(Object requestData) {
        Objects.requireNonNull(requestData);

        boolean isList;
        Class<?> clazz;

        if (requestData instanceof List<?> list) {
            Objects.requireNonNull(list.getFirst());

            isList = true;
            clazz = list.getFirst().getClass();
        } else {
            isList = false;
            clazz = requestData.getClass();
        }

        var methodParser = MethodParsers.getMethodParser(clazz);
        return methodParser.parseData(isList, requestData);
    }
}

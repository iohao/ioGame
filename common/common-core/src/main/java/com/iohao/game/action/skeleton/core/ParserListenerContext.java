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

import com.iohao.game.common.kit.MoreKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;

/**
 * action 构建时的上下文
 *
 * @author 渔民小镇
 * @date 2024-04-30
 */
@Getter
@Accessors(chain = true)
@Setter(AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ParserListenerContext {
    /** 业务框架 */
    BarSkeleton barSkeleton;
    /** action controller */
    ParserActionController parserActionController = new ParserActionController();
    Map<Integer, ParserActionCommand> parserActionCommandMap = new NonBlockingHashMap<>();

    ParserActionCommand ofParserActionCommand(int subCmd) {

        ParserActionCommand parserActionCommand = this.parserActionCommandMap.get(subCmd);

        if (parserActionCommand == null) {
            return MoreKit.putIfAbsent(this.parserActionCommandMap, subCmd, new ParserActionCommand());
        }

        return parserActionCommand;
    }
}
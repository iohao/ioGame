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
package com.iohao.game.action.skeleton.core.flow;

import com.iohao.game.common.consts.CommonConst;

/**
 * action 方法参数解析器 actionCommand
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public interface ActionMethodParamParser {
    /** 方法空参数 */
    Object[] METHOD_PARAMS = CommonConst.emptyObjects;

    /**
     * 参数解析
     *
     * @param flowContext flow 上下文
     * @return 参数列表 一定不为 null
     */
    Object[] listParam(final FlowContext flowContext);
}

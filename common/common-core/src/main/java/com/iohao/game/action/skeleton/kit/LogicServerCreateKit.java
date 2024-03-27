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
package com.iohao.game.action.skeleton.kit;

import com.iohao.game.action.skeleton.core.BarSkeletonBuilder;
import com.iohao.game.action.skeleton.core.BarSkeletonBuilderParamConfig;
import com.iohao.game.action.skeleton.core.flow.ActionMethodInOut;
import com.iohao.game.action.skeleton.core.flow.internal.DebugInOut;
import lombok.experimental.UtilityClass;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2023-07-13
 */
@UtilityClass
public class LogicServerCreateKit {
    List<ActionMethodInOut> inOutList = new LinkedList<>();

    static {
        inOutList.add(new DebugInOut());
    }

    public BarSkeletonBuilder createBuilder(BarSkeletonBuilderParamConfig config) {
        // 业务框架构建器
        var builder = config.createBuilder();

        // 添加插件
        inOutList.forEach(builder::addInOut);

        return builder;
    }

    public BarSkeletonBuilder createBuilder(Class<?> actionControllerClass) {
        // 业务框架构建器 配置
        var config = new BarSkeletonBuilderParamConfig()
                // 扫描 action 类所在包
                .scanActionPackage(actionControllerClass);

        return createBuilder(config);
    }

    public void removeInOut(Class<? extends ActionMethodInOut> inoutClass) {
        Objects.requireNonNull(inoutClass);
        inOutList.removeIf(actionMethodInOut -> inoutClass.equals(actionMethodInOut.getClass()));
    }

    public void addInOut(ActionMethodInOut inOut) {
        Objects.requireNonNull(inOut);
        inOutList.add(inOut);
    }
}

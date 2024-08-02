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

import com.iohao.game.action.skeleton.core.flow.ActionMethodInOut;
import com.iohao.game.action.skeleton.core.flow.FlowContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * InOut 插件相关
 *
 * @author 渔民小镇
 * @date 2022-03-08
 */
public interface InOutManager {
    /**
     * 执行所有 inOut fuckIn 方法
     *
     * @param flowContext flowContext
     */
    void fuckIn(FlowContext flowContext);

    /**
     * 执行所有 inOut fuckOut 方法
     *
     * @param flowContext flowContext
     */
    void fuckOut(FlowContext flowContext);

    /**
     * 添加 inOut
     *
     * @param inOut inOut 插件
     */
    void addInOut(ActionMethodInOut inOut);

    /**
     * 得到插件列表
     *
     * @return ActionMethodInOut list
     */
    List<ActionMethodInOut> listInOut();

    /**
     * 通过 clazz 找到对应的 inOut 对象
     *
     * @param clazz inOut class
     * @param <T>   ActionMethodInOut
     * @return any optional
     * @see ActionMethodInOut
     */
    @SuppressWarnings("unchecked")
    default <T extends ActionMethodInOut> Optional<T> getOptional(Class<? extends T> clazz) {
        for (ActionMethodInOut inOut : this.listInOut()) {
            if (Objects.equals(inOut.getClass(), clazz)) {
                return (Optional<T>) Optional.of(inOut);
            }
        }

        return Optional.empty();
    }

    /**
     * 创建 InOutManager 对象实现。inOut 的执行顺序为 in ABC，out ABC
     *
     * @return InOutManager ABC, ABC
     */
    static InOutManager ofAbcAbc() {
        return new AbcAbcInOutManager();
    }

    /**
     * 创建 InOutManager 对象实现。inOut 的执行顺序为 in ABC，out CBA。（默认策略）
     *
     * @return InOutManager ABC, CBA
     */
    static InOutManager ofPipeline() {
        return new PipelineInOutManager();
    }
}
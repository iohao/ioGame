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
package com.iohao.game.widget.light.room.flow;

import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.common.kit.attr.AttrOption;
import com.iohao.game.common.kit.attr.AttrOptions;
import com.iohao.game.widget.light.room.Room;

import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2024-05-12
 * @since 21.8
 */
public interface GameFlowContext {
    /**
     * 获取动态成员属性
     *
     * @return 动态成员属性
     */
    AttrOptions getOptions();

    /**
     * get room
     *
     * @return room
     */
    Room getRoom();

    /**
     * get FlowContext
     *
     * @return FlowContext
     */
    FlowContext getFlowContext();

    default long getUserId() {
        return this.getFlowContext().getUserId();
    }

    /**
     * 获取选项值。
     * <p>
     * 如果选项不存在，返回默认值。
     *
     * @param option 选项值
     * @return 如果 option 不存在，则使用默认的 option 值。
     */
    default <T> T option(AttrOption<T> option) {
        T value = this.getOptions().option(option);
        if (Objects.isNull(value)) {
            value = option.defaultValue();
        }

        return value;
    }

    /**
     * 设置一个具有特定值的新选项。
     * <p>
     * 使用 null 值删除前一个设置的 {@link AttrOption}。
     *
     * @param option 选项值
     * @param value  选项值, null 用于删除前一个 {@link AttrOption}.
     * @return this
     */
    default <T> GameFlowContext option(AttrOption<T> option, T value) {
        this.getOptions().option(option, value);
        return this;
    }

    static GameFlowContext of(Room room, FlowContext flowContext) {
        return new SimpleGameFlowContext(room, flowContext);
    }
}

/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.action.skeleton.core.flow;

/**
 * 业务框架 flow 上下文 工厂，负责创建 FlowContext
 * <pre>
 *     通过这个工厂，开发者可以自定义 FlowContext 的子类，
 *     通常用于给 FlowContext 子类添加上一些自定义方法
 *
 *     FlowContext 还支持开发者自定义，具体参考
 *     https://www.yuque.com/iohao/game/zz8xiz#sLySn
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-08-20
 */
public interface FlowContextFactory {
    /**
     * 创建业务框架 flow 上下文
     *
     * @return FlowContext
     */
    FlowContext createFlowContext();
}

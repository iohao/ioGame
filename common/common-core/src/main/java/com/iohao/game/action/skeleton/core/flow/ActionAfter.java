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
package com.iohao.game.action.skeleton.core.flow;

/**
 * ActionAfter 最后的处理，通常用于将数据发送给请求端
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
public interface ActionAfter {
    /**
     * 最后执行的方法, 一般将发送到客户端的逻辑存放到这里
     * <pre>
     * netty
     *     channelContext.writeAndFlush(msg);
     * </pre>
     *
     * @param flowContext flow 上下文
     */
    void execute(FlowContext flowContext);
}

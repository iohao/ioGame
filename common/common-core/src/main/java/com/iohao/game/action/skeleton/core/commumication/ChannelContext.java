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
package com.iohao.game.action.skeleton.core.commumication;

/**
 * 通信通道接口
 * <pre>
 *     用于对 bolt AsyncContext、netty Channel 的包装，这样可以使得业务框架与网络通信框架解耦。
 *     为将来 ioGame 实现绳量级架构的使用做准备，也为将来实现一套更符合游戏的网络通信框架做预留准备。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-12-04
 */
public interface ChannelContext {
    /**
     * 发送响应给请求端
     *
     * @param responseObject 响应对象
     */
    void sendResponse(Object responseObject);
}

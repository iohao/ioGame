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
package com.iohao.game.external.core;

import com.iohao.game.bolt.broker.core.aware.AwareInject;
import com.iohao.game.common.kit.attr.AttrOptionDynamic;

/**
 * 与真实玩家连接的 ExternalCore 服务器设置
 * <pre>
 *     由于有动态属性的存在，开发者可以通过此接口做任意的扩展
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-05-05
 */
public interface ExternalCoreSetting extends AwareInject, AttrOptionDynamic {
}

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
package com.iohao.game.external.client;

import com.iohao.game.common.kit.attr.AttrOptionDynamic;
import com.iohao.game.common.kit.attr.AttrOptions;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户端的用户（玩家）
 * <pre>
 *     开发者可以通过动态属性来扩展业务，比如可以在动态属性中保存货币、战力值、血条 ...等
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-07-09
 */
@Setter
@Getter
public class ClientUser implements AttrOptionDynamic {
    final AttrOptions options = new AttrOptions();
    long userId;
    /** 昵称 */
    String nickname;
    String jwt;
}

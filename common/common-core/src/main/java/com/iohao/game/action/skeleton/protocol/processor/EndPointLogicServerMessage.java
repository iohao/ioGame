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
package com.iohao.game.action.skeleton.protocol.processor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 玩家绑定逻辑服
 *
 * @author 渔民小镇
 * @date 2022-05-28
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EndPointLogicServerMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = -281565377520818401L;
    /**
     * 绑定的逻辑服id
     * <pre>
     *     由 逻辑服的 id 转为 hash32
     *     see {@link com.iohao.game.common.kit.MurmurHash3#hash32(String)}
     * </pre>
     */
    String logicServerId;

    /** 用户 id */
    List<Long> userList;

    /**
     * 绑定类型
     * <pre>
     *     true 绑定逻辑服id
     *     false 清除绑定的逻辑服id
     * </pre>
     */
    boolean binding;
}

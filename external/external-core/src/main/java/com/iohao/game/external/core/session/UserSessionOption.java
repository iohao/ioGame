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
package com.iohao.game.external.core.session;

import com.iohao.game.common.kit.attr.AttrOption;
import com.iohao.game.external.core.config.ExternalJoinEnum;

import java.util.Set;

/**
 * UserSession 的动态属性名
 *
 * @author 渔民小镇
 * @date 2023-02-21
 */
public interface UserSessionOption {
    /** false : 没有进行身份验证 */
    AttrOption<Boolean> verifyIdentity = AttrOption.valueOf("verifyIdentity");

    /** 元信息 */
    AttrOption<byte[]> attachment = AttrOption.valueOf("attachment");

    /**
     * 玩家绑定的多个游戏逻辑服
     * <pre>
     *     所有与该游戏逻辑服相关的请求都将被分配给已绑定的游戏逻辑服处理。
     *     即使启动了多个同类型的游戏逻辑服，该请求仍将被分配给已绑定的游戏逻辑服处理。
     *
     *     see {@link com.iohao.game.common.kit.HashKit#hash32(String)}
     * </pre>
     */
    AttrOption<Set<Integer>> bindingLogicServerIdSet = AttrOption.valueOf("bindingLogicServerIdSet");
    /**
     * 玩家绑定的多个游戏逻辑服
     * <pre>
     *     数据来源于 bindingLogicServerIdSet
     *     在传输时使用基础类型数组要比 Set 性能更高
     * </pre>
     */
    AttrOption<int[]> bindingLogicServerIdArray = AttrOption.valueOf("bindingLogicServerIdArray");
    /** 连接方式 */
    AttrOption<ExternalJoinEnum> externalJoin = AttrOption.valueOf("externalJoin");
    /** 玩家真实 ip */
    AttrOption<String> realIp = AttrOption.valueOf("realIp", "");
}

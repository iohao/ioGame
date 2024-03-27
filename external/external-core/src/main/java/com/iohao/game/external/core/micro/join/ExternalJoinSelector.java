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
package com.iohao.game.external.core.micro.join;

import com.iohao.game.external.core.ExternalCoreSetting;
import com.iohao.game.external.core.config.ExternalJoinEnum;

/**
 * 游戏对外服连接方式选择器
 * <pre>
 *     连接方式：tcp、websocket、udp、kcp
 *
 *     Selector 作用是根据当前的连接方式（实现类），初始化相关实现类的属性和编解码
 *         1. getCodecPipeline：编解码相关的 Pipeline
 *         2. defaultSetting：相关连接方式的一些默认设置
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-05-29
 */
public interface ExternalJoinSelector {
    /**
     * 连接方式
     *
     * @return 连接方式
     */
    ExternalJoinEnum getExternalJoinEnum();

    /**
     * 相关连接方式的一些默认设置
     *
     * @param coreSetting coreSetting
     */
    void defaultSetting(ExternalCoreSetting coreSetting);
}
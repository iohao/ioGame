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
package com.iohao.game.bolt.broker.core.common.processor.hook;

import com.iohao.game.bolt.broker.core.client.BrokerClientBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * bolt 业务处理器的钩子管理器
 * <pre>
 *     在构建游戏逻辑服赋值
 *
 *     see {@link BrokerClientBuilder#clientProcessorHooks(ClientProcessorHooks)}
 *
 *
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-06-26
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientProcessorHooks {
    /**
     * 逻辑服业务处理钩子接口
     * <pre>
     *     通过业务框架把请求派发给指定的业务类（action）来处理
     * </pre>
     */
    RequestMessageClientProcessorHook requestMessageClientProcessorHook = new DefaultRequestMessageClientProcessorHook();
}

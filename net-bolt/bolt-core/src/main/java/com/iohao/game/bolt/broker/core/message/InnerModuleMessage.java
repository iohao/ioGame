/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General  License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General  License for more details.
 *
 * You should have received a copy of the GNU General  License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.bolt.broker.core.message;

import com.iohao.game.action.skeleton.protocol.RequestMessage;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 模块之间的访问
 * <pre>
 *     如： 模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据
 *
 *     如果不需要返回值的，see {@link InnerModuleVoidMessage}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
@Data
public class InnerModuleMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = -5352732154154036339L;
    RequestMessage requestMessage;
}

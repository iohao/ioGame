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
package com.iohao.game.bolt.broker.client.processor;

import com.alipay.remoting.AsyncContext;
import com.iohao.game.action.skeleton.core.commumication.ChannelContext;
import lombok.NonNull;

/**
 * @author 渔民小镇
 * @date 2022-12-04
 */
public record BoltChannelContext(@NonNull AsyncContext asyncContext) implements ChannelContext {
    @Override
    public void sendResponse(Object responseObject) {
        this.asyncContext.sendResponse(responseObject);
    }
}

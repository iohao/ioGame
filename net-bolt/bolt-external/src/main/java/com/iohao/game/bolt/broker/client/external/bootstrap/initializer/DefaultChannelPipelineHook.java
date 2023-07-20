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
package com.iohao.game.bolt.broker.client.external.bootstrap.initializer;

import com.iohao.game.bolt.broker.client.external.bootstrap.handler.AccessAuthenticationHandler;
import com.iohao.game.bolt.broker.client.external.bootstrap.handler.RequestBrokerHandler;
import com.iohao.game.bolt.broker.client.external.bootstrap.handler.UserSessionHandler;
import io.netty.channel.ChannelPipeline;

/**
 * @author 渔民小镇
 * @date 2022-12-05
 */
public class DefaultChannelPipelineHook implements ChannelPipelineHook {

    @Override
    public void initChannelPipeline(ChannelPipeline pipeline) {
        /*
         * 这是一个默认的 netty 编排业务实现，
         * 通常情况下，这样的编排方式也会更加清晰，
         * 因为代码统一在这里做编排了。
         *
         * 开发者在自定义业务编排时，可以通过这个钩子接口，
         * 比如添加一个 SSL 到 pipeline addFirst 中。
         *
         * 注意事项：
         * 在调用 hook 前，会经过 ExternalChannelInitializerCallback.initChannelPipeline(SocketChannel) 。
         * ExternalChannelInitializerCallback 接口的实现类有
         *     1.ExternalChannelInitializerCallbackWebsocket
         *     2.ExternalChannelInitializerCallbackTcp
         * 这些实现类中，会给 ChannelPipeline 添加上一些默认的处理器，通常是编解码相关的。
         *
         * 当前默认的钩子实现类 DefaultChannelPipelineHook 只是一个样例，提供参考
         */

        // 管理 UserSession 的 Handler
        pipeline.addLast("UserSessionHandler", new UserSessionHandler());

        // 路由访问验证 的 Handler
        pipeline.addLast("AccessAuthenticationHandler", new AccessAuthenticationHandler());

        // 负责把游戏端的请求转发给 Broker（游戏网关）的 Handler
        pipeline.addLast("RequestBrokerHandler", new RequestBrokerHandler());

        /*
         * UserSessionHandler、AccessAuthenticationHandler、RequestBrokerHandler
         * 上面添加了三个 Handler 分别处理 UserSession、路由权限、转发请求到 Broker。
         *
         * 实际上 ExternalBizHandler 是包含了上述三个 Handler 的功能集合的，
         * 拆分出来是为了更符合单一职责原则。
         */
    }
}

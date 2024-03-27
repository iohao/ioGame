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
package com.iohao.game.external.core.netty.micro.join;

import com.iohao.game.common.kit.PresentKit;
import com.iohao.game.external.core.ExternalCoreSetting;
import com.iohao.game.external.core.config.ExternalJoinEnum;
import com.iohao.game.external.core.micro.MicroBootstrapFlow;
import com.iohao.game.external.core.netty.DefaultExternalCoreSetting;
import com.iohao.game.external.core.netty.micro.WebSocketMicroBootstrapFlow;

/**
 * Websocket 相关
 *
 * @author 渔民小镇
 * @date 2023-05-29
 */
public final class WebSocketExternalJoinSelector extends SocketExternalJoinSelector {
    @Override
    public ExternalJoinEnum getExternalJoinEnum() {
        return ExternalJoinEnum.WEBSOCKET;
    }

    @Override
    public void defaultSetting(ExternalCoreSetting coreSetting) {
        super.defaultSetting(coreSetting);

        DefaultExternalCoreSetting setting = (DefaultExternalCoreSetting) coreSetting;
        // MicroBootstrapFlow 启动流程；如果开发者没有手动赋值，则根据当前连接方式生成
        MicroBootstrapFlow<?> microBootstrapFlow = setting.getMicroBootstrapFlow();
        PresentKit.ifNull(microBootstrapFlow, () -> setting.setMicroBootstrapFlow(new WebSocketMicroBootstrapFlow()));
    }
}

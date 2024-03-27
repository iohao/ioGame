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
package com.iohao.game.bolt.broker.server.kit;

import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.common.kit.ArrayKit;
import com.iohao.game.core.common.cmd.CmdRegions;
import lombok.experimental.UtilityClass;

/**
 * @author 渔民小镇
 * @date 2023-06-06
 */
@UtilityClass
public class EndPointClientIdKit {
    public void endPointClientId(HeadMetadata headMetadata, CmdRegions cmdRegions) {

        // 玩家绑定的游戏逻辑服列表
        int[] bindingLogicServerIds = headMetadata.getBindingLogicServerIds();
        if (ArrayKit.isEmpty(bindingLogicServerIds)) {
            return;
        }

        int cmdMerge = headMetadata.getCmdMerge();
        int pointLogicServerId = cmdRegions.endPointLogicServerId(cmdMerge, bindingLogicServerIds);
        headMetadata.setEndPointClientId(pointLogicServerId);
    }
}

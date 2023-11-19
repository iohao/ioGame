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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.action.skeleton.protocol.SyncRequestMessage;
import lombok.experimental.UtilityClass;

import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2022-06-07
 */
@UtilityClass
public class BarMessageKit {
    public RequestMessage createRequestMessage(CmdInfo cmdInfo) {
        return createRequestMessage(cmdInfo, null);
    }

    public RequestMessage createRequestMessage(CmdInfo cmdInfo, Object data) {

        RequestMessage requestMessage = new RequestMessage();

        employ(requestMessage, cmdInfo, data);

        return requestMessage;
    }

    public void employ(RequestMessage requestMessage, CmdInfo cmdInfo, Object data) {

        requestMessage.setHeadMetadata(new HeadMetadata().setCmdInfo(cmdInfo));

        if (Objects.nonNull(data)) {
            requestMessage.setData(data);
        }
    }

    /**
     * 将 RequestMessage 转为 SyncRequestMessage
     *
     * @param requestMessage RequestMessage
     * @return SyncRequestMessage
     */
    public SyncRequestMessage convertSyncRequestMessage(RequestMessage requestMessage) {
        SyncRequestMessage syncRequestMessage;

        if (requestMessage instanceof SyncRequestMessage theSyncRequestMessage) {
            syncRequestMessage = theSyncRequestMessage;
        } else {
            syncRequestMessage = new SyncRequestMessage();
            requestMessage.copyTo(syncRequestMessage);
        }

        return syncRequestMessage;
    }

    /**
     * 创建响应对象
     *
     * @param cmdInfo 路由地址
     * @return ResponseMessage
     */
    public ResponseMessage createResponseMessage(CmdInfo cmdInfo) {

        ResponseMessage responseMessage = new ResponseMessage();
        // 元信息 路由地址
        responseMessage.setHeadMetadata(new HeadMetadata().setCmdInfo(cmdInfo));

        return responseMessage;
    }

    /**
     * 创建响应对象
     *
     * @param cmdInfo 路由地址
     * @param bizData 业务数据
     * @return ResponseMessage
     */
    public ResponseMessage createResponseMessage(CmdInfo cmdInfo, Object bizData) {

        Objects.requireNonNull(bizData);

        ResponseMessage responseMessage = createResponseMessage(cmdInfo);
        // 业务数据
        responseMessage.setData(bizData);

        return responseMessage;
    }
}

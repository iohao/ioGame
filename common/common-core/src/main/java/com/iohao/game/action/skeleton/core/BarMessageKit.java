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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import lombok.experimental.UtilityClass;

import java.util.Objects;

/**
 * 创建 RequestMessage，ResponseMessage 相关内部消息的工具类
 *
 * @author 渔民小镇
 * @date 2022-06-07
 * @see RequestMessage
 * @see ResponseMessage
 */
@UtilityClass
public class BarMessageKit {
    public RequestMessage createRequestMessage(CmdInfo cmdInfo) {
        return createRequestMessage(cmdInfo, null);
    }

    /**
     * 创建 RequestMessage
     *
     * @param cmdInfo 路由
     * @param data    业务数据
     * @return RequestMessage
     */
    public RequestMessage createRequestMessage(CmdInfo cmdInfo, Object data) {

        RequestMessage requestMessage = new RequestMessage();

        employ(requestMessage, cmdInfo, data);

        return requestMessage;
    }

    /**
     * 将路由、业务数据设置到 RequestMessage 中
     *
     * @param requestMessage RequestMessage
     * @param cmdInfo        路由
     * @param data           业务数据
     */
    public void employ(RequestMessage requestMessage, CmdInfo cmdInfo, Object data) {

        var headMetadata = new HeadMetadata()
                .setCmdInfo(cmdInfo)
                // 请求命令类型: 0 心跳，1 业务; see ExternalMessageCmdCode
                .setCmdCode(1);

        requestMessage.setHeadMetadata(headMetadata);

        if (Objects.nonNull(data)) {
            requestMessage.setData(data);
        }
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

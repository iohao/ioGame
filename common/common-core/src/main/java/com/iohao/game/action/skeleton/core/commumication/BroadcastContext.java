/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.action.skeleton.core.commumication;

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;

import java.util.Collection;

/**
 * 广播通讯上下文
 *
 * @author 渔民小镇
 * @date 2022-05-18
 */
public interface BroadcastContext {
    /**
     * 广播消息给指定用户列表
     *
     * @param responseMessage 消息
     * @param userIdList      指定用户列表 (如果为 null 或 empty 就不会触发)
     */
    void broadcast(ResponseMessage responseMessage, Collection<Long> userIdList);

    /**
     * 广播消息给单个用户
     *
     * @param responseMessage 消息
     * @param userId          userId
     */
    void broadcast(ResponseMessage responseMessage, long userId);

    /**
     * 全服广播
     *
     * @param responseMessage 消息
     */
    void broadcast(ResponseMessage responseMessage);

    /**
     * 全服广播
     *
     * @param cmdInfo 广播到此路由
     * @param bizData 业务数据
     */
    default void broadcast(CmdInfo cmdInfo, Object bizData) {
        ResponseMessage responseMessage = this.createResponseMessage(cmdInfo, bizData);
        this.broadcast(responseMessage);
    }

    /**
     * 广播消息给指定用户列表
     *
     * @param cmdInfo    广播到此路由
     * @param bizData    业务数据
     * @param userIdList 指定用户列表
     */
    default void broadcast(CmdInfo cmdInfo, Object bizData, Collection<Long> userIdList) {
        ResponseMessage responseMessage = this.createResponseMessage(cmdInfo, bizData);
        this.broadcast(responseMessage, userIdList);
    }

    /**
     * 广播消息给单个用户
     *
     * @param cmdInfo 广播到此路由
     * @param bizData 业务数据
     * @param userId  userId
     */
    default void broadcast(CmdInfo cmdInfo, Object bizData, long userId) {
        ResponseMessage responseMessage = this.createResponseMessage(cmdInfo, bizData);
        this.broadcast(responseMessage, userId);
    }

    /**
     * 创建响应对象
     *
     * @param cmdInfo 路由地址
     * @param bizData 业务数据
     * @return ResponseMessage
     */
    private ResponseMessage createResponseMessage(CmdInfo cmdInfo, Object bizData) {
        // 元信息
        HeadMetadata headMetadata = new HeadMetadata();
        headMetadata.setCmdInfo(cmdInfo);

        return (ResponseMessage) new ResponseMessage()
                .setHeadMetadata(headMetadata)
                .setData(bizData);
    }
}

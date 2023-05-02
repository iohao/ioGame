/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.bolt.broker.client.external.bootstrap.message;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.action.skeleton.core.CmdKit;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 对外服数据协议
 *
 * @author 渔民小镇
 * @date 2022-01-10
 */
@Data
@ProtobufClass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExternalMessage {
    /** 请求命令类型: 0 心跳，1 业务 */
    @Protobuf(fieldType = FieldType.INT32, order = 1)
    int cmdCode;
    /** 协议开关，用于一些协议级别的开关控制，比如 安全加密校验等。 : 0 不校验 */
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    int protocolSwitch;
    /** 业务路由（高16为主, 低16为子） */
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    int cmdMerge;

    /**
     * 响应码。
     * <pre>
     *     从字段精简的角度，我们不可能每次响应都带上完整的异常信息给客户端排查问题，
     *     因此，我们会定义一些响应码，通过编号进行网络传输，方便客户端定位问题。
     *
     *     0:成功
     *     !=0: 表示有错误
     * </pre>
     */
    @Protobuf(fieldType = FieldType.SINT32, order = 4)
    int responseStatus;
    /** 验证信息（错误消息、异常消息） */
    @Protobuf(fieldType = FieldType.STRING, order = 5)
    String validMsg;
    @Protobuf(fieldType = FieldType.BYTES, order = 6)
    byte[] data;
    /** 消息标记号；由前端请求时设置，服务器响应时会携带上 */
    @Protobuf(fieldType = FieldType.INT32, order = 7)
    int msgId;

    /**
     * 业务数据
     *
     * @param data 业务数据
     */
    public void setData(byte[] data) {
        if (data != null) {
            this.data = data;
        }
    }

    /**
     * 业务路由
     *
     * @param cmd    主路由
     * @param subCmd 子路由
     */
    public void setCmdMerge(int cmd, int subCmd) {
        this.cmdMerge = CmdKit.merge(cmd, subCmd);
    }
}

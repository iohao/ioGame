/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
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

import com.baidu.bjf.remoting.protobuf.annotation.EnableZigZap;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.action.skeleton.core.CmdKit;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 对外数据的协议
 *
 * @author 渔民小镇
 * @date 2022-01-10
 */
@Data
@EnableZigZap
@ProtobufClass(description = "对外数据协议")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExternalMessage {
    /** 请求命令类型: 0 心跳，1 业务 */
    @Protobuf(description = "请求命令类型: 0 心跳，1 业务")
    int cmdCode;
    /** 协议开关，用于一些协议级别的开关控制，比如 安全加密校验等。 : 0 不校验 */
    @Protobuf(description = "协议开关，用于一些协议级别的开关控制，比如 安全加密校验等。 : 0 不校验")
    int protocolSwitch;
    /** 业务路由（高16为主, 低16为子） */
    @Protobuf(description = "业务路由（高16为主, 低16为子）")
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
    @Protobuf(description = "响应码: 0:成功, 其他表示有错误")
    int responseStatus;
    /** 验证信息 */
    @Protobuf(description = "验证信息: 当 responseStatus == -1001 时， 会有值")
    String validMsg;
    @Protobuf(description = "业务请求数据")
    byte[] data;

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

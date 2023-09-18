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
package com.iohao.game.external.core.message;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.action.skeleton.core.CmdKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;

/**
 * 游戏对外服协议
 * <pre>
 *     参考 <a href="https://www.yuque.com/iohao/game/xeokui">游戏对外服的协议 - 文档</a>
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-02-21
 */
@Getter
@Setter
@ProtobufClass
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ExternalMessage {
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
    /** 业务数据 */
    @Protobuf(fieldType = FieldType.BYTES, order = 6)
    byte[] data;
    /** 消息标记号；由前端请求时设置，服务器响应时会携带上 */
    @Protobuf(fieldType = FieldType.INT32, order = 7)
    int msgId;
    /** 预留 */
    @Ignore
    transient Object other;
    /**
     * 实验性：预留给开发者的字段
     * <pre>
     *     具备透传，服务器响应时会携带上
     *     需要注意的是，目前该字段是不会序列化到客户端的
     * </pre>
     */
    @Ignore
    transient byte[] customData;

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

    @SuppressWarnings("unchecked")
    public <T> T getOther() {
        return (T) other;
    }

    @Override
    public String toString() {
        return "ExternalMessage{" +
                "cmdCode=" + cmdCode +
                ", protocolSwitch=" + protocolSwitch +
                ", cmdMerge=" + cmdMerge +
                ", responseStatus=" + responseStatus +
                ", validMsg='" + validMsg + '\'' +
                ", msgId=" + msgId +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}

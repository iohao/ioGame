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
package com.iohao.game.external.client.command;

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.common.kit.StrKit;
import com.iohao.game.external.client.kit.ClientKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

/**
 * 模拟命令
 * example:
 * <pre>{@code
 *         ofCommand(DemoCmd.here).setTitle("here").setRequestData(() -> {
 *             YourMsg msg = ...
 *             return msg;
 *         }).callback(result -> {
 *              HelloReq value = result.getValue(HelloReq.class);
 *              log.info("value : {}", value);
 *          });
 *
 *         ofCommand(DemoCmd.list).setTitle("list").callback(result -> {
 *             // 得到 list 数据
 *             List<HelloReq> list = result.listValue(HelloReq.class);
 *             log.info("list : {}", list);
 *         });
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-07-08
 */
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InputCommand {

    final String inputName;
    final CmdInfo cmdInfo;
    /**
     * 模拟请求命令的描述
     * <pre>
     *     请使用 {@code setTitle(String}} 代替
     * </pre>
     */
    @Deprecated
    String description;
    /** 模拟请求命令的描述 */
    String title = "... ...";
    /** 描述的前缀 */
    String cmdName = "";
    /**
     * 动态请求参数
     * <pre>
     *     如果对象存在，则会使用 createRequestData 方法的返回值来代替 this.requestData 属性
     * </pre>
     * <pre>
     *     请使用 requestData 代替
     * </pre>
     */
    @Deprecated
    InputRequestData inputRequestData;

    /** 请求参数 */
    RequestDataDelegate requestData;
    /** 回调接口 */
    @Setter(AccessLevel.PRIVATE)
    CallbackDelegate callback;
    /**
     * 回调业务数据的类型
     * <pre>
     *     如果配置了，会根据此类型来解析服务器返回的业务数据
     *
     *     方法已经过期，无需代替品
     * </pre>
     */
    @Deprecated
    @Setter(AccessLevel.PRIVATE)
    Class<?> responseClass;

    public InputCommand(CmdInfo cmdInfo) {
        this.inputName = ClientKit.toInputName(cmdInfo);
        this.cmdInfo = cmdInfo;
    }

    /**
     * 模拟请求命令的描述
     * <pre>
     *     请使用 {@code setTitle(String}} 代替
     * </pre>
     */
    @Deprecated
    public InputCommand setDescription(String description) {
        this.title = description;
        this.description = description;
        return this;
    }

    /**
     * <pre>
     *     请使用 {@code  this.setRequestData(RequestDataDelegate)} 代替
     * </pre>
     *
     * @param data v
     * @return m
     */
    @Deprecated
    public InputCommand setRequestData(Object data) {
        if (data instanceof RequestDataDelegate theRequestData) {
            this.requestData = theRequestData;
        } else {
            this.requestData = () -> data;
        }

        return this;
    }

    /**
     * <pre>
     *     请使用 {@code  this.setRequestData(RequestDataDelegate)} 代替
     * </pre>
     *
     * <pre>{@code
     * ofCommand(cmd)
     *         .setTitle("yourTitle")
     *         .setRequestData(() -> {
     *             YourMsg msg = ...
     *
     *             return msg;
     *         });
     * }
     *
     * </pre>
     *
     * @param inputRequestData inputRequestData
     * @return m
     */
    @Deprecated
    public InputCommand setInputRequestData(InputRequestData inputRequestData) {
        this.inputRequestData = inputRequestData;
        this.requestData = inputRequestData;
        return this;
    }

    public InputCommand setRequestData(RequestDataDelegate requestData) {
        this.requestData = requestData;
        return this;
    }

    /**
     * <pre>
     *     请使用 {@code this.callback(CallbackDelegate)} 代替
     * </pre>
     * example:
     * <pre>{@code
     *         ofCommand(DemoCmd.here).callback(result -> {
     *             HelloReq value = result.getValue(HelloReq.class);
     *             log.info("value : {}", value);
     *         }).setTitle("here").setData(helloReq);
     *
     *         ofCommand(DemoCmd.list).callback(result -> {
     *             // 得到 list 数据
     *             List<HelloReq> list = result.listValue(HelloReq.class);
     *             log.info("list : {}", list);
     *         }).setTitle("list");
     *
     * }
     * </pre>
     *
     * @param responseClass r
     * @param callback      c
     * @return m
     */
    @Deprecated
    public InputCommand callback(Class<?> responseClass, CallbackDelegate callback) {
        this.callback = callback;

        if (Objects.nonNull(responseClass)) {
            this.responseClass = responseClass;
        }

        return this;
    }

    public InputCommand callback(CallbackDelegate callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public String toString() {
        if (StrKit.isEmpty(cmdName)) {
            var format = "%s    :    %s";
            return String.format(format, inputName, title);
        }

        var format = "%s    :    [%s] - %s";
        return String.format(format, inputName, cmdName, title);
    }
}

/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.external.client.input;

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.external.client.kit.InputCommandKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

/**
 * 输入类的命令
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

    String description = "... ...";

    /** 默认的请求参数 */
    Object requestData;
    /**
     * 动态请求参数
     * <pre>
     *     如果对象存在，则会使用 createRequestData 方法的返回值来代替 this.requestData 属性
     * </pre>
     */
    InputRequestData inputRequestData;

    /** 回调接口 */
    @Setter(AccessLevel.PRIVATE)
    InputCallback callback;
    /**
     * 回调业务数据的类型
     * <pre>
     *     如果配置了，会根据此类型来解析服务器返回的业务数据
     * </pre>
     */
    Class<?> responseClass;

    public InputCommand(CmdInfo cmdInfo) {
        this.inputName = InputCommandKit.toInputName(cmdInfo);
        this.cmdInfo = cmdInfo;
    }

    public InputCommand callback(Class<?> responseClass, InputCallback callback) {
        this.callback = callback;
        if (Objects.nonNull(responseClass)) {
            this.responseClass = responseClass;
        }

        return this;
    }

    public Object getRequestData() {

        if (Objects.nonNull(inputRequestData)) {
            return inputRequestData.createRequestData();
        }

        return requestData;
    }


    @Override
    public String toString() {
        return inputName + "    :    " + description;
    }
}

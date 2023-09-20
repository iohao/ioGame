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
package com.iohao.game.external.client.kit;

import lombok.experimental.UtilityClass;

/**
 * 模拟客户端相关的配置
 *
 * @author 渔民小镇
 * @date 2023-07-15
 */
@UtilityClass
public class ClientUserConfigs {
    /**
     * 关闭控制台输入配置
     * <pre>
     *     在压测时建议关闭，也就是将该属性设置为 true。
     *
     *     当开启关闭控制台输入配置时，也就是将该属性设置为 true 时，控制台输入相关的将失效。
     * </pre>
     */
    public boolean closeScanner;

    /** true 开启广播监听触发日志 */
    public boolean openLogListenBroadcast = true;

    /** true 开启 client action 回调日志 */
    @Deprecated
    public boolean openLogAction = true;

    /** true 开启客户端向服务器发送请求的日志 */
    public boolean openLogRequestCommand = true;

    /** true 开启请求回调的日志 */
    public boolean openLogRequestCallback = true;

    /**
     * true 表示不能存在相同的模拟命令
     * <pre>
     *     默认为 false，不做任何检测
     * </pre>
     */
    public boolean uniqueInputCommand;

    /** true 开启心跳回调的日志 */
    public boolean openLogIdle = false;

    /**
     * 关闭模拟请求相关日志
     */
    public void closeLog() {
        openLogListenBroadcast = false;
        openLogAction = false;
        openLogRequestCommand = false;
        openLogRequestCallback = false;
    }
}

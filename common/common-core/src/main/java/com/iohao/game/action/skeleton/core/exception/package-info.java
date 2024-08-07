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
/**
 * 业务框架 - 系统异常全局统一处理，<a href="https://www.yuque.com/iohao/game/avlo99">断言 + 异常机制 = 清晰简洁的代码</a>
 * <p>
 * 使用示例
 * <pre>{@code
 * // 自定义错误码
 * @Getter
 * public enum GameCodeEnum implements MsgExceptionInfo {
 *     levelMax(202,"等级超出"),
 *     ;
 *     // 消息码
 *     final int code;
 *     // 消息
 *     final String msg;
 *
 *     GameCodeEnum(int code, String msg) {
 *         this.code = code;
 *         this.msg = msg;
 *     }
 * }
 *
 * // 使用
 * @ActionController(1)
 * public class DemoAction {
 *     @ActionMethod(1)
 *     public void here(HelloReq helloReq) {
 *         // 断言必须是 true, 否则抛出异常
 *         GameCodeEnum.levelMax.assertTrue(helloReq.level > 10);
 *     }
 * }
 *
 * }</pre>
 *
 * @author 渔民小镇
 * @date 2022-01-14
 */
package com.iohao.game.action.skeleton.core.exception;
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
 * 业务框架 - action 构建时的监听器，开发者可以利用该接口观察 action 构建过程，或者做一些额外的扩展。
 * <p>
 * for example
 * <pre>{@code // 简单打印
 * public final class YourActionParserListener implements ActionParserListener {
 *     @Override
 *     public void onActionCommand(ActionParserContext context) {
 *         ActionCommand actionCommand = context.getActionCommand();
 *         log.info(actionCommand);
 *     }
 * }
 *
 * void test() {
 *     BarSkeletonBuilder builder = ...;
 *     builder.addActionParserListener(new YourActionParserListener());
 * }
 * }</pre>
 *
 * @author 渔民小镇
 * @date 2024-08-05
 * @see com.iohao.game.action.skeleton.core.action.parser.ProtobufActionParserListener
 */
package com.iohao.game.action.skeleton.core.action.parser;
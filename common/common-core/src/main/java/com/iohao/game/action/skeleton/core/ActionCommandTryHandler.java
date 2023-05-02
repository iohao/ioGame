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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import org.slf4j.Logger;

/**
 * @author 渔民小镇
 * @date 2022-12-28
 */
final class ActionCommandTryHandler extends ActionCommandHandler {
    static final Logger log = IoGameLoggerFactory.getLoggerCommonStdout();

    @Override
    public boolean handler(final FlowContext flowContext) {
        /*
         * 业务框架默认是在 bolt 线程运行，如果有异常会被 bolt 捕获到日志文件中，
         * https://www.yuque.com/iohao/game/derl0laiu2v0k104#likQv
         *
         * 由于没有打印到控制台，开发者如果对 bolt 不熟悉，是不会看日志文件，或者说不知道有该日志文件。
         * 为了避免这种情况，业务框架先做捕获并打印，在向上抛 ex
         *
         */

        try {
            return super.handler(flowContext);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}

/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
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

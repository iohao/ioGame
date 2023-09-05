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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.FlowContextKit;
import lombok.extern.slf4j.Slf4j;

/**
 * 该handler用于执行 {@link ActionCommand} 对象
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
@Slf4j
class ActionCommandHandler implements Handler {

    @Override
    public boolean handler(final FlowContext flowContext) {

        try {
            // 设置 flowContext 的一些属性
            this.settingFlowContext(flowContext);
            // actionCommand 命令流程执行器
            DefaultActionCommandFlowExecute.me().execute(flowContext);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return false;
        }

        return true;
    }

    protected void settingFlowContext(FlowContext flowContext) {
        /*
         * 做一些兼容，这部分逻辑在 RequestMessageClientProcessor 已经设置过一次，
         * 这里做兼容的目的有两个：
         * 1 防止开发者将 RequestMessageClientProcessor 移除，并做了相关的自定义。
         * 2 业务框架可以单独做测试
         */
        FlowContextKit.employ(flowContext);

        // 业务框架
        BarSkeleton barSkeleton = flowContext.getBarSkeleton();

        // 参数解析器
        var paramParser = barSkeleton.getActionMethodParamParser();
        // 得到业务方法的参数列表，并验证
        var params = paramParser.listParam(flowContext);
        // 业务方法参数 save to flowContext
        flowContext.setMethodParams(params);
    }
}

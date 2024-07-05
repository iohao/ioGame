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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.core.flow.*;
import com.iohao.game.action.skeleton.core.runner.Runners;
import com.iohao.game.common.kit.attr.AttrOptionDynamic;
import com.iohao.game.common.kit.attr.AttrOptions;
import com.iohao.game.common.kit.concurrent.executor.ExecutorRegion;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
 * 整个核心的骨架.积木骷髅 (业务框架)
 * <pre>
 *     ta可以处理所有来访者 {@link Handler}, 你可以为ta添加多种处理链,直到你满意为止.
 *     ta可以拿起笔和本子 {@link ActionMethodInOut}, 记录所看到的东西.
 *     处理完后你会让ta如何收尾 {@link ActionAfter}
 *
 *     发挥你的想象力
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-12
 * @see SkeletonAttr 业务框架动态属性
 */
@Getter
@Accessors(chain = true)
@Setter(AccessLevel.PACKAGE)
public final class BarSkeleton implements AttrOptionDynamic {
    final AttrOptions options = new AttrOptions();
    /** handler array */
    final Handler[] handlers;
    Runners runners;
    /** 命令域 管理器 */
    ActionCommandRegions actionCommandRegions = new ActionCommandRegions();
    /** InOut 插件相关 */
    InOutManager inOutManager;
    /** action 对象创建工厂 */
    ActionFactoryBean<Object> actionFactoryBean;
    /** InvokeActionMethod */
    ActionMethodInvoke actionMethodInvoke;
    /** 方法参数解析器 */
    ActionMethodParamParser actionMethodParamParser;
    /** 异常处理 */
    ActionMethodExceptionProcess actionMethodExceptionProcess;
    /** 结果包装器 */
    ActionMethodResultWrap actionMethodResultWrap;
    /** action 执行完后，最后需要做的事。 一般用于将数据发送到 Broker（游戏网关） */
    ActionAfter actionAfter;
    /** 响应对象的创建 */
    ResponseMessageCreate responseMessageCreate;
    /** 业务框架 flow 上下文 工厂 */
    FlowContextFactory flowContextFactory;
    /** 与业务框架所关联的线程执行器管理域 */
    ExecutorRegion executorRegion;

    BarSkeleton(Handler[] handlers) {
        this.handlers = handlers;
    }

    public static BarSkeletonBuilder newBuilder() {
        return new BarSkeletonBuilder();
    }

    /**
     * 业务框架处理入口
     *
     * @param flowContext flowContext
     */
    public void handle(final FlowContext flowContext) {
        // 将业务框架设置到 FlowContext 中
        flowContext.setBarSkeleton(this);

        /*
         * 多次访问的变量，保存到局部变量，可以提升性能。
         * 把成员变量的访问变为局部变量的访问 。 通过栈帧访问（线程栈），不用每次从堆中得到成员变量
         *
         * 因为这段代码访问频繁，才这样做。常规下不需要这么做
         * 可以参考 HashMap 的 putVal 方法相关
         */
        var handlers = this.handlers;

        if (handlers.length == 1) {
            handlers[0].handler(flowContext);
            return;
        }

        for (Handler theHandler : handlers) {
            if (!theHandler.handler(flowContext)) {
                return;
            }
        }
    }
}

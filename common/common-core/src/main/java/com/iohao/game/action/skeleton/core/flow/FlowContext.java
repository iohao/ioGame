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
package com.iohao.game.action.skeleton.core.flow;

import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.core.flow.attr.FlowOption;
import com.iohao.game.action.skeleton.core.flow.attr.FlowOptionDynamic;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 业务框架 flow 上下文
 * <pre>
 *     生命周期存在于这一次的 flow 过程
 *
 *     实现了类型明确的动态属性接口 {@link FlowOptionDynamic} ，实现类只需要实现 getOptions 方法就能具有动态属性的功能。
 *     动态属性可以更方便的为 FlowContext 实现属性的扩展，以方便开发者。
 *
 *     扩展属性接口 {@link FlowAttr}
 *
 *     FlowContext 还支持开发者自定义，具体参考
 *     <a href="https://www.yuque.com/iohao/game/zz8xiz#sLySn">文档 - FlowContext</a>
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-21
 * @see FlowAttr FlowContext 动态属性
 */
@Setter
@Getter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlowContext implements SimpleContext {
    /** 动态属性 */
    final Map<FlowOption<?>, Object> options = new HashMap<>();
    /** 业务框架 */
    BarSkeleton barSkeleton;
    /** command */
    ActionCommand actionCommand;
    /** 控制器类对象 */
    Object actionController;
    /** 请求对象 */
    RequestMessage request;
    /** 响应对象 */
    ResponseMessage response;
    /** 业务方法参数 */
    Object[] methodParams;
    /** 业务方法的返回值 */
    Object methodResult;
    /** true 业务方法有异常 */
    boolean error;
    /** true 执行 ActionAfter 接口 {@link ActionAfter} */
    boolean executeActionAfter = true;
    /**
     * 记录 InOut 插件的开始时间
     * <pre>
     *     一般在 InOut 插件 fuckIn 方法中调用
     *
     *     由于时间记录会比较常用，所以有必要放到该类中
     * </pre>
     */
    @Setter(AccessLevel.PRIVATE)
    long inOutStartTime;

    /**
     * InOut 执行完成后所消耗的时间
     * <pre>
     *     一般在 InOut 插件 fuckOut 方法中调用
     *
     *     消耗时间 = System.currentTimeMillis - inOutStartTime
     * </pre>
     */
    @Setter(AccessLevel.PRIVATE)
    long inOutTime;

    /**
     * 设置响应结果
     *
     * @param methodResult 响应结果
     * @return this
     */
    public FlowContext setMethodResult(Object methodResult) {

        if (Objects.nonNull(methodResult)) {
            this.methodResult = methodResult;
        }

        return this;
    }

    @Override
    public HeadMetadata getHeadMetadata() {
        return this.request.getHeadMetadata();
    }

    /**
     * 开始时间记录，用于 InOut 插件 fuckIn 方法的时间记录
     * <pre>
     *     记录 InOut 插件的开始时间
     *
     *     由于时间记录会比较常用，所以有必要放到该类中
     * </pre>
     */
    public void inOutStartTime() {
        if (this.inOutStartTime == 0) {
            this.inOutStartTime = System.currentTimeMillis();
        }
    }

    /**
     * InOut 执行完成后所消耗的时间
     * <pre>
     *     在此之前，确保调用了 {@code this.inOutStartTime()} 方法
     * </pre>
     *
     * @return 消耗时间 = System.currentTimeMillis - inOutStartTime
     */
    public long getInOutTime() {

        if (this.inOutStartTime == 0) {
            // 表示开发者没有主动调用开始的时间记录 inOutStartTime() 方法
            return Long.MAX_VALUE;
        }

        if (this.inOutTime == 0) {
            this.inOutTime = System.currentTimeMillis() - this.inOutStartTime;
        }

        return inOutTime;
    }
}

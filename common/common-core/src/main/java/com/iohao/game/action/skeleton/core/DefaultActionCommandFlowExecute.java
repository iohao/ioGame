/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.core.flow.ActionAfter;
import com.iohao.game.action.skeleton.protocol.RequestMessage;

/**
 * 默认的 action 命令流程执行器
 * <pre>
 *     编排业务框架处理业务类的流程
 *
 *     文档
 *      https://www.yuque.com/iohao/game/gz4i3k
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-17
 */
public final class DefaultActionCommandFlowExecute implements ActionCommandFlowExecute {

    @Override
    public void execute(final FlowContext flowContext) {
        // 设置 flowContext 的一些属性
        this.settingFlowContext(flowContext);

        // 业务框架
        BarSkeleton barSkeleton = flowContext.getBarSkeleton();
        // 命令对象
        ActionCommand actionCommand = flowContext.getActionCommand();
        // inout manager
        InOutManager inOutManager = barSkeleton.inOutManager;

        // 1 ---- fuck前 在调用控制器对应处理方法前, 执行inout的in.
        inOutManager.fuckIn(flowContext);

        // true 表示没有错误码 。如果在这里有错误码，一般是业务参数验证得到的错误 （即开启了业务框架的验证）
        boolean notError = !flowContext.getResponse().hasError();
        if (notError) {
            // 2 ---- ActionController 工厂
            var factoryBean = barSkeleton.getActionFactoryBean();
            var controller = factoryBean.getBean(actionCommand);
            // 业务 actionController
            flowContext.setActionController(controller);

            // 3 ---- fuck中 开始执行控制器方法, 这是真正处理客户端请求的逻辑.
            var actionMethodInvoke = barSkeleton.getActionMethodInvoke();
            // 得到业务类的返回结果
            var result = actionMethodInvoke.invoke(flowContext);
            flowContext.setMethodResult(result);

            // 4 ---- wrap result 结果包装器
            var actionMethodResultWrap = barSkeleton.getActionMethodResultWrap();
            // 结果包装器
            actionMethodResultWrap.wrap(flowContext);
        }

        // 5 ---- after 一般用于响应数据到 请求端
        ActionAfter actionAfter = barSkeleton.getActionAfter();
        actionAfter.execute(flowContext);

        // 6 ---- fuck后 在调用控制器对应处理方法结束后, 执行inout的out.
        inOutManager.fuckOut(flowContext);
    }

    private void settingFlowContext(FlowContext flowContext) {
        // 请求参数
        RequestMessage request = flowContext.getRequest();
        // 业务框架
        BarSkeleton barSkeleton = flowContext.getBarSkeleton();
        // 元信息
        HeadMetadata headMetadata = request.getHeadMetadata();
        // 当前用户 id
        long userId = headMetadata.getUserId();

        // 创建响应对象
        var responseMessageCreate = barSkeleton.getResponseMessageCreate();
        // 响应
        var responseMessage = responseMessageCreate.createResponseMessage();

        request.settingCommonAttr(responseMessage);

        flowContext
                .setResponse(responseMessage)
                .setUserId(userId);

        // 参数解析器
        var paramParser = barSkeleton.getActionMethodParamParser();
        // 得到业务方法的参数列表 , 并验证
        var params = paramParser.listParam(flowContext);
        // 业务方法参数 save to flowContext
        flowContext.setMethodParams(params);
    }


    private DefaultActionCommandFlowExecute() {

    }

    public static DefaultActionCommandFlowExecute me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final DefaultActionCommandFlowExecute ME = new DefaultActionCommandFlowExecute();
    }
}

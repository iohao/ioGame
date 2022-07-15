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
package com.iohao.game.action.skeleton.core.flow.interal;

import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.doc.ActionCommandDoc;
import com.iohao.game.action.skeleton.core.flow.ActionMethodInOut;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.core.flow.attr.FlowOption;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.common.kit.StrKit;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * debug info 开发阶段推荐, see beetlsql DebugInterceptor
 *
 * <pre>
 * 日志输出
 *
 * ┏━━━━━ Debug [.(ActivityAction.java:1).hello] ━━━ [cmd:1 - subCmd:0 - cmdMerge:65536]
 * ┣ userId: 当前发起请求的 userId
 * ┣ 参数: active : Active(id=101, name=塔姆)
 * ┣ 响应: 塔姆, I'm here
 * ┣ 时间: 1 ms (业务方法总耗时)
 * ┗━━━━━ Debug [ActivityAction.java] ━━━
 *
 * 参数 :  通常是游戏前端传入的值
 * 响应：通常是业务方法返回的值 （游戏后端人员编写的业务）
 * 时间：执行业务方法总耗时
 * Debug [ActivityAction.java] ：表示业务方法是在这个类运行的
 * (ActivityAction.java:1).hello ：表示运行的业务方法名是 hello
 *
 * 有了以上信息，游戏开发者可以很快的定位问题。
 * 控制台会打印方法所在的类，包括方法所在的代码行数。
 * 这样可以使得开发者在开发工具中快速的导航到对应的代码；
 * 这是一个在开发阶段很有用的功能。
 *
 * 如果没有可视化的信息，开发中会浪费很多时间在前后端的沟通上，问题包括：
 * <ul>
 *     <li>是否传参问题 （游戏前端说传了）</li>
 *     <li>是否响应问题（游戏后端说返回了）</li>
 *     <li>业务执行时长问题 （游戏前端说没收到响应， 游戏后端说早就响应了）</li>
 *     <li>代码导航</li>
 * </ul>
 *
 * 其中代码导航可以让开发者快速的跳转到业务类对应代码中，在多人合作的项目中，可以快速的知道业务经过了哪些方法的执行，使得我们可以快速的进行阅读或修改；
 *
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
public final class DebugInOut implements ActionMethodInOut {

    final FlowOption<Long> timeKey = FlowOption.valueOf("ExecuteTimeInOutStartTime");

    final long time;

    public DebugInOut() {
        this(0);
    }

    /**
     * @param time >= 这个时间才打印
     */
    public DebugInOut(long time) {
        this.time = time;
    }

    @Override
    public void fuckIn(final FlowContext flowContext) {
        // 记录当前时间
        flowContext.option(timeKey, System.currentTimeMillis());
    }

    @Override
    public void fuckOut(final FlowContext flowContext) {

        long currentTimeMillis = System.currentTimeMillis();
        Long time = flowContext.option(timeKey);

        long ms = currentTimeMillis - time;

        if (this.time > ms) {
            return;
        }

        ActionCommand actionCommand = flowContext.getActionCommand();
        ActionCommandDoc actionCommandDoc = actionCommand.getActionCommandDoc();
        Class<?> cc = actionCommand.getActionControllerClazz();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("className", cc.getSimpleName());
        paramMap.put("actionMethodName", actionCommand.getActionMethodName());
        paramMap.put("time", ms);
        paramMap.put("lineNumber", actionCommandDoc.getLineNumber());
        // 路由信息
        CmdInfo cmdInfo = flowContext.getRequest().getHeadMetadata().getCmdInfo();
        paramMap.put("cmdInfo", cmdInfo);
        paramMap.put("userId", flowContext.getUserId());

        paramMap.put("paramName", "");
        paramMap.put("paramData", "");
        paramMap.put("returnData", "");

        paramMap.put("logicServerId", flowContext.option(FlowAttr.logicServerId));
        paramMap.put("logicServerTag", flowContext.option(FlowAttr.logicServerTag));

        methodRequestParam(flowContext, paramMap);

        ResponseMessage responseMessage = flowContext.getResponse();

        if (responseMessage.hasError()) {
            this.printValidate(flowContext, paramMap);
        } else {
            this.printNormal(flowContext, paramMap);
        }
    }

    private void printValidate(FlowContext flowContext, Map<String, Object> paramMap) {

        ResponseMessage responseMessage = flowContext.getResponse();
        paramMap.put("errorCode", responseMessage.getResponseStatus());
        paramMap.put("validatorMsg", responseMessage.getValidatorMsg());

        if (StrKit.isEmpty(responseMessage.getValidatorMsg())) {
            paramMap.put("validatorMsg", flowContext.option(FlowAttr.msgException));
        }

        String template = """
                ┏━━错误━━━ Debug. [({className}.java:{lineNumber}).{actionMethodName}] ━━━ {cmdInfo} ━━━ [逻辑服 [{logicServerTag}] - id:[{logicServerId}]]
                ┣ userId: {userId}
                ┣ 参数: {paramName} : {paramData}
                ┣ 错误码: {errorCode}
                ┣ 错误信息: {validatorMsg}
                ┣ 时间: {time} ms (业务方法总耗时)
                ┗━━━━━ Debug [{className}.java] ━━━
                """;

        String message = StrKit.format(template, paramMap);
        System.out.println(message);
    }

    private void printNormal(FlowContext flowContext, Map<String, Object> paramMap) {
        methodResponseData(flowContext, paramMap);

        ActionCommand actionCommand = flowContext.getActionCommand();

        if (actionCommand.getActionMethodReturnInfo().isVoid()) {
            paramMap.put("returnData", "void");
        }

        String template = """
                ┏━━━━━ Debug. [({className}.java:{lineNumber}).{actionMethodName}] ━━━ {cmdInfo} ━━━ [逻辑服 [{logicServerTag}] - id:[{logicServerId}]]
                ┣ userId: {userId}
                ┣ 参数: {paramName} : {paramData}
                ┣ 响应: {returnData}
                ┣ 时间: {time} ms (业务方法总耗时)
                ┗━━━━━ Debug [{className}.java] ━━━
                """;

        String message = StrKit.format(template, paramMap);
        System.out.println(message);
    }

    private void methodResponseData(FlowContext flowContext, Map<String, Object> paramMap) {
        Object data = flowContext.getMethodResult();

        if (Objects.isNull(data)) {
            data = "null";
        }

        paramMap.put("returnData", data);
    }

    private void methodRequestParam(FlowContext flowContext, Map<String, Object> paramMap) {
        ActionCommand actionCommand = flowContext.getActionCommand();
        if (!actionCommand.isHasMethodParam()) {
            return;
        }

        final var paramInfos = actionCommand.getParamInfos();

        for (ActionCommand.ParamInfo paramInfo : paramInfos) {
            Class<?> paramClazz = paramInfo.getParamClazz();

            if (FlowContext.class.equals(paramClazz)) {
                continue;
            }

            paramMap.put("paramName", paramInfo.getName());

            Object bizData = flowContext.option(FlowAttr.data);

            if (Objects.isNull(bizData)) {
                bizData = "null";
            }

            paramMap.put("paramData", bizData);
        }
    }

}

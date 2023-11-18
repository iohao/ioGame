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
package com.iohao.game.action.skeleton.core.flow.interal;

import com.iohao.game.action.skeleton.IoGameVersion;
import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.core.doc.ActionCommandDoc;
import com.iohao.game.action.skeleton.core.flow.ActionMethodInOut;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.action.skeleton.protocol.wrapper.ByteValueList;
import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.kit.StrKit;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;


/**
 * debug info 开发阶段推荐, see beetlsql DebugInterceptor
 *
 * <pre>
 *     <a href="https://www.yuque.com/iohao/game/pf3sx0">DebugInOut 插件-文档</a>
 *
 * 日志输出预览
 *
 * ┏━━━━━ Debug [.(ActivityAction.java:1).hello] ━━━ [cmd:1 - subCmd:0 - cmdMerge:65536]
 * ┣ userId: 当前发起请求的 userId
 * ┣ 参数: active : Active(id=101, name=塔姆)
 * ┣ 响应: 塔姆, I'm here
 * ┣ 时间: 1 ms (业务方法总耗时)
 * ┗━━━━━ Debug [ActivityAction.java] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
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
    final long time;

    /**
     * 开发者可自定义打印
     * <pre>
     *     message: 可用于直接输出的 debug 信息
     *
     *     通过此方法，可以做一些简单的逻辑，如：
     *     不输出某个 cmd 的信息、
     *     或使用 log.info 来打印 message ... 等等。
     * </pre>
     */
    @Setter
    BiConsumer<String, FlowContext> printConsumer = (message, flowContext) -> {
        // 打印 message
        System.out.println(message);
    };

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
        flowContext.inOutStartTime();
    }

    @Override
    public void fuckOut(final FlowContext flowContext) {

        long ms = flowContext.getInOutTime();

        if (this.time > ms) {
            return;
        }

        ActionCommand actionCommand = flowContext.getActionCommand();
        ActionCommandDoc actionCommandDoc = actionCommand.getActionCommandDoc();
        Class<?> cc = actionCommand.getActionControllerClazz();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ioGameVersion", IoGameVersion.VERSION);
        paramMap.put("threadName", Thread.currentThread().getName());
        paramMap.put("className", cc.getSimpleName());
        paramMap.put("actionMethodName", actionCommand.getActionMethodName());
        paramMap.put("time", ms);
        paramMap.put("lineNumber", actionCommandDoc.getLineNumber());
        // 路由信息
        CmdInfo cmdInfo = flowContext.getRequest().getHeadMetadata().getCmdInfo();
        paramMap.put("cmdInfo", CmdKit.mergeToShort(cmdInfo.getCmdMerge()));
        paramMap.put("userId", flowContext.getUserId());

        paramMap.put("paramName", "");
        paramMap.put("paramData", "");
        paramMap.put("returnData", "");

        paramMap.put("logicServerId", flowContext.option(FlowAttr.logicServerId));
        paramMap.put("logicServerTag", flowContext.option(FlowAttr.logicServerTag));

        extractedJoin(flowContext, paramMap);

        methodRequestParam(flowContext, paramMap);

        ResponseMessage responseMessage = flowContext.getResponse();

        if (responseMessage.hasError()) {
            this.printValidate(flowContext, paramMap);
        } else {
            this.printNormal(flowContext, paramMap);
        }
    }

    private static void extractedJoin(FlowContext flowContext, Map<String, Object> paramMap) {
        HeadMetadata headMetadata = flowContext.getRequest().getHeadMetadata();
        int stick = headMetadata.getStick();

        String str = switch (stick) {
            case 1 -> " [连接方式:TCP] ";
            case 2 -> " [连接方式:WebSocket] ";
            case 3 -> " [连接方式:UDP] ";
            default -> "";
        };

        paramMap.put("joinName", str);
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
                ┗━━━━━ [ioGame:{ioGameVersion}] ━━━━━ [线程:{threadName}] ━━━━━{joinName}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                """;

        String message = StrKit.format(template, paramMap);
        this.printConsumer.accept(message, flowContext);
    }

    private void printNormal(FlowContext flowContext, Map<String, Object> paramMap) {
        methodResponseData(flowContext, paramMap);

        ActionCommand actionCommand = flowContext.getActionCommand();

        if (actionCommand.getActionMethodReturnInfo().isVoid()) {
            paramMap.put("returnData", "void");
        }

        String template = """
                ┏━━━━━ Debug. [({className}.java:{lineNumber}).{actionMethodName}] ━━━━━ {cmdInfo} ━━━━━ [逻辑服 [{logicServerTag}] - id:[{logicServerId}]]
                ┣ userId: {userId}
                ┣ 参数: {paramName} : {paramData}
                ┣ 响应: {returnData}
                ┣ 时间: {time} ms (业务方法总耗时)
                ┗━━━━━ [ioGame:{ioGameVersion}] ━━━━━ [线程:{threadName}] ━━━━━{joinName}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                """;

        String message = StrKit.format(template, paramMap);
        this.printConsumer.accept(message, flowContext);
    }

    private void methodResponseData(FlowContext flowContext, Map<String, Object> paramMap) {
        Object data = flowContext.getMethodResult();

        if (Objects.isNull(data)) {
            data = "null";
        }

        // 将 ByteValueList 内的元素打印
        if (data instanceof ByteValueList byteValueList && CollKit.notEmpty(byteValueList.values)) {
            ActionCommand actionCommand = flowContext.getActionCommand();
            ActionCommand.ActionMethodReturnInfo actionMethodReturnInfo = actionCommand.getActionMethodReturnInfo();
            Class<?> actualTypeArgumentClazz = actionMethodReturnInfo.getActualTypeArgumentClazz();

            data = byteValueList.values.stream()
                    .map(bytes -> DataCodecKit.decode(bytes, actualTypeArgumentClazz))
                    .toList();
        }

        paramMap.put("returnData", data);
    }

    private void methodRequestParam(FlowContext flowContext, Map<String, Object> paramMap) {
        ActionCommand actionCommand = flowContext.getActionCommand();
        if (!actionCommand.isMethodHasParam()) {
            return;
        }

        final var paramInfos = actionCommand.getParamInfos();

        for (ActionCommand.ParamInfo paramInfo : paramInfos) {
            Class<?> paramClazz = paramInfo.getParamClazz();

            if (FlowContext.class.isAssignableFrom(paramClazz)) {
                continue;
            }

            paramMap.put("paramName", paramInfo.getName());

            Object bizData = flowContext.option(FlowAttr.actionBizParam);

            if (Objects.isNull(bizData)) {
                bizData = "null";
            }

            paramMap.put("paramData", bizData);
        }
    }
}

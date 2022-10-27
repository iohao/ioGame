/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.bolt.broker.core.client;

import com.alibaba.fastjson2.JSONObject;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.core.DevConfig;
import com.iohao.game.action.skeleton.core.commumication.BroadcastContext;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.bolt.broker.core.message.BroadcastMessage;
import com.iohao.game.bolt.broker.core.message.BroadcastOrderMessage;
import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.kit.JsonKit;
import com.iohao.game.common.kit.StrKit;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 渔民小镇
 * @date 2022-05-19
 */
@Slf4j
@UtilityClass
class BroadcastDebug {
    final Map<String, Class<?>> classMap = new NonBlockingHashMap<>();

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    void print(BroadcastMessage broadcastMessage) {
        RuntimeException ex = new RuntimeException();
        StackTraceElement[] traces = ex.getStackTrace();
        int index = lookBusinessCodeInTrace(traces);
        StackTraceElement traceElement = traces[index];
//        extracted(traceElement);

        // 接收广播的用户
        String userIds = getUserIds(broadcastMessage);
        ResponseMessage responseMessage = broadcastMessage.getResponseMessage();
        Object returnData = getReturnData(responseMessage);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("returnData", returnData);
        paramMap.put("userIds", userIds);
        paramMap.put("className", traceElement.getFileName());
        paramMap.put("lineNumber", traceElement.getLineNumber());
        paramMap.put("cmdInfo", responseMessage.getHeadMetadata().getCmdInfo());
        paramMap.put("time", dateTimeFormatter.format(LocalDateTime.now()));

        String title = broadcastMessage instanceof BroadcastOrderMessage ? "严格顺序广播" : "广播";
        paramMap.put("title", title);

        String template = """
                ┏━━━━━ {title}. [({className}:{lineNumber})] ━━━ {cmdInfo}
                ┣ userId: {userIds}
                ┣ 广播数据: {returnData}
                ┣ 广播时间: {time}
                ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                """;

        String message = StrKit.format(template, paramMap);
        log.info("\n{}", message);
    }

    private Object getReturnData(ResponseMessage responseMessage) {
        Object returnData = null;

        try {
            String dataClass = responseMessage.getDataClass();

            Class<?> aClass = classMap.get(dataClass);
            // 无锁化
            if (aClass == null) {
                aClass = Class.forName(dataClass);
                aClass = classMap.putIfAbsent(dataClass, aClass);
                if (aClass == null) {
                    aClass = classMap.get(dataClass);
                }
            }

            returnData = DataCodecKit.decode(responseMessage.getData(), aClass);

            // 保存 cmd 路由对应的响应数据类型 class 信息
            int cmdMerge = responseMessage.getHeadMetadata().getCmdMerge();
            DevConfig.me().getCmdDataClassMap().putIfAbsent(cmdMerge, aClass);

        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }

        return returnData;
    }

    private String getUserIds(BroadcastMessage broadcastMessage) {
        String userIds = "";
        Collection<Long> userIdList = broadcastMessage.getUserIdList();

        if (broadcastMessage.isBroadcastAll()) {
            userIds = "全服广播";
        } else if (CollKit.notEmpty(userIdList)) {
            userIds = userIdList.toString();
        } else if (broadcastMessage.getResponseMessage().getHeadMetadata().getUserId() != 0) {
            userIds = String.valueOf(broadcastMessage.getResponseMessage().getHeadMetadata().getUserId());
        }

        return userIds;
    }

    private int lookBusinessCodeInTrace(StackTraceElement[] traces) {
        for (int index = traces.length - 1; index >= 0; index--) {
            String name = traces[index].getClassName();
            // 广播对象
            if (BroadcastContext.class.getName().equals(name)) {
                return index + 1;
            }

            // 房间内的广播
            if ("com.iohao.game.widget.light.room.AbstractRoom".equals(name)) {
                return index + 1;
            }
        }

        return 2;
    }

    private void extracted(StackTraceElement traceElement) {
        String className = traceElement.getClassName();
        String methodName = traceElement.getMethodName();
        int line = traceElement.getLineNumber();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("className", className);
        jsonObject.put("methodName", methodName);
        jsonObject.put("line", line);

        String jsonPretty = JsonKit.toJsonPretty(jsonObject);
        log.info("jsonPretty : {}", jsonPretty);
        System.out.println("~~~~~~~~~~~~~~~~~~");
        System.out.println("~~~~~~~~~~~~~~~~~~");
    }
}

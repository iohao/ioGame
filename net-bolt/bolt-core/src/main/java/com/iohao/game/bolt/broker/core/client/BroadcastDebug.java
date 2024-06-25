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
package com.iohao.game.bolt.broker.core.client;

import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.core.commumication.BroadcastContext;
import com.iohao.game.action.skeleton.kit.RangeBroadcast;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.bolt.broker.core.message.BroadcastMessage;
import com.iohao.game.bolt.broker.core.message.BroadcastOrderMessage;
import com.iohao.game.common.consts.IoGameLogName;
import com.iohao.game.common.kit.ArrayKit;
import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.kit.MoreKit;
import com.iohao.game.common.kit.StrKit;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2022-05-19
 */
@UtilityClass
@Slf4j(topic = IoGameLogName.CommonStdout)
class BroadcastDebug {
    final Map<String, Class<?>> classMap = new NonBlockingHashMap<>();

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    void print(BroadcastMessage broadcastMessage) {
        RuntimeException ex = new RuntimeException();
        StackTraceElement[] traces = ex.getStackTrace();
        int index = lookBusinessCodeInTrace(traces);
        StackTraceElement traceElement = traces[index];

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

        byte[] data = responseMessage.getData();
        String dataClass = responseMessage.getDataClass();

        if (ArrayKit.isEmpty(data) || Objects.isNull(dataClass)) {
            return "null or []";
        }

        Class<?> aClass = null;

        try {
            aClass = getDataClass(dataClass);
            if (Objects.isNull(aClass)) {
                return "null";
            }
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }

        return DataCodecKit.decode(responseMessage.getData(), aClass);
    }

    private Class<?> getDataClass(String dataClass) throws ClassNotFoundException {
        Class<?> aClass = classMap.get(dataClass);

        // 无锁化
        if (aClass == null) {
            Class<?> newValue = Class.forName(dataClass);
            return MoreKit.putIfAbsent(classMap, dataClass, newValue);
        }

        return aClass;
    }

    private String getUserIds(BroadcastMessage broadcastMessage) {

        if (broadcastMessage.isBroadcastAll()) {
            return "全服广播";
        }

        var userIdList = broadcastMessage.getUserIdList();
        if (CollKit.notEmpty(userIdList)) {
            return userIdList.toString();
        }

        long userId = broadcastMessage.getResponseMessage().getHeadMetadata().getUserId();

        if (userId != 0) {
            return String.valueOf(userId);
        }

        return "";
    }

    private int lookBusinessCodeInTrace(StackTraceElement[] traces) {
        for (int index = traces.length - 1; index >= 0; index--) {
            String name = traces[index].getClassName();
            // 广播对象
            if (BroadcastContext.class.getName().equals(name)) {
                return index + 1;
            }

            // 范围广播
            if (RangeBroadcast.class.getName().equals(name)) {
                return index + 1;
            }
        }

        return 2;
    }

    private void extracted(StackTraceElement traceElement) {
        String className = traceElement.getClassName();
        String methodName = traceElement.getMethodName();
        int line = traceElement.getLineNumber();

        String format = "className:%s - methodName:%s - line:%d";
        String msg = String.format(format, className, methodName, line);
        System.out.println(msg);
    }
}

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
package com.iohao.game.action.skeleton.core.flow;

import com.iohao.game.action.skeleton.core.*;
import com.iohao.game.action.skeleton.core.commumication.BrokerClientContext;
import com.iohao.game.action.skeleton.core.commumication.InvokeModuleContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.core.flow.attr.FlowOption;
import com.iohao.game.action.skeleton.core.flow.attr.FlowOptionDynamic;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.action.skeleton.protocol.collect.ResponseCollectMessage;
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
 */
@Setter
@Getter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlowContext implements FlowOptionDynamic {
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
    /** userId */
    long userId;
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

    public CmdInfo getCmdInfo() {
        HeadMetadata headMetadata = this.request.getHeadMetadata();
        return headMetadata.getCmdInfo();
    }

    /**
     * 元附加信息
     * <pre>
     *     一般是在游戏对外服中设置的一些附加信息
     *     这些信息会跟随请求来到游戏逻辑服中
     * </pre>
     *
     * @param clazz clazz
     * @param <T>   t
     * @return 元附加信息
     */
    public <T> T getAttachment(Class<T> clazz) {
        byte[] attachmentData = this.request.getHeadMetadata().getAttachmentData();
        return DataCodecKit.decode(attachmentData, clazz);
    }

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

    /**
     * cmdInfo
     * <pre>
     *     将在下个大版本中移除，因为类职责的原因
     *
     *     flowContext 中已经有 getCmdInfo() 方法，这个方法表示当前请求的上下文中的 cmdInfo 信息
     *
     *     如果在 flowContext 中提供这个带参数的 getCmdInfo(int,int) 方法，
     *     会给使用的开发者造成理解上的困难，因为这个方法表示获取一个"新的" cmdInfo 信息
     *
     *     建议使用 {@link CmdInfo#getCmdInfo(int, int)} 来代替
     * </pre>
     *
     * @param cmd    主 cmd
     * @param subCmd 子 cmd
     * @return cmdInfo
     */
    @Deprecated
    public CmdInfo getCmdInfo(int cmd, int subCmd) {
        return CmdInfo.getCmdInfo(cmd, subCmd);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     * <pre>
     *     相关文档
     *     https://www.yuque.com/iohao/game/anguu6
     * </pre>
     *
     * <pre>
     *     将在下个大版本中移除，因为类职责的原因
     *     具体描述参考 https://www.yuque.com/iohao/game/zz8xiz#sLySn
     * </pre>
     *
     * @param cmdInfo 路由信息
     * @param data    请求参数
     * @param clazz   pb class
     * @param <T>     t
     * @return pb 对象
     */
    @Deprecated
    public <T> T invokeModuleMessageData(CmdInfo cmdInfo, Object data, Class<T> clazz) {
        // 当前项目启动的服务上下文
        BrokerClientContext brokerClientContext = this.option(FlowAttr.brokerClientContext);
        InvokeModuleContext invokeModuleContext = brokerClientContext.getInvokeModuleContext();

        return invokeModuleContext.invokeModuleMessageData(cmdInfo, data, clazz);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     * <pre>
     *     相关文档
     *     https://www.yuque.com/iohao/game/anguu6
     * </pre>
     *
     * <pre>
     *     将在下个大版本中移除，因为类职责的原因
     *     具体描述参考 https://www.yuque.com/iohao/game/zz8xiz#sLySn
     * </pre>
     *
     * @param cmdInfo 路由信息
     * @param clazz   pb class
     * @param <T>     t
     * @return pb 对象
     */
    @Deprecated
    public <T> T invokeModuleMessageData(CmdInfo cmdInfo, Class<T> clazz) {
        return this.invokeModuleMessageData(cmdInfo, null, clazz);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     * <pre>
     *     相关文档
     *     https://www.yuque.com/iohao/game/anguu6
     * </pre>
     *
     * <pre>
     *     将在下个大版本中移除，因为类职责的原因
     *     具体描述参考 https://www.yuque.com/iohao/game/zz8xiz#sLySn
     * </pre>
     *
     * @param cmdInfo cmdInfo
     * @param data    请求参数
     * @return ResponseMessage
     */
    @Deprecated
    public ResponseMessage invokeModuleMessage(CmdInfo cmdInfo, Object data) {

        RequestMessage requestMessage = createRequestMessage(cmdInfo, data);
        // 当前项目启动的服务上下文
        BrokerClientContext brokerClientContext = this.option(FlowAttr.brokerClientContext);
        InvokeModuleContext invokeModuleContext = brokerClientContext.getInvokeModuleContext();

        return invokeModuleContext.invokeModuleMessage(requestMessage);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的数据
     * <pre>
     *     相关文档
     *     https://www.yuque.com/iohao/game/anguu6
     * </pre>
     *
     * <pre>
     *     将在下个大版本中移除，因为类职责的原因
     *     具体描述参考 https://www.yuque.com/iohao/game/zz8xiz#sLySn
     * </pre>
     *
     * @param cmdInfo cmdInfo
     * @return ResponseMessage
     */
    @Deprecated
    public ResponseMessage invokeModuleMessage(CmdInfo cmdInfo) {
        return this.invokeModuleMessage(cmdInfo, null);
    }

    /**
     * 模块之间的访问，访问【同类型】的多个逻辑服
     * <pre>
     *     模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是逻辑服。
     *     假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起。
     *
     *     具体的意思可以参考文档中的说明
     *     https://www.yuque.com/iohao/game/rf9rb9
     * </pre>
     *
     * <pre>
     *     将在下个大版本中移除，因为类职责的原因
     *     具体描述参考 https://www.yuque.com/iohao/game/zz8xiz#sLySn
     * </pre>
     *
     * @param cmdInfo 路由信息
     * @param data    业务数据
     * @return ResponseCollectMessage
     */
    @Deprecated
    public ResponseCollectMessage invokeModuleCollectMessage(CmdInfo cmdInfo, Object data) {
        RequestMessage requestMessage = createRequestMessage(cmdInfo, data);
        // 当前项目启动的服务上下文
        BrokerClientContext brokerClientContext = this.option(FlowAttr.brokerClientContext);
        InvokeModuleContext invokeModuleContext = brokerClientContext.getInvokeModuleContext();

        return invokeModuleContext.invokeModuleCollectMessage(requestMessage);
    }

    /**
     * 模块之间的访问，访问【同类型】的多个逻辑服
     * <pre>
     *     模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是逻辑服。
     *     假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起。
     *
     *     具体的意思可以参考文档中的说明
     *     https://www.yuque.com/iohao/game/rf9rb9
     * </pre>
     *
     * <pre>
     *     将在下个大版本中移除，因为类职责的原因
     *     具体描述参考 https://www.yuque.com/iohao/game/zz8xiz#sLySn
     * </pre>
     *
     * @param cmdInfo 路由信息
     * @return ResponseCollectMessage
     */
    @Deprecated
    public ResponseCollectMessage invokeModuleCollectMessage(CmdInfo cmdInfo) {
        return invokeModuleCollectMessage(cmdInfo, null);
    }

    /**
     * 创建一个 request 对象，并使用当前 FlowContext HeadMetadata 部分属性。
     * <pre>
     *     HeadMetadata 对象以下属性不会赋值，如有需要，请自行赋值
     *       sourceClientId
     *       endPointClientId
     *       rpcCommandType
     *       msgId
     * </pre>
     *
     * @param cmdInfo 路由
     * @param data    业务参数
     * @return request
     */
    protected RequestMessage createRequestMessage(CmdInfo cmdInfo, Object data) {

        HeadMetadata headMetadata = this.request
                .getHeadMetadata()
                .cloneHeadMetadata()
                .setCmdInfo(cmdInfo);

        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setHeadMetadata(headMetadata);

        if (Objects.nonNull(data)) {
            requestMessage.setData(data);
        }

        return requestMessage;
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

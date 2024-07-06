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
package com.iohao.game.action.skeleton.core.doc;

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.protocol.wrapper.ByteValueList;
import com.iohao.game.action.skeleton.protocol.wrapper.WrapperKit;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

/**
 * 广播文档构建器
 *
 * @author 渔民小镇
 * @date 2024-07-05
 * @since 21.11
 */
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PACKAGE)
public class BroadcastDocumentBuilder {
    /** 路由 */
    final CmdInfo cmdInfo;

    /** 业务数据类型 */
    Class<?> dataClass;
    @Setter(AccessLevel.PRIVATE)
    String dataClassName;
    /** 广播业务参数的描述 */
    String dataDescription;

    @Setter(AccessLevel.PACKAGE)
    boolean list;

    /** 广播方法名，仅在生成客户端代码时使用 */
    String methodName;
    /** 广播（推送）描述 */
    String methodDescription;

    BroadcastDocumentBuilder(CmdInfo cmdInfo) {
        this.cmdInfo = cmdInfo;
    }

    /**
     * set 推送的数据类型 List dataClass，ByteValueList dataClass。
     *
     * @param dataClass 数据类型
     * @return this
     */
    public BroadcastDocumentBuilder setDataClassList(Class<?> dataClass) {
        return this.setDataClassList(dataClass, null);
    }

    public BroadcastDocumentBuilder setDataClassList(Class<?> dataClass, String dataDescription) {
        String simpleName = ByteValueList.class.getSimpleName();
        String simpleNameActualClazz = dataClass.getSimpleName();

        this.dataClassName = String.format("%s<%s>", simpleName, simpleNameActualClazz);
        this.list = true;
        this.dataClass = dataClass;
        this.dataDescription = dataDescription;

        return this;
    }

    /**
     * set 推送的数据类型
     *
     * @param dataClass 推送的数据类型
     * @return this
     */
    public BroadcastDocumentBuilder setDataClass(Class<?> dataClass) {
        return setDataClass(dataClass, null);
    }

    /**
     * set 推送的数据类型
     *
     * @param dataClass       推送的数据类型
     * @param dataDescription 业务数据描述
     * @return this
     */
    public BroadcastDocumentBuilder setDataClass(Class<?> dataClass, String dataDescription) {

        this.dataDescription = dataDescription;

        this.dataClassName = WrapperKit.optionalRefType(dataClass)
                .map(Class::getSimpleName)
                .orElse(dataClass.getSimpleName());

        this.dataClass = dataClass;

        return this;
    }

    private String getMethodName() {
        // 如果没有指定广播的方法名，则方法名使用下述规则
        return Objects.isNull(methodName)
                ? "Method_%d_%d".formatted(cmdInfo.getCmd(), cmdInfo.getSubCmd())
                : methodName;
    }

    /**
     * 构建广播文档对象
     *
     * @return BroadcastDocument 广播文档
     */
    public BroadcastDocument build() {
        String theMethodName = getMethodName();

        return new BroadcastDocument(this.cmdInfo)
                // 方法相关
                .setMethodDescription(this.methodDescription)
                .setMethodName(theMethodName)
                // 业务参数相关
                .setDataClass(this.dataClass)
                .setDataClassName(this.dataClassName)
                .setDataDescription(this.dataDescription)
                .setDataIsList(this.list);
    }

    /**
     * 构建广播文档对象，并添加到 {@link IoGameDocumentHelper}
     */
    public void buildToDocument() {
        IoGameDocumentHelper.addBroadcastDocument(this.build());
    }
}

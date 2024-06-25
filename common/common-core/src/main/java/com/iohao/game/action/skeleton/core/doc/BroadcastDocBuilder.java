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
import java.util.Optional;

/**
 * 广播文档构建器
 *
 * @author 渔民小镇
 * @date 2024-05-18
 * @since 21.8
 */
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PACKAGE)
public final class BroadcastDocBuilder {
    /** 路由 */
    final CmdInfo cmdInfo;
    /** 广播（推送）描述 */
    String description;
    @Setter(AccessLevel.PRIVATE)
    String dataClassName;
    /** 广播业务参数的描述 */
    String dataDescription;
    boolean list;
    /** 业务数据类型 */
    Class<?> dataClass;

    BroadcastDocBuilder(CmdInfo cmdInfo) {
        this.cmdInfo = cmdInfo;
    }

    /**
     * set 推送的数据类型 List dataClass，ByteValueList dataClass。
     *
     * @param dataClass 数据类型
     * @return this
     */
    public BroadcastDocBuilder setDataClassList(Class<?> dataClass) {
        return this.setDataClassList(dataClass, "");
    }

    public BroadcastDocBuilder setDataClassList(Class<?> dataClass, String dataDescription) {
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
    public BroadcastDocBuilder setDataClass(Class<?> dataClass) {
        return setDataClass(dataClass, "");
    }

    /**
     * set 推送的数据类型
     *
     * @param dataClass       推送的数据类型
     * @param dataDescription 业务数据描述
     * @return this
     */
    public BroadcastDocBuilder setDataClass(Class<?> dataClass, String dataDescription) {

        this.dataDescription = dataDescription;

        this.dataClassName = WrapperKit.optionalRefType(dataClass)
                .map(Class::getSimpleName)
                .orElse(dataClass.getSimpleName());

        this.dataClass = dataClass;

        return this;
    }

    /**
     * 构建广播文档
     *
     * @return 广播文档
     */
    public ActionSendDoc build() {

        Objects.requireNonNull(this.description);

        this.dataClassName = Optional.ofNullable(this.dataClassName).orElse("none");
        this.dataDescription = Optional.ofNullable(this.dataDescription).orElse("");

        ActionSendDoc actionSendDoc = new ActionSendDoc(this.cmdInfo);
        actionSendDoc.setDescription(this.description);
        actionSendDoc.setDataClassName(this.dataClassName);
        actionSendDoc.setDataDescription(this.dataDescription);
        actionSendDoc.setDataClass(this.dataClass);

        return actionSendDoc;
    }
}

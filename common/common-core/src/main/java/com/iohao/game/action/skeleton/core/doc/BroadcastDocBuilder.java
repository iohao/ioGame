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
import lombok.AccessLevel;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * 广播文档构建器
 *
 * @author 渔民小镇
 * @date 2024-05-18
 * @since 21.8
 * @deprecated 请使用 {@link BroadcastDocumentBuilder}
 */
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PACKAGE)
@Deprecated
public final class BroadcastDocBuilder {
    final BroadcastDocumentBuilder proxy;

    BroadcastDocBuilder(CmdInfo cmdInfo) {
        this.proxy = BroadcastDocument.newBuilder(cmdInfo);
    }

    public BroadcastDocBuilder setMethodDescription(String methodDescription) {
        this.proxy.setMethodDescription(methodDescription);
        return this;
    }


    public BroadcastDocBuilder setDataDescription(String dataDescription) {
        this.proxy.setDataDescription(dataDescription);
        return this;
    }

    public BroadcastDocBuilder setList(boolean list) {
        this.proxy.setList(list);
        return this;
    }

    public BroadcastDocBuilder setMethodName(String methodName) {
        this.proxy.setMethodName(methodName);
        return this;
    }

    /**
     * set 广播（推送）描述
     *
     * @param description 广播（推送）描述
     * @return this
     */
    public BroadcastDocBuilder setDescription(String description) {
        this.proxy.setMethodDescription(description);
        return this;
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
        this.proxy.setDataClassList(dataClass, dataDescription);
        return this;
    }

    /**
     * set 推送的数据类型
     *
     * @param dataClass 推送的数据类型
     * @return this
     */
    public BroadcastDocBuilder setDataClass(Class<?> dataClass) {
        return setDataClass(dataClass, null);
    }

    /**
     * set 推送的数据类型
     *
     * @param dataClass       推送的数据类型
     * @param dataDescription 业务数据描述
     * @return this
     */
    public BroadcastDocBuilder setDataClass(Class<?> dataClass, String dataDescription) {

        this.proxy.setDataClass(dataClass, dataDescription);

        return this;
    }

    /**
     * 构建广播文档
     *
     * @return 广播文档
     * @deprecated 请使用 {@link BroadcastDocBuilder#buildDocument()}
     */
    @Deprecated
    public ActionSendDoc build() {
        return null;
    }

    /**
     * 构建广播文档
     *
     * @return BroadcastDocument 广播文档
     */
    public BroadcastDocument buildDocument() {
        return this.proxy.build();
    }
}

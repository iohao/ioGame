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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * 广播文档
 *
 * @author 渔民小镇
 * @date 2024-06-25
 */
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class BroadcastDocument {
    /** 路由 */
    final CmdInfo cmdInfo;
    /** 推送方法描述 */
    String methodDescription;
    /** 方法名 */
    String methodName;

    /** 业务类型 */
    Class<?> dataClass;
    /** 业务参数 */
    String dataClassName;
    /** 广播业务参数的描述 */
    String dataDescription;

    /** true 表示协议碎片，false 表示开发者自定义的协议 */
    boolean dataTypeIsInternal;
    /** 广播业务参数是否是 List */
    boolean dataIsList;

    String bizDataType;

    /** sdk result get 方法名 */
    String resultMethodTypeName;
    /** sdk result get list 方法名 */
    String resultMethodListTypeName;

    String dataActualTypeName;

    String exampleCode;

    public int getCmdMerge() {
        return this.cmdInfo.getCmdMerge();
    }

    public int getCmd() {
        return this.cmdInfo.getCmd();
    }

    public int getSubCmd() {
        return this.cmdInfo.getSubCmd();
    }

    BroadcastDocument(CmdInfo cmdInfo) {
        this.cmdInfo = cmdInfo;
    }

    public static BroadcastDocumentBuilder newBuilder(CmdInfo cmdInfo) {
        return new BroadcastDocumentBuilder(cmdInfo);
    }
}

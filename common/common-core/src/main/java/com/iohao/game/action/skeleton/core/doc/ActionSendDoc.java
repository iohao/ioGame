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

import com.iohao.game.action.skeleton.annotation.DocActionSend;
import com.iohao.game.action.skeleton.core.CmdInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * @author 渔民小镇
 * @date 2022-02-01
 * @deprecated 请使用 {@link BroadcastDocument}
 */
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Deprecated
public final class ActionSendDoc {
    /** 主路由 */
    final int cmd;
    /** 子路由 */
    final int subCmd;
    /** 业务类型 */
    Class<?> dataClass;
    /** 推送描述 */
    String description;
    /** 业务参数 */
    String dataClassName;
    /** 广播业务参数的描述 */
    String dataDescription;
    /** 广播业务参数是否是 List */
    boolean list;
    String methodName;

    public ActionSendDoc(DocActionSend docActionSend) {
        this(docActionSend.cmd(), docActionSend.subCmd(), docActionSend.dataClass(), docActionSend.description());
    }

    public ActionSendDoc(CmdInfo cmdInfo, Class<?> dataClass, String description) {
        this(cmdInfo.getCmd(), cmdInfo.getSubCmd(), dataClass, description);
    }

    public ActionSendDoc(int cmd, int subCmd, Class<?> dataClass, String description) {
        this.cmd = cmd;
        this.subCmd = subCmd;
        this.dataClass = dataClass;
        this.description = description;
    }

    public ActionSendDoc(CmdInfo cmdInfo) {
        this.cmd = cmdInfo.getCmd();
        this.subCmd = cmdInfo.getSubCmd();
    }
}

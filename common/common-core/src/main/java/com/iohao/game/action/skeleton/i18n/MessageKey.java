/*
 * ioGame
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General  License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General  License for more details.
 *
 * You should have received a copy of the GNU Affero General  License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.iohao.game.action.skeleton.i18n;

/**
 * @author 渔民小镇
 * @date 2024-10-02
 * @since 21.18
 */
public interface MessageKey {
    String cmdName = "cmdName";
    String gameExternalServer = "gameExternalServer";
    /** ExternalJoinEnum */
    String connectionWay = "connectionWay";

    /* see PrintActionKit.java */
    String printActionKitPrintClose = "printActionKitPrintClose";
    String printActionKitPrintFull = "printActionKitPrintFull";
    String printActionKitDataCodec = "printActionKitDataCodec";
    String printActionKitCheckReturnType = "printActionKitCheckReturnType";

    /* see DebugInOut.java */
    String debugInOutThreadName = "debugInOutThreadName";
    String debugInOutParamName = "debugInOutParamName";
    String debugInOutReturnData = "debugInOutReturnData";
    String debugInOutErrorCode = "debugInOutErrorCode";
    String debugInOutErrorMsg = "debugInOutErrorMsg";
    String debugInOutTime = "debugInOutTime";
}

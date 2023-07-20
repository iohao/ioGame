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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.core.flow.ActionMethodInOut;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * InOut 插件相关
 *
 * @author 渔民小镇
 * @date 2022-03-08
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class InOutManager {
    /** true : 开放拦截 in */
    final boolean openIn;
    /** true : 开放拦截 out */
    final boolean openOut;
    /** inOuts */
    @Getter
    final ActionMethodInOut[] inOuts;

    InOutManager(BarSkeletonSetting setting, List<ActionMethodInOut> inOutList) {
        this(setting.openIn, setting.openOut, inOutList);
    }

    private InOutManager(boolean openIn, boolean openOut, List<ActionMethodInOut> inOutList) {
        this.inOuts = new ActionMethodInOut[inOutList.size()];
        inOutList.toArray(this.inOuts);

        if (inOuts.length == 0) {
            openIn = false;
            openOut = false;
        }

        this.openIn = openIn;
        this.openOut = openOut;
    }

    public void fuckIn(FlowContext flowContext) {
        if (!this.openIn) {
            return;
        }

        if (this.inOuts.length == 1) {
            this.inOuts[0].fuckIn(flowContext);
            return;
        }

        for (ActionMethodInOut inOut : this.inOuts) {
            inOut.fuckIn(flowContext);
        }
    }

    public void fuckOut(FlowContext flowContext) {
        if (!this.openOut) {
            return;
        }

        if (this.inOuts.length == 1) {
            this.inOuts[0].fuckOut(flowContext);
            return;
        }

        for (ActionMethodInOut inOut : this.inOuts) {
            inOut.fuckOut(flowContext);
        }
    }
}

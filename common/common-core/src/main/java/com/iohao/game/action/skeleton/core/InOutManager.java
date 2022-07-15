/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
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
public class InOutManager {
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

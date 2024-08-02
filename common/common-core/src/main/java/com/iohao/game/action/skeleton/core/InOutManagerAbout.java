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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.core.flow.ActionMethodInOut;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
final class AbcAbcInOutManager implements InOutManager {
    final List<ActionMethodInOut> inOutList = new ArrayList<>();

    public void fuckIn(FlowContext flowContext) {
        if (inOutList.isEmpty()) {
            return;
        }

        if (this.inOutList.size() == 1) {
            this.inOutList.getFirst().fuckIn(flowContext);
            return;
        }

        for (ActionMethodInOut inOut : this.inOutList) {
            inOut.fuckIn(flowContext);
        }
    }

    public void fuckOut(FlowContext flowContext) {
        if (inOutList.isEmpty()) {
            return;
        }

        if (this.inOutList.size() == 1) {
            this.inOutList.getFirst().fuckOut(flowContext);
            return;
        }

        for (ActionMethodInOut inOut : this.inOutList) {
            inOut.fuckOut(flowContext);
        }
    }

    @Override
    public void addInOut(ActionMethodInOut inOut) {
        this.inOutList.add(inOut);
    }

    public List<ActionMethodInOut> listInOut() {
        return this.inOutList;
    }
}

final class PipelineInOutManager implements InOutManager {
    final List<ActionMethodInOut> inList = new ArrayList<>();
    final List<ActionMethodInOut> outList = new ArrayList<>();

    @Override
    public void fuckIn(FlowContext flowContext) {
        int size = inList.size();

        if (size == 0) {
            return;
        }

        if (size == 1) {
            this.inList.getFirst().fuckIn(flowContext);
            return;
        }

        for (ActionMethodInOut inOut : this.inList) {
            inOut.fuckIn(flowContext);
        }
    }

    @Override
    public void fuckOut(FlowContext flowContext) {
        int size = outList.size();

        if (size == 0) {
            return;
        }

        if (size == 1) {
            this.outList.getFirst().fuckOut(flowContext);
            return;
        }

        for (ActionMethodInOut inOut : this.outList) {
            inOut.fuckOut(flowContext);
        }
    }

    @Override
    public void addInOut(ActionMethodInOut inOut) {
        this.inList.addLast(inOut);
        this.outList.addFirst(inOut);
    }

    @Override
    public List<ActionMethodInOut> listInOut() {
        return this.inList;
    }
}
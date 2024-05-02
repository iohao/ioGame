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
package com.iohao.game.action.skeleton.core.data;

import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.core.*;
import com.iohao.game.action.skeleton.core.action.BeeAction;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.internal.DebugInOut;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.common.kit.ClassScanner;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@UtilityClass
public class TestDataKit {

    public BarSkeleton newBarSkeleton() {
        return createBuilder().build();
    }

    public BarSkeletonBuilder createBuilder(Predicate<Class<?>> appendPredicateFilter) {
        // 尽量做到所有操作是可插拔的. 详细配置 see BarSkeletonBuilder.build
        BarSkeletonBuilder builder = BarSkeleton.newBuilder();

        builder.addInOut(new DebugInOut());

        builder.setActionAfter(flowContext -> System.out.println());

        List<Class<?>> classList = getClasses(appendPredicateFilter);

        classList.forEach(builder::addActionController);

        BarSkeletonSetting setting = builder.getSetting();
        setting.setPrintHandler(false);
        setting.setPrintInout(false);
        setting.setPrintDataCodec(false);
        setting.setPrintRunners(false);
        setting.setPrintHandler(false);

        return builder;
    }

    private List<Class<?>> getClasses(Predicate<Class<?>> appendPredicateFilter) {
        Predicate<Class<?>> predicateFilter = (clazz) -> {
            if (clazz.getAnnotation(ActionController.class) == null) {
                return false;
            }

            if (Objects.nonNull(appendPredicateFilter)) {
                if (appendPredicateFilter.test(clazz)) {
                    return true;
                }

                return false;
            }

            return true;
        };

        String packagePath = BeeAction.class.getPackageName();
        ClassScanner classScanner = new ClassScanner(packagePath, predicateFilter);
        return classScanner.listScan();
    }

    public BarSkeletonBuilder createBuilder() {
        return createBuilder(null);
    }

    public FlowContext ofFlowContext(CmdInfo cmdInfo, Object data) {
        RequestMessage requestMessage = BarMessageKit.createRequestMessage(cmdInfo, data);

        FlowContext flowContext = new FlowContext();
        flowContext.setRequest(requestMessage);

        return flowContext;
    }
}

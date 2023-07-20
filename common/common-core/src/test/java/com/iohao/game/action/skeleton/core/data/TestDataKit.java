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
package com.iohao.game.action.skeleton.core.data;

import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.BarSkeletonBuilder;
import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.action.BeeAction;
import com.iohao.game.action.skeleton.core.flow.interal.DebugInOut;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.common.kit.ClassScanner;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.Predicate;

@UtilityClass
public class TestDataKit {

    public BarSkeleton newBarSkeleton() {
        BarSkeletonBuilder builder = createBuilder();
        builder.setActionAfter(flowContext -> {

        });
        return builder.build();
    }

    public BarSkeletonBuilder createBuilder() {
        // 尽量做到所有操作是可插拔的. 详细配置 see BarSkeletonBuilder.build
        BarSkeletonBuilder builder = BarSkeleton.newBuilder();

        builder.addInOut(new DebugInOut());

        // 添加(请求响应)处理类. 用户可以定义自己的业务控制器 - 这里推荐实现扫描包的形式添加 tcp 处理类
//        builder
//                .addActionController(BeeAction.class)
//                .addActionController(BirdAction.class)
//        ;

        Predicate<Class<?>> predicateFilter = (clazz) -> clazz.getAnnotation(ActionController.class) != null;

        String packagePath = BeeAction.class.getPackageName();

        ClassScanner classScanner = new ClassScanner(packagePath, predicateFilter);
        List<Class<?>> classList = classScanner.listScan();

        classList.forEach(builder::addActionController);


        return builder;
    }

    public RequestMessage createRequestMessage(CmdInfo cmdInfo) {
        // 模拟请求
        HeadMetadata headMetadata = new HeadMetadata();
        headMetadata.setCmdInfo(cmdInfo);

        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setHeadMetadata(headMetadata);

        return requestMessage;
    }
}

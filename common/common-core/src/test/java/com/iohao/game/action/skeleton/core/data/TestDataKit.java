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

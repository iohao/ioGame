/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.action.skeleton.core;

import com.iohao.game.common.kit.StrKit;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 全局重复路由检测工具
 * <pre>
 *     实际上，如果是按照 COC 原则的项目结构，是不需要这个工具的。
 *     这个工具主要是对多个业务框架中，加载相同的 action 进行检查。
 *
 *     具体查看
 *     https://gitee.com/iohao/iogame/issues/I5IEUJ
 *
 *     如果不是使用框架提供的 bolt-run-one 启动的，需要开发者自己调用
 *     这里的 bolt-run-one 指的是
 *     SimpleRunOne、ClusterSimpleRunOne
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-07-31
 */
@Slf4j
@UtilityClass
public class ActionCommandRegionGlobalCheckKit {

    Map<String, ActionCommandRegions> map = new HashMap<>();
    /** 开启全局检查路由 */
    public boolean check = true;

    public void putActionCommandRegions(String key, ActionCommandRegions actionCommandRegions) {

        if (!check) {
            return;
        }

        if (map.containsKey(key)) {
            return;
        }

        map.put(key, actionCommandRegions);
    }

    /**
     * 全局重复路由检测
     * see https://gitee.com/iohao/iogame/issues/I5IEUJ
     * <p>
     * 这个检测只能用来意思一下的提示
     */
    public void checkGlobalExistSubCmd() {
        if (!check) {
            return;
        }

        Map<Integer, ActionCommand> cmdMap = new HashMap<>(100);

        // 多服单进程下的所有业务框架的命令域管理器
        var actionCommandRegionList = map
                .values()
                .parallelStream()
                .flatMap(ActionCommandRegions::streamActionCommandRegion)
                .toList();

        log.info("actionCommandRegionList.size() : {} {}",
                map.keySet(),
                actionCommandRegionList.size());


        for (ActionCommandRegion actionCommandRegion : actionCommandRegionList) {
            // 命令域下的路由 action
            for (ActionCommand actionCommand : actionCommandRegion.values()) {
                // 路由信息
                CmdInfo cmdInfo = actionCommand.getCmdInfo();

                int cmdMerge = cmdInfo.getCmdMerge();
                // 如果是重复路由，就抛异常
                if (cmdMap.containsKey(cmdMerge)) {

                    String template = """
                            全局重复路由检测，使用了相同的路由，或者多个业务框架中，加载了相同的 action
                            cmd:【{}】下已经存在方法编号 subCmd:【{}】 .请查看: {}
                            """;

                    String message = StrKit.format(template,
                            actionCommandRegion.cmd,
                            cmdInfo.getSubCmd(),
                            actionCommand.getActionControllerClazz()
                    );

                    throw new RuntimeException(message);
                }

                cmdMap.put(cmdMerge, actionCommand);
            }
        }

        // 清空数据，检测完了
        map.clear();
        cmdMap.clear();
    }
}

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

import com.iohao.game.common.kit.StrKit;
import com.iohao.game.common.kit.exception.ThrowKit;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局重复路由检测工具
 * <pre>
 *     实际上，如果是按照 COC 原则的项目结构，是不需要这个工具的。
 *     这个工具主要是对多个业务框架中，加载相同的 action 进行检查。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-07-31
 */
@UtilityClass
public class ActionCommandRegionGlobalCheckKit {

    Map<String, ActionCommandRegions> map = new HashMap<>();

    public void putActionCommandRegions(String key, ActionCommandRegions actionCommandRegions) {

        if (map.containsKey(key)) {
            return;
        }

        map.put(key, actionCommandRegions);
    }

    /**
     * 全局重复路由检测
     */
    public void checkGlobalExistSubCmd() {

        Map<Integer, ActionCommand> cmdMap = new HashMap<>(100);

        // 多服单进程下的所有业务框架的命令域管理器
        var actionCommandRegionList = map
                .values()
                .parallelStream()
                .flatMap(ActionCommandRegions::streamActionCommandRegion)
                .toList();

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

                    ThrowKit.ofRuntimeException(message);
                }

                cmdMap.put(cmdMerge, actionCommand);
            }
        }

        // 清空数据，检测完了
        map.clear();
        cmdMap.clear();
    }
}

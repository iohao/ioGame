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

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Collection;
import java.util.Map;

/**
 * ActionCommand 域
 * <pre>
 *     类似模块的区分，这样可以避免 map 嵌 map 的结构
 *     在代码的阅读上也会清晰很多
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-15
 */
@FieldDefaults(level = AccessLevel.PACKAGE)
public class ActionCommandRegion {
    final int cmd;
    /**
     * <pre>
     *     key: subCmd
     *     value: ActionCommand
     * </pre>
     */
    NonBlockingHashMap<Integer, ActionCommand> subActionCommandMap = new NonBlockingHashMap<>();

    public ActionCommandRegion(int cmd) {
        this.cmd = cmd;
    }

    public boolean containsKey(int subCmd) {
        return this.subActionCommandMap.containsKey(subCmd);
    }

    public void add(ActionCommand subActionCommand) {
        var cmdInfo = subActionCommand.getCmdInfo();

        int subCmd = cmdInfo.getSubCmd();

        this.subActionCommandMap.put(subCmd, subActionCommand);
    }

    /**
     * 得到子路由最大值
     *
     * @return 子路由最大值
     */
    public int getMaxSubCmd() {
        return subActionCommandMap
                .keySet()
                .stream()
                .max(Integer::compareTo)
                .orElse(0)
                ;
    }

    public Collection<ActionCommand> values() {
        return this.subActionCommandMap.values();
    }

    /**
     * 将子路由列表转为数组
     *
     * @return array
     */
    public ActionCommand[] arrayActionCommand() {
        // 子路由最大值
        int subCmdMax = this.getMaxSubCmd() + 1;
        ActionCommand[] subBehaviors = new ActionCommand[subCmdMax];

        for (Map.Entry<Integer, ActionCommand> subEntry : subActionCommandMap.entrySet()) {
            subBehaviors[subEntry.getKey()] = subEntry.getValue();
        }

        return subBehaviors;
    }
}

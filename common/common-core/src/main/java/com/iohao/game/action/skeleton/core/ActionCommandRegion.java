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

import com.iohao.game.action.skeleton.core.doc.JavaClassDocInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Collection;
import java.util.Map;

/**
 * ActionCommand 域，通常与 ActionController 是 1:1 的关系
 *
 * <pre>
 *     类似模块的区分，这样可以避免 map 嵌 map 的结构
 *     在代码的阅读上也会清晰很多
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-15
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PACKAGE)
public final class ActionCommandRegion {
    final int cmd;
    /** actionControllerClazz */
    Class<?> actionControllerClazz;
    /** actionControllerClazz 的源文件信息 */
    JavaClassDocInfo javaClassDocInfo;
    /**
     * <pre>
     *     key: subCmd
     *     value: ActionCommand
     * </pre>
     */
    Map<Integer, ActionCommand> subActionCommandMap = new NonBlockingHashMap<>();

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

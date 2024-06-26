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
package com.iohao.game.action.skeleton.core.doc;

import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.core.CmdInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * action 文档
 *
 * @author 渔民小镇
 * @date 2023-07-13
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ActionDoc {
    final int cmd;
    final Class<?> controllerClazz;
    /**
     * action method
     * <pre>
     *     key: subCmd
     *     value: ActionCommandDoc
     * </pre>
     */
    final Map<Integer, ActionCommandDoc> actionCommandDocMap = new NonBlockingHashMap<>();

    JavaClassDocInfo javaClassDocInfo;

    public ActionDoc(int cmd, Class<?> controllerClazz) {
        this.cmd = cmd;
        this.controllerClazz = controllerClazz;
    }

    public void addActionCommandDoc(ActionCommandDoc actionCommandDoc) {
        int subCmd = actionCommandDoc.getSubCmd();
        this.actionCommandDocMap.put(subCmd, actionCommandDoc);
    }

    public void addActionCommand(ActionCommand actionCommand) {
        CmdInfo cmdInfo = actionCommand.getCmdInfo();
        int subCmd = cmdInfo.getSubCmd();
        if (actionCommandDocMap.containsKey(subCmd)) {
            ActionCommandDoc actionCommandDoc = actionCommandDocMap.get(subCmd);
            actionCommandDoc.setActionCommand(actionCommand);
        }
    }

    public Stream<ActionCommandDoc> stream() {
        return actionCommandDocMap
                .values()
                .stream()
                .sorted(Comparator.comparingInt(ActionCommandDoc::getSubCmd));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ActionDoc that)) {
            return false;
        }

        return cmd == that.cmd && Objects.equals(controllerClazz, that.controllerClazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cmd, controllerClazz);
    }
}

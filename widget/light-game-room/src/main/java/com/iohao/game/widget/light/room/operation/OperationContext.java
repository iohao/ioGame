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
package com.iohao.game.widget.light.room.operation;

import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.widget.light.room.Player;
import com.iohao.game.widget.light.room.Room;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * 操作上下文
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @since 17
 */
@Getter
@Setter
@Accessors(chain = true)
@SuppressWarnings("unchecked")
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class OperationContext {
    /** room */
    Room room;
    /** 当前操作的玩家 */
    Player player;
    /** 具体玩法需要操作的数据，通常由开发者根据游戏业务来定制 */
    Object command;

    FlowContext flowContext;
    /** 玩法操作业务类 */
    OperationHandler operationHandler;

    public long getUserId() {
        return player.getUserId();
    }

    public long getRoomId() {
        return room.getRoomId();
    }

    public <T extends Room> T getRoom() {
        return (T) this.room;
    }

    public <T extends Player> T getPlayer() {
        return (T) this.player;
    }

    public <T> T getCommand() {
        return (T) this.command;
    }

    public void execute() {
        // 玩法操作业务类，将验证与操作分离
        this.operationHandler.verify(this);
        this.operationHandler.process(this);
    }

    private OperationContext() {
    }

    public static OperationContext of() {
        return new OperationContext();
    }
}

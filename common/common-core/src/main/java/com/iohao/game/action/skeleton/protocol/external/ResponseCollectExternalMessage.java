/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.action.skeleton.protocol.external;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 游戏逻辑服访问游戏对外服，同时访问多个游戏对外服 - 响应
 * <pre>
 *     游戏逻辑服访问游戏对外服，因为只有游戏对外服持有这些数据
 *     把多个游戏对外服的结果聚合在一起
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-07-27
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ResponseCollectExternalMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 44170975617598505L;
    /** 由于是调用多个游戏对外服，每条数据来自游戏对外服 */
    List<ResponseCollectExternalItemMessage> messageList;

    /**
     * 是否有成功的
     *
     * @return true 表示在 item 里，其中一个是成功的
     */
    public boolean anySuccess() {

        if (Objects.isNull(this.messageList)) {
            return false;
        }

        for (ResponseCollectExternalItemMessage itemMessage : this.messageList) {
            if (itemMessage.success()) {
                return true;
            }
        }

        return false;
    }

}

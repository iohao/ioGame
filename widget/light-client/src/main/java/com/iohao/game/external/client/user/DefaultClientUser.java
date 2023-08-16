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
package com.iohao.game.external.client.user;

import com.iohao.game.common.kit.attr.AttrOptions;
import com.iohao.game.external.client.InputCommandRegion;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;

/**
 * 客户端的用户（玩家）
 * <pre>
 *     开发者可以通过动态属性来扩展业务，比如可以在动态属性中保存货币、战力值、血条 ...等
 *
 *     也可以通过继承的方式来扩展 ClientUser。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-07-09
 */
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class DefaultClientUser implements ClientUser {
    final AttrOptions options = new AttrOptions();
    /** 通信 channel 用于读写 */
    final ClientUserChannel clientUserChannel = new ClientUserChannel(this);
    final ClientUserInputCommands clientUserInputCommands = new ClientUserInputCommands(clientUserChannel);
    List<InputCommandRegion> inputCommandRegions;

    /** true 已经登录成功 */
    boolean loginSuccess;

    long userId;
    /** 昵称 */
    String nickname;
    String jwt;

    @Override
    public void callbackInputCommandRegion() {
        if (Objects.isNull(this.inputCommandRegions)) {
            return;
        }

        this.inputCommandRegions.forEach(inputCommandRegion -> {
            inputCommandRegion.setClientUser(this);
            inputCommandRegion.loginSuccessCallback();
        });
    }
}

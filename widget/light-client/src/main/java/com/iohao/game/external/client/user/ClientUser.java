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
package com.iohao.game.external.client.user;

import com.iohao.game.common.kit.attr.AttrOptionDynamic;
import com.iohao.game.external.client.InputCommandRegion;

import java.util.List;

/**
 * @author 渔民小镇
 * @date 2023-07-15
 */
public interface ClientUser extends AttrOptionDynamic {

    ClientUserChannel getClientUserChannel();

    ClientUserInputCommands getClientUserInputCommands();

    void setInputCommandRegions(List<InputCommandRegion> inputCommandRegions);

    long getUserId();

    void setUserId(long userId);

    String getNickname();

    void setNickname(String nickname);

    String getJwt();

    void setJwt(String jwt);

    /**
     * 是否活跃
     *
     * @return true 表示玩家活跃
     */
    boolean isActive();

    /**
     * 登录成功后，将会调用 {@link  InputCommandRegion#loginSuccessCallback()} 方法
     * <pre>
     *     需要开发者主动调用触发，一般在登录模拟请求的回调中主动的调用。
     * </pre>
     */
    void callbackInputCommandRegion();
}

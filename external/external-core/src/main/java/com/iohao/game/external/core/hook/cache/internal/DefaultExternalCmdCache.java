/*
 * ioGame
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
package com.iohao.game.external.core.hook.cache.internal;

import com.iohao.game.action.skeleton.protocol.BarMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.external.core.hook.cache.CmdCacheOption;
import com.iohao.game.external.core.hook.cache.ExternalCmdCache;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * 框架内置的缓存默认实现类
 *
 * @author 渔民小镇
 * @date 2023-12-15
 * @deprecated 请使用 {@link ExternalCmdCache#of()}
 */
@Deprecated
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class DefaultExternalCmdCache implements ExternalCmdCache {
    final ExternalCmdCache externalCmdCache = ExternalCmdCache.of();

    @Override
    public BarMessage getCache(BarMessage message) {
        return this.externalCmdCache.getCache(message);
    }

    @Override
    public void addCacheData(ResponseMessage responseMessage) {
        this.externalCmdCache.addCacheData(responseMessage);
    }

    @Override
    public void setCmdCacheOption(CmdCacheOption option) {
        externalCmdCache.setCmdCacheOption(option);
    }

    @Override
    public CmdCacheOption getCmdCacheOption() {
        return externalCmdCache.getCmdCacheOption();
    }

    @Override
    public void addCmd(int cmd, CmdCacheOption option) {
        this.externalCmdCache.addCmd(cmd, option);
    }

    @Override
    public void addCmd(int cmd, int subCmd, CmdCacheOption option) {
        this.externalCmdCache.addCmd(cmd, subCmd, option);
    }
}

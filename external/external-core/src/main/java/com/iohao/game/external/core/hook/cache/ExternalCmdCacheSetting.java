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
package com.iohao.game.external.core.hook.cache;

/**
 * 游戏对外服缓存配置接口
 *
 * @author 渔民小镇
 * @date 2023-07-02
 */
public interface ExternalCmdCacheSetting {

    /**
     * 设置游戏对外服缓存默认配置
     *
     * @param option 配置
     */
    void setCmdCacheOption(CmdCacheOption option);

    /**
     * 获取游戏对外服缓存默认配置
     *
     * @return 配置
     */
    CmdCacheOption getCmdCacheOption();

    /**
     * 添加路由范围缓存，指定配置
     * <pre>
     *     假设添加了主路由为 1 的值。游戏对外服会将主路由为 1 下的所有子路由的数据都做缓存。
     *
     *     比如 1-1、1-2、1-N ，即使你没有配置这些子路由相关的缓存，也是会生效的。
     * </pre>
     *
     * @param cmd            主路由
     * @param cmdCacheOption 配置
     */
    void addCmd(int cmd, CmdCacheOption cmdCacheOption);

    /**
     * 添加路由范围缓存，使用默认配置
     * <pre>
     *     假设添加了主路由为 1 的值。游戏对外服会将主路由为 1 下的所有子路由的数据都做缓存。
     *
     *     比如 1-1、1-2、1-N ，即使你没有配置这些子路由相关的缓存，也是会生效的。
     * </pre>
     *
     * @param cmd 主路由
     */
    default void addCmd(int cmd) {
        this.addCmd(cmd, getCmdCacheOption());
    }

    /**
     * 添加路由缓存，指定配置
     *
     * @param cmd            主路由
     * @param subCmd         子路由
     * @param cmdCacheOption 配置
     */
    void addCmd(int cmd, int subCmd, CmdCacheOption cmdCacheOption);

    /**
     * 添加路由缓存，使用默认配置
     *
     * @param cmd    主路由
     * @param subCmd 子路由
     */
    default void addCmd(int cmd, int subCmd) {
        this.addCmd(cmd, subCmd, getCmdCacheOption());
    }
}

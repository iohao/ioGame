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

/**
 * 文档访问权限生成
 *
 * @author 渔民小镇
 * @date 2024-09-02
 * @since 21.16
 */
public interface DocumentAccessAuthentication {
    /**
     * 拒绝生成的路由文档，当返回值为 true 时，将不会生成该路由对应的文档
     *
     * @param cmdMerge 路由
     * @return true 表示不会生成该路由对应的文档
     */
    boolean reject(int cmdMerge);
}

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
package com.iohao.game.external.core.aware;

import com.iohao.game.external.core.ExternalCoreSetting;

/**
 * ExternalCoreSetting Aware
 *
 * @author 渔民小镇
 * @date 2023-05-05
 */
public interface ExternalCoreSettingAware {
    /**
     * 框架会调用此方法，将 ExternalCoreSetting 对象传入
     *
     * @param externalCoreSetting externalCoreSetting
     */
    void setExternalCoreSetting(ExternalCoreSetting externalCoreSetting);
}

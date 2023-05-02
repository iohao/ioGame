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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.core.codec.DataCodec;

/**
 * 业务框架全局配置
 *
 * @author 渔民小镇
 * @date 2022-11-24
 */
public class IoGameGlobalSetting {

    /**
     * 设置
     *
     * @param dataCodec
     */
    public static void setDataCodec(DataCodec dataCodec) {
        DataCodecKit.setDataCodec(dataCodec);
    }

    private IoGameGlobalSetting() {
    }

    @Deprecated
    public static IoGameGlobalSetting me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final IoGameGlobalSetting ME = new IoGameGlobalSetting();
    }
}

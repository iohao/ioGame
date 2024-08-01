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
package com.iohao.game.external.client.kit;

import com.iohao.game.common.kit.exception.ThrowKit;
import lombok.experimental.UtilityClass;

import java.util.Scanner;

/**
 * 模拟客户端工具
 *
 * @author 渔民小镇
 * @date 2023-07-04
 */
@UtilityClass
public class ScannerKit {

    final Scanner scanner = new Scanner(System.in);

    /**
     * 当开启关闭控制台输入配置时，将不执行 runnable
     *
     * @param runnable runnable
     */
    public void log(Runnable runnable) {
        if (ClientUserConfigs.closeScanner) {
            return;
        }

        runnable.run();
    }

    /**
     * 控制台输入 String 值
     * <pre>
     *     如果开启了关闭了控制台输入的配置，将使用 value 做为默认值返回
     * </pre>
     *
     * @param defaultValue 当开启了关闭了控制台输入，value 将作为默认值返回
     * @return String value
     */
    public String nextLine(String defaultValue) {
        if (ClientUserConfigs.closeScanner) {
            return defaultValue;
        }

        return nextLine();
    }

    /**
     * 控制台输入 String 值
     *
     * @return String value
     */
    public String nextLine() {
        if (ClientUserConfigs.closeScanner) {
            ThrowKit.ofRuntimeException("不支持控制台输入");
        }

        return scanner.nextLine();
    }

    /**
     * 控制台输入 long 值
     * <pre>
     *     如果开启了关闭了控制台输入的配置，将使用 value 做为默认值返回
     * </pre>
     *
     * @param defaultValue 当开启了关闭了控制台输入，value 将作为默认值返回
     * @return long value
     */
    public long nextLong(long defaultValue) {
        if (ClientUserConfigs.closeScanner) {
            return defaultValue;
        }

        return nextLong();
    }

    /**
     * 控制台输入 long 值
     *
     * @return long value
     */
    public long nextLong() {
        if (ClientUserConfigs.closeScanner) {
            ThrowKit.ofRuntimeException("不支持控制台输入");
        }

        String s = ScannerKit.scanner.nextLine();
        return Long.parseLong(s);
    }

    /**
     * 控制台输入 int 值
     * <pre>
     *     如果开启了关闭了控制台输入的配置，将使用 value 做为默认值返回
     * </pre>
     *
     * @param defaultValue 当开启了关闭了控制台输入，value 将作为默认值返回
     * @return int value
     */
    public int nextInt(int defaultValue) {
        if (ClientUserConfigs.closeScanner) {
            return defaultValue;
        }

        return nextInt();
    }

    /**
     * 控制台输入 int 值
     *
     * @return int value
     */
    public int nextInt() {
        if (ClientUserConfigs.closeScanner) {
            ThrowKit.ofRuntimeException("不支持控制台输入");
        }

        String s = ScannerKit.scanner.nextLine();
        return Integer.parseInt(s);
    }
}

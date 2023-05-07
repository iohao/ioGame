/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General  License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General  License for more details.
 *
 * You should have received a copy of the GNU General  License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.common.internal;


import com.iohao.game.common.kit.ClassScanner;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Predicate;

/**
 * BootItem 配置项工具
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public class BootItemConfigKit {
    static final Logger log = IoGameLoggerFactory.getLoggerCommonStdout();

    /**
     * 加载配置
     * <pre>
     *     这里参数使用类,而没有使用 str 的包路径是因为:
     *     如果 str 所在的配置项变更了路径, 会导致这里加载不到(除非手动改这个 str, 毕竟有时会忘记改参数)
     *     所以这里使用类来当做参数, 毕竟类是一定存在的, str 的包缺不一定存在, 相当于做一个代码级别的验证.
     * </pre>
     *
     * @param packClazz 任意 class 都可以, 会扫描该 class 包下面的所有 class, 并加载
     */
    public static void loadBootItemConfig(Class<? extends BootItemConfig> packClazz) {
        // 过滤条件
        Predicate<Class<?>> predicate = BootItemConfig.class::isAssignableFrom;
        // 扫描路径
        String packagePath = packClazz.getPackageName();
        ClassScanner classScanner = new ClassScanner(packagePath, predicate);

        List<Class<?>> classList = classScanner.listScan();
        for (Class<?> clazz : classList) {
            try {
                BootItemConfig itemConfig = (BootItemConfig) clazz.getDeclaredConstructor().newInstance();
                itemConfig.config();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}

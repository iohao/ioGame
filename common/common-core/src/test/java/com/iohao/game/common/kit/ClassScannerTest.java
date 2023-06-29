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
package com.iohao.game.common.kit;

import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Predicate;

public class ClassScannerTest {
    Logger log = IoGameLoggerFactory.getLogger("Stdout");

    //    @Test
    public void scan() {
        Predicate<Class<?>> predicateFilter = (clazz) -> {
            ActionController annotation = clazz.getAnnotation(ActionController.class);
            return annotation != null;
        };

        String packagePath = "com.iohao.game";
        packagePath = "com.iohao.game.action.skeleton.core.action";
        ClassScanner scanner = new ClassScanner(packagePath, predicateFilter);

        List<Class<?>> classList = scanner.listScan();

        for (Class<?> clazz : classList) {
            log.info("clazz: {}", clazz);
        }
    }

    @Test
    public void test() {
        log.info("hello ioGame {}", "miss");
        String title = "!~ @|CYAN ======================== action ========================= |@ ~!";
//        AnsiConsole.systemInstall();
        System.out.println("Hello World");
        AnsiConsole.out().println("Hello World");
        AnsiConsole.out().println(title);

        System.out.println(Ansi.ansi().eraseScreen().render(title));

        Ansi render = Ansi.ansi().eraseScreen().render(title);
        System.out.println(render.eraseScreen().reset());
    }

}
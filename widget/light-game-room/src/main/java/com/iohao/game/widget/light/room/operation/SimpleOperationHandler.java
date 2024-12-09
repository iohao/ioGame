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
package com.iohao.game.widget.light.room.operation;

/**
 * 执行任务{@link Runnable}，任务必须放到 {@link OperationContext#setCommand(Object)} 中。
 *
 * @author 渔民小镇
 * @date 2024-12-09
 * @since 21.23
 */
public final class SimpleOperationHandler implements OperationHandler {
    @Override
    public void process(PlayerOperationContext context) {
        if (context.getCommand() instanceof Runnable runnable) {
            runnable.run();
        }
    }

    private SimpleOperationHandler() {
    }

    public static SimpleOperationHandler me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final SimpleOperationHandler ME = new SimpleOperationHandler();
    }
}
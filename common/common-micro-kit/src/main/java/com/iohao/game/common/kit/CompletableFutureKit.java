/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
package com.iohao.game.common.kit;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * CompletableFuture Kit
 *
 * @author 渔民小镇
 * @date 2022-07-27
 */
@UtilityClass
public class CompletableFutureKit {
    static final CompletableFuture<?>[] EMPTY_ARRAY = new CompletableFuture[0];

    /**
     * 并行调用多个 CompletableFuture 结果
     *
     * @param futures futures
     * @param <T>     t
     * @return 结果集
     */
    public <T> List<T> sequence(List<CompletableFuture<T>> futures) {
        // 组合处理 allOf
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(EMPTY_ARRAY));

        return allDoneFuture.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        ).join();

        // see https://nurkiewicz.com/2013/05/java-8-completablefuture-in-action.html
    }
}

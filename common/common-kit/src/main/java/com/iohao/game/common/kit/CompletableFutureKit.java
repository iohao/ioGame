/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

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
package com.iohao.game.common.kit.trace;

import lombok.experimental.UtilityClass;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Trace 相关工具
 *
 * @author 渔民小镇
 * @date 2023-12-20
 */
@UtilityClass
public class TraceKit {
    final Map<String, TraceIdSupplier> traceIdSupplierMap = new NonBlockingHashMap<>();
    TraceIdSupplier defaultTraceIdSupplier = new SimpleTraceIdSupplier();
    public final String traceName = "ioGameTraceId";

    public void setDefaultTraceIdSupplier(TraceIdSupplier traceIdSupplier) {
        TraceKit.defaultTraceIdSupplier = traceIdSupplier;
    }

    public void putTraceIdSupplier(String name, TraceIdSupplier traceIdSupplier) {
        traceIdSupplierMap.putIfAbsent(name, traceIdSupplier);
    }

    public String newTraceId(String name) {
        return traceIdSupplierMap.getOrDefault(name, defaultTraceIdSupplier).get();
    }

    public String newTraceId() {
        return defaultTraceIdSupplier.get();
    }

    private final class SimpleTraceIdSupplier implements TraceIdSupplier {
        final AtomicLong id = new AtomicLong(System.currentTimeMillis());

        @Override
        public String get() {
            return Long.toString(id.getAndIncrement());
        }
    }
}

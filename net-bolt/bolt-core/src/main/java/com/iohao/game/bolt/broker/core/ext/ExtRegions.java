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
package com.iohao.game.bolt.broker.core.ext;

import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;

/**
 * @author 渔民小镇
 * @date 2022-05-30
 */
public class ExtRegions {

    Map<Integer, ExtRegion> regionMap = new NonBlockingHashMap<>();

    public void add(ExtRegion extRegion) {
        int extHash = extRegion.getExtHash();
        this.regionMap.put(extHash, extRegion);
    }

    public ExtRegion getExtRegion(int extHash) {
        return this.regionMap.get(extHash);
    }

    private ExtRegions() {

    }

    public static ExtRegions me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final ExtRegions ME = new ExtRegions();
    }
}

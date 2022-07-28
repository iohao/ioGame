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
package com.iohao.game.bolt.broker.client.external.ext;

import com.iohao.game.bolt.broker.client.external.ext.impl.ExistUserExternalBizRegion;
import com.iohao.game.bolt.broker.client.external.ext.impl.ForcedOfflineExternalBizRegion;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;

/**
 * 对外服扩展的业务域集合
 *
 * @author 渔民小镇
 * @date 2022-07-27
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExternalBizRegions {

    /**
     * <pre>
     *     key : 业务码
     *     value : 对应的业务处理器
     * </pre>
     */
    Map<Integer, ExternalBizRegion> regionMap = new NonBlockingHashMap<>();

    public void add(ExternalBizRegion externalBizRegion) {
        int bizCode = externalBizRegion.getBizCode();

        if (this.regionMap.containsKey(bizCode)) {
            throw new RuntimeException("重复添加已经存在的业务码:" + bizCode);
        }

        this.regionMap.put(bizCode, externalBizRegion);
    }

    public ExternalBizRegion getExternalRegion(int bizCode) {
        return this.regionMap.get(bizCode);
    }

    private ExternalBizRegions() {
        // 框架默认提供的业务类
        this.add(ExistUserExternalBizRegion.me());
        this.add(ForcedOfflineExternalBizRegion.me());
    }

    public static ExternalBizRegions me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final ExternalBizRegions ME = new ExternalBizRegions();
    }
}

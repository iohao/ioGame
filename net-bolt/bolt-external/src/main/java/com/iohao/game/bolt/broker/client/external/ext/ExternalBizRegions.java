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
package com.iohao.game.bolt.broker.client.external.ext;

import com.iohao.game.bolt.broker.client.external.ext.impl.AttachmentExternalBizRegion;
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
        this.add(new ExistUserExternalBizRegion());
        this.add(new ForcedOfflineExternalBizRegion());
        this.add(new AttachmentExternalBizRegion());
    }

    public static ExternalBizRegions me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final ExternalBizRegions ME = new ExternalBizRegions();
    }
}

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
package com.iohao.game.external.core.broker.client.ext;

import com.iohao.game.common.kit.exception.ThrowKit;
import com.iohao.game.external.core.broker.client.ext.impl.*;
import lombok.experimental.UtilityClass;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;

/**
 * 对外服扩展的业务域集合
 *
 * @author 渔民小镇
 * @date 2023-02-21
 */
@UtilityClass
public final class ExternalBizRegions {

    /**
     * <pre>
     *     key : 业务码
     *     value : 对应的业务处理器
     * </pre>
     */
    Map<Integer, ExternalBizRegion> regionMap = new NonBlockingHashMap<>();

    static {
        // 框架默认提供的业务类
        add(new ExistUserExternalBizRegion());
        add(new ForcedOfflineExternalBizRegion());
        add(new AttachmentExternalBizRegion());
        add(new UserHeadMetadataExternalBizRegion());
    }

    public void add(ExternalBizRegion externalBizRegion) {
        int bizCode = externalBizRegion.getBizCode();

        if (regionMap.containsKey(bizCode)) {
            ThrowKit.ofRuntimeException("重复添加已经存在的业务码:" + bizCode);
        }

        regionMap.put(bizCode, externalBizRegion);
    }

    public ExternalBizRegion getExternalRegion(int bizCode) {
        return regionMap.get(bizCode);
    }
}

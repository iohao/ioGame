/*
 * ioGame
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.external.core.hook.cache.internal;

import com.iohao.game.action.skeleton.protocol.BarMessage;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.external.core.hook.cache.CmdCacheOption;
import com.iohao.game.external.core.kit.ExternalKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.Objects;

/**
 * 路由 action 缓存
 *
 * @author 渔民小镇
 * @date 2023-12-15
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
final class CmdActionCache {
    /**
     * 缓存数据
     * <pre>
     *     key : requestParam hashId。以请求时的参数 hash 后作为 key
     *     value : cache data。对应的缓存数据
     * </pre>
     */
    final Map<Integer, CacheNode> cacheDataMap = new NonBlockingHashMap<>();
    @Getter
    final CmdCacheOption cmdCacheOption;

    CmdActionCache(CmdCacheOption cmdCacheOption) {
        this.cmdCacheOption = cmdCacheOption;
    }

    /**
     * 得到缓存的业务数据
     *
     * @param message message
     * @return response data
     */
    byte[] getCacheData(BarMessage message) {

        byte[] data = message.getData();
        int cacheCondition = ExternalKit.getCacheCondition(data);
        CacheNode cacheNode = cacheDataMap.get(cacheCondition);

        if (Objects.isNull(cacheNode)) {
            // 当没有找到缓存时，将请求的具体业务参数作为缓存条件，用于后续处理。see ResponseMessageExternalProcessor
            HeadMetadata headMetadata = message.getHeadMetadata();
            headMetadata.setCacheCondition(cacheCondition);
            return null;
        }

        return cacheNode.cacheData;
    }

    void addCacheData(ResponseMessage responseMessage) {
        HeadMetadata headMetadata = responseMessage.getHeadMetadata();
        int cacheCondition = headMetadata.getCacheCondition();
        if (cacheCondition == 0) {
            return;
        }

        int size = this.cacheDataMap.size();
        int cacheLimit = this.cmdCacheOption.getCacheLimit();
        if (size >= cacheLimit) {
            // 如果缓存已经达到配置的上限，则不将数据添加到缓存中。
            return;
        }

        // 这里的 data 有可能为 null，后续版本中可以添加一些缓存击穿相关的防范，现阶段不着急。
        byte[] data = responseMessage.getData();
        int expireCheckTime = (int) this.cmdCacheOption.getExpireCheckTime().getSeconds();
        int expireTimeCount = (int) this.cmdCacheOption.getExpireTime().getSeconds();
        // 新增缓存 Node
        CacheNode cacheNode = new CacheNode(expireCheckTime, data);
        cacheNode.expireTimeCount = expireTimeCount;

        // 将数据添加到缓存中
        this.cacheDataMap.put(cacheCondition, cacheNode);
    }

    void expireMonitor() {
        if (this.cacheDataMap.isEmpty()) {
            return;
        }

        // 如果缓存到期，移除缓存
        var iterator = this.cacheDataMap.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();

            CacheNode cacheNode = entry.getValue();
            if (cacheNode.expire()) {
                iterator.remove();
            }
        }
    }

    private static final class CacheNode {
        final int expireCheckTime;
        final byte[] cacheData;
        int expireTimeCount;

        CacheNode(int expireCheckTime, byte[] cacheData) {
            this.expireCheckTime = expireCheckTime;
            this.cacheData = cacheData;
        }

        boolean expire() {
            expireTimeCount -= expireCheckTime;
            return expireTimeCount <= 0;
        }
    }
}

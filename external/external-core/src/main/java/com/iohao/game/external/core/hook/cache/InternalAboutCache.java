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
package com.iohao.game.external.core.hook.cache;

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.protocol.BarMessage;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.common.kit.MoreKit;
import com.iohao.game.common.kit.concurrent.TaskKit;
import com.iohao.game.external.core.kit.ExternalKit;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 框架内置的缓存默认实现类
 *
 * @author 渔民小镇
 * @date 2024-09-16
 * @since 21.17
 */
final class SimpleExternalCmdCache implements ExternalCmdCache {

    CmdCacheOption cmdCacheOption = CmdCacheOption.newBuilder().build();

    @Override
    public void setCmdCacheOption(CmdCacheOption option) {
        this.cmdCacheOption = option;
    }

    @Override
    public CmdCacheOption getCmdCacheOption() {
        return this.cmdCacheOption;
    }

    @Override
    public void addCmd(int cmd, CmdCacheOption option) {
        CmdCacheRegion cmdCacheRegion = CmdCacheRegions.getCmdCacheRegion(cmd);
        cmdCacheRegion.setRange(true);
        cmdCacheRegion.setCmdCacheOption(option);
    }

    @Override
    public void addCmd(int cmd, int subCmd, CmdCacheOption option) {
        int cmdMerge = CmdKit.merge(cmd, subCmd);
        CmdCacheRegion cmdCacheRegion = CmdCacheRegions.getCmdCacheRegion(cmd);
        cmdCacheRegion.addCmdCache(cmdMerge, option);
    }

    @Override
    public BarMessage getCache(BarMessage message) {

        HeadMetadata headMetadata = message.getHeadMetadata();
        CmdInfo cmdInfo = headMetadata.getCmdInfo();
        CmdActionCache cmdCache = CmdCacheRegions.getCmdActionCache(cmdInfo);

        if (Objects.isNull(cmdCache)) {
            // 表示没有对路由做缓存配置
            return null;
        }

        byte[] responseCacheData = cmdCache.getCacheData(message);
        if (Objects.isNull(responseCacheData)) {
            return null;
        }

        // 当响应的缓存数据存在时，将缓存数据设置到 data 中；可避免 new、序列化 ...等操作。
        message.setData(responseCacheData);
        return message;
    }

    @Override
    public void addCacheData(ResponseMessage responseMessage) {
        HeadMetadata headMetadata = responseMessage.getHeadMetadata();
        CmdInfo cmdInfo = headMetadata.getCmdInfo();
        CmdActionCache cmdCache = CmdCacheRegions.getCmdActionCache(cmdInfo);

        // 没有配置缓存，不做处理
        if (Objects.isNull(cmdCache)) {
            return;
        }

        cmdCache.addCacheData(responseMessage);
    }
}

/**
 * 路由 action 缓存
 *
 * @author 渔民小镇
 * @date 2023-12-15
 */
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
        this.cacheDataMap.entrySet().removeIf(entry -> entry.getValue().expire());
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

/**
 * 缓存域，同一主路由下的。
 *
 * @author 渔民小镇
 * @date 2023-12-15
 */
@Setter
final class CmdCacheRegion {
    /**
     * 路由数据缓存对象
     * <pre>
     *     key : cmdMerge
     *     value : CmdCache
     * </pre>
     */
    final Map<Integer, CmdActionCache> cmdCacheMap = new NonBlockingHashMap<>();
    /** 是否开启范围缓存 */
    boolean range;
    CmdCacheOption cmdCacheOption;

    /**
     * 得到 CmdMergeCache，如果不存在则表示没有做相关的缓存配置
     *
     * @param cmdMerge cmdMerge
     * @return CmdMergeCache
     */
    CmdActionCache getCmdCache(int cmdMerge) {

        var cmdActionCache = this.cmdCacheMap.get(cmdMerge);
        if (Objects.nonNull(cmdActionCache)) {
            return cmdActionCache;
        }

        // 如果开启了范围缓存，即使没有显示的配置，也会生成缓存对象
        return range ? addCmdCache(cmdMerge, this.cmdCacheOption) : null;
    }

    CmdActionCache addCmdCache(int cmdMerge, CmdCacheOption cmdCacheOption) {

        var cmdActionCache = this.cmdCacheMap.get(cmdMerge);
        if (Objects.nonNull(cmdActionCache)) {
            return cmdActionCache;
        }

        var cache = MoreKit.putIfAbsent(this.cmdCacheMap, cmdMerge, new CmdActionCache(cmdCacheOption));
        // 缓存过期时间检测
        extractedExpire(cache);
        return cache;
    }

    private void extractedExpire(CmdActionCache cmdActionCache) {
        TaskKit.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) {
                // 开启过期检测
                cmdActionCache.expireMonitor();

                CmdCacheOption option = cmdActionCache.getCmdCacheOption();
                long delay = option.getExpireCheckTime().getSeconds();

                TaskKit.newTimeout(this, delay, TimeUnit.SECONDS);
            }
        }, 3, TimeUnit.SECONDS);
    }
}

/**
 * 缓存域管理
 *
 * @author 渔民小镇
 * @date 2023-12-15
 */
@UtilityClass
final class CmdCacheRegions {
    /**
     * <pre>
     *     key : cmd
     *     value : CmdCacheRegion
     * </pre>
     */
    final Map<Integer, CmdCacheRegion> cmdCacheRegionMap = new NonBlockingHashMap<>();

    CmdCacheRegion getCmdCacheRegion(int cmd) {

        CmdCacheRegion cmdCacheRegion = cmdCacheRegionMap.get(cmd);

        // 无锁化
        if (Objects.isNull(cmdCacheRegion)) {
            return MoreKit.putIfAbsent(cmdCacheRegionMap, cmd, new CmdCacheRegion());
        }

        return cmdCacheRegion;
    }

    CmdActionCache getCmdActionCache(CmdInfo cmdInfo) {
        var cmd = cmdInfo.getCmd();
        CmdCacheRegion cmdCacheRegion = CmdCacheRegions.getCmdCacheRegion(cmd);

        var cmdMerge = cmdInfo.getCmdMerge();
        return cmdCacheRegion.getCmdCache(cmdMerge);
    }
}

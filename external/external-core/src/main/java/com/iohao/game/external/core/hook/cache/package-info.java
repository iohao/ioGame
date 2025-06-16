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
/**
 * 游戏对外服 - core - <a href="https://iohao.github.io/game/docs/external/cache">游戏对外服缓存</a>
 *
 * <pre>
 *     我们对业务数据做缓存时，一般的做法是通过 Caffeine、cache2k、ehcache、JetCache 等专业的缓存库，将业务数据缓存在游戏逻辑服中，以实现对业务数据的缓存。
 *
 *     而游戏对外服缓存，可以将一些热点的业务数据缓存在游戏对外服中，玩家每次访问相关路由时，会直接从游戏对外服的内存中取数据。这样可以避免反复请求游戏逻辑服，从而达到性能的超级提升；
 *
 *     当我们把游戏对外服缓存与专业的缓存库做结合时，可以发挥更大的性能效果。因为我们可以将热点数据缓存在游戏对外服中，之后其他玩家访问热点数据时，就不需要去游戏逻辑服中取数据，而是直接在游戏对外服这一环节中就能得到数据了。
 *
 *     游戏对外服缓存的使用方式大概与路由访问权限控制差不多，如果你之前了解过这部分的内容，那么花几分钟就能上手了。
 *
 *     游戏对外服缓存对性能有着巨大的提升，主要体现在几个方面
 *     1. 当玩家访问缓存数据时，响应更快了，因为请求链更少了。
 *     2. 直接在游戏对外服中取数据，无需将请求传递到游戏逻辑服中，无需对业务数据做序列化操作。
 *     3. 避免请求传递到游戏逻辑服中，节省系统资源。
 *
 *     特点
 *     1. 零学习成本
 *     2. 可快速响应玩家请求。
 *     3. 简化了缓存的使用，即使没有在游戏逻辑服中对这些业务数据做缓存，只要在游戏对外服配置好相关的路由缓存，就能达到缓存的效果。
 *     4. 减少请求传递，同时游戏对外服缓存还可以减少请求的传递，使得业务数据在游戏对外服就能处理，而不需要经过游戏逻辑服；
 *     5. 避免序列化操作，由于路由对应的业务数据是以 byte[] 类型缓存在游戏对外服的，所以从缓存中取得的业务数据，将不再需要序列化（编码）操作了。简单点说，就是不需要将业务对象转换成 byte[] 类型了；
 *     6. 支持条件缓存，同一 action 支持不同的请求参数。
 *     7. 支持路由范围缓存配置
 * </pre>
 * for example
 * <pre>{@code
 *     // 创建框架内置的缓存实现类
 *     var externalCmdCache = ExternalCmdCache.of();
 *     // 添加全局配置中
 *     ExternalGlobalConfig.externalCmdCache = externalCmdCache;
 *
 *     // 即使不设置，框架默认也是这个配置，这里只是展示如何设置默认的缓存配置。
 *     CmdCacheOption defaultOption = CmdCacheOption.newBuilder()
 *             // 缓存过期时间，1 小时
 *             .setExpireTime(Duration.ofHours(1))
 *             // 缓存过期检测时间间隔 5 分钟
 *             .setExpireCheckTime(Duration.ofMinutes(5))
 *             // 同一个 action 的缓存数量上限设置为 256 条
 *             .setCacheLimit(256)
 *             // 构建缓存配置
 *             .build();
 *
 *     // 设置为默认的缓存配置，之后添加的路由缓存都将使用这个缓存配置
 *     externalCmdCache.setCmdCacheOption(defaultOption);
 *
 *     // 添加路由缓存 22-1，使用默认的缓存配置
 *     externalCmdCache.addCmd(CacheCmd.cmd, CacheCmd.cacheHere);
 *
 *     // 新增一个缓存配置对象，对业务做更精细的控制。
 *     CmdCacheOption optionCustom = CmdCacheOption.newBuilder()
 *             // 缓存过期时间 30 秒
 *             .setExpireTime(Duration.ofSeconds(30))
 *             // 缓存过期检测时间间隔 5 秒
 *             .setExpireCheckTime(Duration.ofSeconds(5))
 *             // 构建缓存配置
 *             .build();
 *
 *     // 添加路由缓存，使用自定义缓存配置
 *     externalCmdCache.addCmd(CacheCmd.cmd, CacheCmd.cacheCustom, optionCustom);
 *     externalCmdCache.addCmd(CacheCmd.cmd, CacheCmd.cacheList, optionCustom);
 *
 *     // 添加路由范围缓存，使用默认的缓存配置
 *     externalCmdCache.addCmd(2);
 * }</pre>
 *
 * @author 渔民小镇
 * @date 2023-07-02
 */
package com.iohao.game.external.core.hook.cache;
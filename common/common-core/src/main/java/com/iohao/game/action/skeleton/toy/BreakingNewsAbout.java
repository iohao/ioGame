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
package com.iohao.game.action.skeleton.toy;

import com.iohao.game.common.kit.RandomKit;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * ioGame 启动新闻
 * <pre>
 *     名字来源 Michael Jackson 的歌曲 Breaking News
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-06-10
 */
@UtilityClass
class BreakingNewsKit {

    private BreakingNews breakingNews;

    private BreakingNews getBreakingNews() {
        if (breakingNews == null) {
            if (Locale.getDefault().getLanguage().equals("zh")) {
                breakingNews = new DefaultBreakingNews();
            } else {
                breakingNews = new EnglishBreakingNews();
            }
        }

        return breakingNews;
    }

    List<News> randomNewsList() {
        List<News> news = getBreakingNews().listNews();
        Collections.shuffle(news);

        return news.stream().limit(2).toList();
    }

    News randomAdv() {
        List<News> list = getBreakingNews().listAdv();
        return RandomKit.randomEle(list);
    }

    News randomMainNews() {
        var list = getBreakingNews().listMainNews();
        return RandomKit.randomEle(list);
    }
}

record News(String title, String url) {
    @Override
    public String toString() {
        return String.format("%s - %s", title, url);
    }
}

interface BreakingNews {
    List<News> listMainNews();

    List<News> listAdv();

    List<News> listNews();
}

final class DefaultBreakingNews implements BreakingNews {

    @Override
    public List<News> listMainNews() {
        return List.of(
                new News("ioGame javadoc", "https://iohao.github.io/javadoc"),
                new News("ioGame issues", "https://github.com/iohao/ioGame/issues"),
                new News("ioGame 框架各版本更新日志", "https://www.yuque.com/iohao/game/ab15oe"),
                new News("ioGame 发版本的频率：每月会发 1 ~ 2 个版本，通常在大版本内的升级总是兼容的", "")
        );
    }

    @Override
    public List<News> listAdv() {
        return List.of(
                new News("MMO", "https://www.yuque.com/iohao/game/sw08q89x3x7kiuhx"),
                new News("桌游类、房间类的实战", "https://www.yuque.com/iohao/game/lul9a9t989s0q2t3")
        );
    }

    @Override
    public List<News> listNews() {
        List<News> list = new ArrayList<>();

        list.add(new News("支持者名单", "https://www.yuque.com/iohao/game/backers"));
        list.add(new News("28行代码，做个网页聊天室", "https://www.yuque.com/iohao/game/we9eppym4yno9hq2"));
        list.add(new News("web 转游戏 - 示例理解篇", "https://www.yuque.com/iohao/game/gpzmc8vadn4vl70z"));
        list.add(new News("学习指南", "https://www.yuque.com/iohao/game/oybe5gmz5uk4ldzb"));
        list.add(new News("ioGame 文档阅读指南", "https://www.yuque.com/iohao/game/giqg6r"));

        // 开发常见问题与小技巧
        list.add(new News("全链路调用日志跟踪", "https://www.yuque.com/iohao/game/zurusq"));
        list.add(new News("需要给到游戏前端的（协议与路由解析规则）", "https://www.yuque.com/iohao/game/zfg3ci"));

        // 授权相关、实践类产品
        list.add(new News("项目成本分析", "https://www.yuque.com/iohao/game/gd5l3b0y0h027kcv#aSk5x"));
        list.add(new News("为什么采用授权许可申请？", "https://www.yuque.com/iohao/game/gd5l3b0y0h027kcv"));
        list.add(new News("授权成员的更多权益", "https://www.yuque.com/iohao/game/ruqkacwigfnlk129"));
        list.add(new News("ioGameAdmin 运维监控", "https://www.yuque.com/iohao/game/xwxxcynh9yz0z8w4"));

        list.add(new News("ioGame 诞生、发展", "https://www.yuque.com/iohao/game/mun9gbwzfph3y5vn"));
        list.add(new News("需要给到游戏前端的", "https://www.yuque.com/iohao/game/zfg3ci"));

        // 整体、架构相关
        list.add(new News("架构简介", "https://www.yuque.com/iohao/game/dqf0he"));
        list.add(new News("ioGame 架构多样性", "https://www.yuque.com/iohao/game/zqgdv3g9if8w37vr"));
        list.add(new News("与传统架构的对比", "https://www.yuque.com/iohao/game/cklv8p"));

        list.add(new News("ioGame 请求的处理流程", "https://www.yuque.com/iohao/game/ibwgawdy4al6o389"));
        list.add(new News("ioGame 消息处理流程", "https://www.yuque.com/iohao/game/dugawvczzc9f4ihg"));
        list.add(new News("ioGame 线程相关", "https://www.yuque.com/iohao/game/eixd6x"));
        list.add(new News("单服单进程、多服单进程、多服多进程的启动方式", "https://www.yuque.com/iohao/game/qni8eqlzsxk7gabm"));
        list.add(new News("代码组织与约定", "https://www.yuque.com/iohao/game/keyrxn"));
        list.add(new News("同进程亲和性", "https://www.yuque.com/iohao/game/unp26u"));

        // 游戏逻辑服
        list.add(new News("游戏逻辑服 - 动态绑定游戏逻辑服", "https://www.yuque.com/iohao/game/idl1wm"));
        list.add(new News("游戏逻辑服 - 元信息、附加信息", "https://www.yuque.com/iohao/game/sw1y8u"));

        // 游戏对外服
        list.add(new News("游戏对外服 - 统一协议说明", "https://www.yuque.com/iohao/game/xeokui"));
        list.add(new News("游戏对外服 - 设计", "https://www.yuque.com/iohao/game/wotnhl"));
        list.add(new News("游戏对外服 - 使用", "https://www.yuque.com/iohao/game/ea6geg"));
        list.add(new News("游戏对外服 - 心跳设置与心跳钩子", "https://www.yuque.com/iohao/game/uueq3i"));
        list.add(new News("游戏对外服 - 用户上线、下线钩子", "https://www.yuque.com/iohao/game/hv5qqh"));
        list.add(new News("游戏对外服 - 路由访问权限控制", "https://www.yuque.com/iohao/game/nap5y8p5fevhv99y"));
        list.add(new News("游戏对外服 - 游戏对外服缓存", "https://www.yuque.com/iohao/game/khg23pvbh59a7spm"));
        list.add(new News("游戏对外服 - ws token 鉴权、校验", "https://www.yuque.com/iohao/game/tb1126szmgfu6u55"));
        list.add(new News("游戏对外服 - 内置与可选的 Handler", "https://www.yuque.com/iohao/game/gqvf6cooowpo0ukp"));

        // 通讯方式
        list.add(new News("通讯方式 - 游戏逻辑服之间的交互", "https://www.yuque.com/iohao/game/anguu6"));
        list.add(new News("通讯方式 - 请求同类型多个逻辑服通信结果", "https://www.yuque.com/iohao/game/rf9rb9"));
        list.add(new News("通讯方式 - 获取游戏对外服的数据与扩展", "https://www.yuque.com/iohao/game/ivxsw5"));
        list.add(new News("通讯方式 - 分布式事件总线", "https://www.yuque.com/iohao/game/gmxz33"));

        // 内置工具
        list.add(new News("内置 Kit - TaskKit 是一个任务、时间、延时监听、超时监听...等相结合的一个工具模块", "https://www.yuque.com/iohao/game/gzsl8pg0si1l4bu3"));
        list.add(new News("内置 Kit - 属性监听", "https://www.yuque.com/iohao/game/uqn84q41f58xe5f0"));
        list.add(new News("内置 Kit - 动态属性", "https://www.yuque.com/iohao/game/vfnqpum6hrt23mnf"));
        list.add(new News("内置 Kit - 轻量可控的延时任务", "https://www.yuque.com/iohao/game/nykaacfzg4h1ynii"));

        // 小部件
        list.add(new News("小部件 - 领域事件可解决多人同一业务的并发问题", "https://www.yuque.com/iohao/game/gmfy1k"));
        list.add(new News("小部件 - 压测&模拟客户端请求", "https://www.yuque.com/iohao/game/tc83ud"));
        list.add(new News("小部件 - room 桌游、房间类的扩展模块", "https://www.yuque.com/iohao/game/vtzbih"));

        // 插件相关
        list.add(new News("业务框架 - 插件介绍", "https://www.yuque.com/iohao/game/bsgvzglvlr5tenao"));
        list.add(new News("插件 - DebugInOut 插件", "https://www.yuque.com/iohao/game/pf3sx0"));
        list.add(new News("插件 - action 调用统计插件", "https://www.yuque.com/iohao/game/znapzm1dqgehdyw8"));
        list.add(new News("插件 - 业务线程监控插件", "https://www.yuque.com/iohao/game/zoqabk4gez3bckis"));
        list.add(new News("插件 - 各时间段调用统计插件", "https://www.yuque.com/iohao/game/umzk2d6lovo4n9gz"));
        list.add(new News("插件 - 全链路调用日志跟踪", "https://www.yuque.com/iohao/game/xhvpqy"));

        // 业务框架
        list.add(new News("业务框架 - 简介", "https://www.yuque.com/iohao/game/wiwpwusmktrv35i4"));
        list.add(new News("业务框架 - FlowContext", "https://www.yuque.com/iohao/game/zz8xiz"));
        list.add(new News("业务框架 - 断言 + 异常机制 = 清晰简洁的代码", "https://www.yuque.com/iohao/game/avlo99"));
        list.add(new News("业务框架 - 开启 JSR380 验证规范", "https://www.yuque.com/iohao/game/ghng6g"));
        list.add(new News("业务框架 - 解决协议碎片", "https://www.yuque.com/iohao/game/ieimzn"));

        return list;
    }
}

final class EnglishBreakingNews implements BreakingNews {

    @Override
    public List<News> listMainNews() {
        return List.of(
                new News("ioGame javadoc", "https://iohao.github.io/javadoc"),
                new News("ioGame issues", "https://github.com/iohao/ioGame/issues"),
                new News("ioGame version log", "https://www.yuque.com/iohao/game/ab15oe"),
                new News("Releases: 1 to 2 versions are released every month, and upgrades within a major version are always compatible, such as 21.1 is upgraded to any higher version 21.x", "")
        );
    }

    @Override
    public List<News> listAdv() {
        return List.of(
                new News("MMO", "https://www.yuque.com/iohao/game/sw08q89x3x7kiuhx"),
                new News("Room games in action", "https://www.yuque.com/iohao/game/lul9a9t989s0q2t3")
        );
    }

    @Override
    public List<News> listNews() {
        List<News> list = new ArrayList<>();

        list.add(new News("Supporters List", "https://www.yuque.com/iohao/game/backers"));
        list.add(new News("28 lines of code to make a web chat room", "https://www.yuque.com/iohao/game/we9eppym4yno9hq2"));
        list.add(new News("Web to Game - Example Understanding", "https://www.yuque.com/iohao/game/gpzmc8vadn4vl70z"));
        list.add(new News("ioGame Study Guide", "https://www.yuque.com/iohao/game/oybe5gmz5uk4ldzb"));
        list.add(new News("ioGame Document Reading Guide", "https://www.yuque.com/iohao/game/giqg6r"));

        // 开发常见问题与小技巧
        list.add(new News("Full-link call log tracking", "https://www.yuque.com/iohao/game/zurusq"));
        list.add(new News("Protocol and routing parsing rules that need to be given to the game client", "https://www.yuque.com/iohao/game/zfg3ci"));
        list.add(new News("Need to be given to the game front end", "https://www.yuque.com/iohao/game/zfg3ci"));

        // 授权相关、实践类产品
        list.add(new News("Project cost analysis", "https://www.yuque.com/iohao/game/gd5l3b0y0h027kcv#aSk5x"));

        // 整体、架构相关
        list.add(new News("ioGame - Birth and development", "https://www.yuque.com/iohao/game/mun9gbwzfph3y5vn"));
        list.add(new News("ioGame - Architecture Introduction", "https://www.yuque.com/iohao/game/dqf0he"));
        list.add(new News("ioGame - Architecture Diversity", "https://www.yuque.com/iohao/game/zqgdv3g9if8w37vr"));
        list.add(new News("ioGame - Comparison with traditional architecture", "https://www.yuque.com/iohao/game/cklv8p"));

        list.add(new News("ioGame - Request Processing Flow", "https://www.yuque.com/iohao/game/ibwgawdy4al6o389"));
        list.add(new News("ioGame - Message processing flow", "https://www.yuque.com/iohao/game/dugawvczzc9f4ihg"));
        list.add(new News("ioGame - Thread related", "https://www.yuque.com/iohao/game/eixd6x"));
        list.add(new News("Startup methods for single server single process, multiple servers single process, and multiple servers multiple processes", "https://www.yuque.com/iohao/game/qni8eqlzsxk7gabm"));
        list.add(new News("Code Organization and Conventions", "https://www.yuque.com/iohao/game/keyrxn"));
        list.add(new News("Same-process affinity", "https://www.yuque.com/iohao/game/unp26u"));

        // 游戏逻辑服
        list.add(new News("GameLogicServer - Dynamically bind game logic server", "https://www.yuque.com/iohao/game/idl1wm"));
        list.add(new News("GameLogicServer - Meta information, additional information", "https://www.yuque.com/iohao/game/sw1y8u"));

        // 游戏对外服
        list.add(new News("ExternalLogicServer - Unified Protocol Description", "https://www.yuque.com/iohao/game/xeokui"));
        list.add(new News("ExternalLogicServer - design", "https://www.yuque.com/iohao/game/wotnhl"));
        list.add(new News("ExternalLogicServer - use", "https://www.yuque.com/iohao/game/ea6geg"));
        list.add(new News("ExternalLogicServer - Heartbeat settings and heartbeat hooks", "https://www.yuque.com/iohao/game/uueq3i"));
        list.add(new News("ExternalLogicServer - User online and offline hooks", "https://www.yuque.com/iohao/game/hv5qqh"));
        list.add(new News("ExternalLogicServer - Routing access control", "https://www.yuque.com/iohao/game/nap5y8p5fevhv99y"));
        list.add(new News("ExternalLogicServer - Game cache for external servers", "https://www.yuque.com/iohao/game/khg23pvbh59a7spm"));
        list.add(new News("ExternalLogicServer - ws token Authentication and verification", "https://www.yuque.com/iohao/game/tb1126szmgfu6u55"));
        list.add(new News("ExternalLogicServer - Built-in and optional Handler", "https://www.yuque.com/iohao/game/gqvf6cooowpo0ukp"));

        // 通讯方式
        list.add(new News("Communication - Interaction between game logic servers", "https://www.yuque.com/iohao/game/anguu6"));
        list.add(new News("Communication - Request the communication results of multiple logical servers of the same type", "https://www.yuque.com/iohao/game/rf9rb9"));
        list.add(new News("Communication - Get game data and expansion for external servers", "https://www.yuque.com/iohao/game/ivxsw5"));
        list.add(new News("Communication - Distributed Event Bus", "https://www.yuque.com/iohao/game/gmxz33"));

        // 内置工具
        list.add(new News("Built-in Kit - TaskKit is a tool module that combines tasks, time, delay monitoring, timeout monitoring, etc.", "https://www.yuque.com/iohao/game/gzsl8pg0si1l4bu3"));
        list.add(new News("Built-in Kit - Property monitoring", "https://www.yuque.com/iohao/game/uqn84q41f58xe5f0"));
        list.add(new News("Built-in Kit - Dynamic properties", "https://www.yuque.com/iohao/game/vfnqpum6hrt23mnf"));
        list.add(new News("Built-in Kit - Lightweight and controllable delayed tasks", "https://www.yuque.com/iohao/game/nykaacfzg4h1ynii"));

        // 小部件
        list.add(new News("Widgets - Domain Events - Can solve the concurrency problem of multiple people doing the same business", "https://www.yuque.com/iohao/game/gmfy1k"));
        list.add(new News("Widgets - Stress testing & simulating client requests", "https://www.yuque.com/iohao/game/tc83ud"));
        list.add(new News("Widgets - Room - Extension modules for board games and rooms", "https://www.yuque.com/iohao/game/vtzbih"));

        // 插件相关
        list.add(new News("Business Framework - Plugin Introduction", "https://www.yuque.com/iohao/game/bsgvzglvlr5tenao"));
        list.add(new News("Plugin - DebugInOut", "https://www.yuque.com/iohao/game/pf3sx0"));
        list.add(new News("Plugin - Action call statistics", "https://www.yuque.com/iohao/game/znapzm1dqgehdyw8"));
        list.add(new News("Plugin - Business thread monitoring", "https://www.yuque.com/iohao/game/zoqabk4gez3bckis"));
        list.add(new News("Plugin - Call statistics for each time period", "https://www.yuque.com/iohao/game/umzk2d6lovo4n9gz"));
        list.add(new News("Plugin - Full-link call log tracking", "https://www.yuque.com/iohao/game/xhvpqy"));

        // 业务框架
        list.add(new News("Business Framework - Introduction", "https://www.yuque.com/iohao/game/wiwpwusmktrv35i4"));
        list.add(new News("Business Framework - FlowContext", "https://www.yuque.com/iohao/game/zz8xiz"));
        list.add(new News("Business Framework - Assertions + exceptions = clear and concise code", "https://www.yuque.com/iohao/game/avlo99"));
        list.add(new News("Business Framework - Enable JSR380 validation specification", "https://www.yuque.com/iohao/game/ghng6g"));
        list.add(new News("Business Framework - Resolving protocol fragmentation", "https://www.yuque.com/iohao/game/ieimzn"));

        return list;
    }
}
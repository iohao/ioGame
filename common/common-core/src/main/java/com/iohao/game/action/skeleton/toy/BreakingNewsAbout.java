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
                new News("ioGame 框架各版本更新日志", "https://iohao.github.io/game/docs/version_log"),
                new News("ioGame 发版本的频率：每月会发 1 ~ 2 个版本，通常在大版本内的升级总是兼容的", "")
        );
    }

    @Override
    public List<News> listAdv() {
        return List.of(
                new News("在线体验 demo", "https://a.iohao.com"),
                new News("MMO", "https://iohao.github.io/game/docs/practices/mmo"),
                new News("桌游类、房间类的实战", "https://iohao.github.io/game/docs/practices/room_in_action")
        );
    }

    @Override
    public List<News> listNews() {
        List<News> list = new ArrayList<>();

        list.add(new News("支持者名单", "https://iohao.github.io/game/docs/contribute/backers"));

        // 开发常见问题与小技巧
        list.add(new News("全链路调用日志跟踪", "https://iohao.github.io/game/docs/manual/trace"));

        // 授权相关、实践类产品
        list.add(new News("项目成本分析", "https://iohao.github.io/game/services/cost_analysis"));

        // 整体、架构相关
        list.add(new News("架构简介", "https://iohao.github.io/game/docs/overall/architecture_intro"));
        list.add(new News("ioGame 架构灵活、部署多样性", "https://iohao.github.io/game/docs/overall/deploy_flexible"));
        list.add(new News("与传统架构的对比", "https://iohao.github.io/game/docs/overall/legacy_system"));

        list.add(new News("ioGame 请求的处理流程", "https://iohao.github.io/game/docs/overall/request_processing_procedure"));
        list.add(new News("ioGame 线程相关", "https://iohao.github.io/game/docs/overall/thread_executor"));
        list.add(new News("单服单进程、多服单进程、多服多进程的启动方式", "https://iohao.github.io/game/docs/manual/netty_run_one"));
        list.add(new News("代码组织与约定", "https://iohao.github.io/game/docs/manual_high/code_organization"));
        list.add(new News("同进程亲和性", "https://iohao.github.io/game/docs/manual_high/same_process"));

        // 游戏逻辑服
        list.add(new News("游戏逻辑服 - 动态绑定游戏逻辑服", "https://iohao.github.io/game/docs/manual/binding_logic_server"));
        list.add(new News("游戏逻辑服 - 元信息、附加信息", "https://iohao.github.io/game/docs/manual/flowcontext_attachment"));

        // 游戏对外服
        list.add(new News("游戏对外服 - 统一协议说明", "https://iohao.github.io/game/docs/manual_high/external_message"));
        list.add(new News("游戏对外服", "https://iohao.github.io/game/docs/overall/external_intro"));
        list.add(new News("游戏对外服 - 心跳设置与心跳钩子", "https://iohao.github.io/game/docs/external/idle"));
        list.add(new News("游戏对外服 - 用户上线、下线钩子", "https://iohao.github.io/game/docs/external/user_hook"));
        list.add(new News("游戏对外服 - 路由访问权限控制", "https://iohao.github.io/game/docs/external/access_authentication"));
        list.add(new News("游戏对外服 - 游戏对外服缓存", "https://iohao.github.io/game/docs/external/cache"));
        list.add(new News("游戏对外服 - ws token 鉴权、校验", "https://iohao.github.io/game/docs/external/ws_verify"));
        list.add(new News("游戏对外服 - 内置与可选的 Handler", "https://iohao.github.io/game/docs/external/netty_handler"));

        // 通讯方式
        list.add(new News("通讯方式 - 请求同类型多个逻辑服通信结果", "https://iohao.github.io/game/docs/communication/request_multiple_response"));
        list.add(new News("通讯方式 - 访问游戏对外服与扩展", "https://iohao.github.io/game/docs/communication/external_biz_region"));
        list.add(new News("通讯方式 - 分布式事件总线", "https://iohao.github.io/game/docs/communication/event_bus"));

        // 内置工具
        list.add(new News("内置 Kit - TaskKit 是一个任务、时间、延时监听、超时监听...等相结合的一个工具模块", "https://iohao.github.io/game/docs/kit/task_kit"));
        list.add(new News("内置 Kit - 属性监听", "https://iohao.github.io/game/docs/kit/property_change_listener"));
        list.add(new News("内置 Kit - 轻量可控的延时任务", "https://iohao.github.io/game/docs/kit/delay_task"));

        // 扩展模块
        list.add(new News("扩展模块 - 领域事件可解决多人同一业务的并发问题", "https://iohao.github.io/game/docs/extension_module/domain_event"));
        list.add(new News("扩展模块 - 压测&模拟客户端请求", "https://iohao.github.io/game/docs/extension_module/simulation_client"));
        list.add(new News("扩展模块 - room 桌游、房间类的扩展模块", "https://iohao.github.io/game/docs/extension_module/room"));
        list.add(new News("扩展模块 - sdk-generate-code", "https://iohao.github.io/game/docs/extension_module/generate_code"));

        // 插件相关
        list.add(new News("业务框架 - 插件介绍", "https://iohao.github.io/game/docs/manual/plugin_intro"));
        list.add(new News("插件 - DebugInOut 插件", "https://iohao.github.io/game/docs/core_plugin/action_debug"));
        list.add(new News("插件 - action 调用统计插件", "https://iohao.github.io/game/docs/core_plugin/action_stat"));
        list.add(new News("插件 - 业务线程监控插件", "https://iohao.github.io/game/docs/core_plugin/action_thread_monitor"));
        list.add(new News("插件 - 各时间段调用统计插件", "https://iohao.github.io/game/docs/core_plugin/action_time_range"));
        list.add(new News("插件 - 全链路调用日志跟踪", "https://iohao.github.io/game/docs/core_plugin/action_trace"));

        // 业务框架
        list.add(new News("业务框架 - 简介", "https://iohao.github.io/game/docs/core/framework"));
        list.add(new News("业务框架 - FlowContext", "https://iohao.github.io/game/docs/manual/flowcontext"));
        list.add(new News("业务框架 - 断言 + 异常机制 = 清晰简洁的代码", "https://iohao.github.io/game/docs/manual/assert_game_code"));
        list.add(new News("业务框架 - 开启 JSR380 验证规范", "https://iohao.github.io/game/docs/core/jsr380"));
        list.add(new News("业务框架 - 解决协议碎片", "https://iohao.github.io/game/docs/manual/protocol_fragment"));

        return list;
    }
}

final class EnglishBreakingNews implements BreakingNews {

    @Override
    public List<News> listMainNews() {
        return List.of(
                new News("ioGame javadoc", "https://iohao.github.io/javadoc"),
                new News("ioGame issues", "https://github.com/iohao/ioGame/issues"),
                new News("ioGame version log", "https://iohao.github.io/game/docs/version_log"),
                new News("Releases: 1 to 2 versions are released every month, and upgrades within a major version are always compatible, such as 21.1 is upgraded to any higher version 21.x", "")
        );
    }

    @Override
    public List<News> listAdv() {
        return List.of(
                new News("online demo", "https://a.iohao.com"),
                new News("MMO", "https://iohao.github.io/game/docs/practices/mmo"),
                new News("Room games in action", "https://iohao.github.io/game/docs/practices/room_in_action")
        );
    }

    @Override
    public List<News> listNews() {
        List<News> list = new ArrayList<>();

        list.add(new News("Backers", "https://iohao.github.io/game/docs/contribute/backers"));

        // 开发常见问题与小技巧
        list.add(new News("Full-link call log tracking", "https://iohao.github.io/game/docs/manual/trace"));

        // 授权相关、实践类产品
        list.add(new News("Project cost analysis", "https://iohao.github.io/game/services/cost_analysis"));

        // 整体、架构相关
        list.add(new News("ioGame - Architecture Introduction", "https://iohao.github.io/game/docs/overall/architecture_intro"));
        list.add(new News("ioGame - Architecture Diversity", "https://iohao.github.io/game/docs/overall/deploy_flexible"));
        list.add(new News("ioGame - Comparison with traditional architecture", "https://iohao.github.io/game/docs/overall/legacy_system"));

        list.add(new News("ioGame - Request Processing Flow", "https://iohao.github.io/game/docs/overall/request_processing_procedure"));
        list.add(new News("ioGame - Thread related", "https://iohao.github.io/game/docs/overall/thread_executor"));
        list.add(new News("Startup methods for single server single process, multiple servers single process, and multiple servers multiple processes", "https://iohao.github.io/game/docs/manual/netty_run_one"));
        list.add(new News("Code Organization and Conventions", "https://iohao.github.io/game/docs/manual_high/code_organization"));
        list.add(new News("Same-process affinity", "https://iohao.github.io/game/docs/manual_high/same_process"));

        // 游戏逻辑服
        list.add(new News("GameLogicServer - Dynamically bind game logic server", "https://iohao.github.io/game/docs/manual/binding_logic_server"));
        list.add(new News("GameLogicServer - Meta information, additional information", "https://iohao.github.io/game/docs/manual/flowcontext_attachment"));

        // 游戏对外服
        list.add(new News("ExternalServer - Unified Protocol Description", "https://iohao.github.io/game/docs/manual_high/external_message"));
        list.add(new News("ExternalServer", "https://iohao.github.io/game/docs/overall/external_intro"));
        list.add(new News("ExternalServer - Heartbeat settings and heartbeat hooks", "https://iohao.github.io/game/docs/external/idle"));
        list.add(new News("ExternalServer - User online and offline hooks", "https://iohao.github.io/game/docs/external/user_hook"));
        list.add(new News("ExternalServer - Routing access control", "https://iohao.github.io/game/docs/external/access_authentication"));
        list.add(new News("ExternalServer - Game cache for external servers", "https://iohao.github.io/game/docs/external/cache"));
        list.add(new News("ExternalServer - ws token Authentication and verification", "https://iohao.github.io/game/docs/external/ws_verify"));
        list.add(new News("ExternalServer - Built-in and optional Handler", "https://iohao.github.io/game/docs/external/netty_handler"));

        // 通讯方式
        list.add(new News("Communication - Request the communication results of multiple logical servers of the same type", "https://iohao.github.io/game/docs/communication/request_multiple_response"));
        list.add(new News("Communication - Get game data and expansion for external servers", "https://iohao.github.io/game/docs/communication/external_biz_region"));
        list.add(new News("Communication - Distributed Event Bus", "https://iohao.github.io/game/docs/communication/event_bus"));

        // 内置工具
        list.add(new News("Built-in Kit - TaskKit is a tool module that combines tasks, time, delay monitoring, timeout monitoring, etc.", "https://iohao.github.io/game/docs/kit/task_kit"));
        list.add(new News("Built-in Kit - Property monitoring", "https://iohao.github.io/game/docs/kit/property_change_listener"));
        list.add(new News("Built-in Kit - Lightweight and controllable delayed tasks", "https://iohao.github.io/game/docs/kit/delay_task"));

        // 扩展模块
        list.add(new News("ExtendedModule - Domain Events - Can solve the concurrency problem of multiple people doing the same business", "https://iohao.github.io/game/docs/extension_module/domain_event"));
        list.add(new News("ExtendedModule - Stress testing & simulating client requests", "https://iohao.github.io/game/docs/extension_module/simulation_client"));
        list.add(new News("ExtendedModule - Room - Extension modules for board games and rooms", "https://iohao.github.io/game/docs/extension_module/room"));
        list.add(new News("ExtendedModule - sdk-generate-code", "https://iohao.github.io/game/docs/extension_module/room"));

        // 插件相关
        list.add(new News("Business Framework - Plugin Introduction", "https://iohao.github.io/game/docs/manual/plugin_intro"));
        list.add(new News("Plugin - DebugInOut", "https://iohao.github.io/game/docs/core_plugin/action_debug"));
        list.add(new News("Plugin - Action call statistics", "https://iohao.github.io/game/docs/core_plugin/action_stat"));
        list.add(new News("Plugin - Business thread monitoring", "https://iohao.github.io/game/docs/core_plugin/action_thread_monitor"));
        list.add(new News("Plugin - Call statistics for each time period", "https://iohao.github.io/game/docs/core_plugin/action_time_range"));
        list.add(new News("Plugin - Full-link call log tracking", "https://iohao.github.io/game/docs/core_plugin/action_trace"));

        // 业务框架
        list.add(new News("Business Framework - Introduction", "https://iohao.github.io/game/docs/core/framework"));
        list.add(new News("Business Framework - FlowContext", "https://iohao.github.io/game/docs/manual/flowcontext"));
        list.add(new News("Business Framework - Assertions + exceptions = clear and concise code", "https://iohao.github.io/game/docs/manual/assert_game_code"));
        list.add(new News("Business Framework - Enable JSR380 validation specification", "https://iohao.github.io/game/docs/core/jsr380"));
        list.add(new News("Business Framework - Resolving protocol fragmentation", "https://iohao.github.io/game/docs/manual/protocol_fragment"));

        return list;
    }
}
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
package com.iohao.game.bolt.broker.boot.monitor;

import com.iohao.game.bolt.broker.boot.monitor.kit.OshiKit;
import com.iohao.game.bolt.broker.boot.monitor.message.oshi.MonitorCollect;
import com.iohao.game.bolt.broker.boot.monitor.message.oshi.MonitorCpuInfo;
import com.iohao.game.bolt.broker.boot.monitor.message.oshi.MonitorOperatingSystem;
import com.iohao.game.bolt.broker.core.ext.ExtRegionContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 渔民小镇
 * @date 2022-06-04
 */
@Slf4j
public class MonitorService {
    public MonitorCollect getMonitorCollect(ExtRegionContext extRegionContext) {
        // cpu相关的（使用率、温度）、内存使用率、磁盘（容量、IO）、
        // 硬盘SMART健康状态、逻辑服的数量


        // 操作系统
        MonitorOperatingSystem operatingSystem = OshiKit.getOperatingSystem();
        // cpu相关的
        MonitorCpuInfo cpu = OshiKit.getCpu();

        // 监控数据集
        return new MonitorCollect()
                .setMonitorOperatingSystem(operatingSystem)
                .setMonitorCpuInfo(cpu);
    }

    private MonitorService() {

    }

    public static MonitorService me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final MonitorService ME = new MonitorService();
    }
}

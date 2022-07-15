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
package com.iohao.game.bolt.broker.boot.monitor.kit;

import cn.hutool.core.date.DateUtil;
import cn.hutool.system.HostInfo;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import com.iohao.game.bolt.broker.boot.monitor.mapstruct.MonitorMapstruct;
import com.iohao.game.bolt.broker.boot.monitor.message.oshi.MonitorCpuInfo;
import com.iohao.game.bolt.broker.boot.monitor.message.oshi.MonitorOperatingSystem;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;

/**
 * @author 渔民小镇
 * @date 2022-06-04
 */
@Slf4j
@UtilityClass
public class OshiKit {
    public SystemInfo systemInfo = new SystemInfo();

    public MonitorOperatingSystem getOperatingSystem() {
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        MonitorOperatingSystem os = new MonitorOperatingSystem();
        HostInfo hostInfo = SystemUtil.getHostInfo();
        OsInfo osInfo = SystemUtil.getOsInfo();
        os.setName(operatingSystem.getFamily() + " " + operatingSystem.getVersionInfo().getVersion());
        os.setArch(osInfo.getArch());
        os.setBitness(operatingSystem.getBitness());
        os.setVersion(operatingSystem.getVersionInfo().toString());
        os.setVendor(operatingSystem.getManufacturer());
        os.setHostName(hostInfo.getName());
        os.setHostAddress(hostInfo.getAddress());
        os.setBootTime(DateUtil.format(DateUtil.date(operatingSystem.getSystemBootTime() * 1000L), "yyyy年MM月dd日 HH时mm分ss秒"));
        os.setUptime(DateUtil.formatBetween(operatingSystem.getSystemUptime() * 1000L));
        return os;
    }

    public MonitorCpuInfo getCpu() {

        CpuInfo cpuInfo = OshiUtil.getCpuInfo();

        MonitorCpuInfo monitorCpuInfo = MonitorMapstruct.ME.convert(cpuInfo);


        return monitorCpuInfo;
    }
}

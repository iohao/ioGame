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
package com.iohao.game.bolt.broker.boot.monitor.message.oshi;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 渔民小镇
 * @date 2022-06-04
 */
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonitorOperatingSystem implements Serializable {
    @Serial
    private static final long serialVersionUID = 5571193858808356505L;

    /** OS Name : 操作系统名称 */
    String name;

    /** OS Arch : 操作系统架构 */
    String arch;

    /** OS Bit ness : 操作系统位数(32 or 64) */
    int bitness;

    /** OS Version : 操作系统版本 */
    String version;

    /** OS Name : 操作系统供应商 */
    String vendor;

    /** Host Name : 主机名称 */
    String hostName;

    /** Host Address : 主机Ip */
    String hostAddress;

    /** Os 启动时间 */
    String bootTime;

    /** Os 运行时间 */
    String uptime;
}

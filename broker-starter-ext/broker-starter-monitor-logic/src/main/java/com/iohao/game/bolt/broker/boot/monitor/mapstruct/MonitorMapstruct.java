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
package com.iohao.game.bolt.broker.boot.monitor.mapstruct;

import cn.hutool.system.oshi.CpuInfo;
import com.iohao.game.bolt.broker.boot.monitor.message.oshi.MonitorCpuInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 实体映射工具
 * <pre>
 *     https://mapstruct.org/documentation/stable/reference/html/
 *
 *     bean 相互转换
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-06-04
 */
@Mapper
public interface MonitorMapstruct {
    MonitorMapstruct ME = Mappers.getMapper(MonitorMapstruct.class);

    /**
     * convert
     *
     * @param cpuInfo cpuInfo
     * @return MonitorCpuInfo
     */
    MonitorCpuInfo convert(CpuInfo cpuInfo);
}

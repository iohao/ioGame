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
package com.iohao.game.bolt.broker.boot.monitor.message;

import com.iohao.game.bolt.broker.boot.monitor.message.ext.MonitorServerInfo;
import com.iohao.game.bolt.broker.boot.monitor.message.oshi.MonitorCollect;
import com.iohao.game.common.kit.ToJson;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

/**
 * 监控信息收集器
 *
 * @author 渔民小镇
 * @date 2022-06-04
 */
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonitorCollectMessage implements Serializable, ToJson {
    @Serial
    private static final long serialVersionUID = -3395358802006326327L;
    /** 监控相关的信息 */
    MonitorCollect monitorCollect;
    /** 逻辑服信息收集 */
    MonitorServerInfo monitorServerInfo;
}

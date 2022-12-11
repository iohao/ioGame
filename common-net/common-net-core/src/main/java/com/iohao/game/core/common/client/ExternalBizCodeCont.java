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
package com.iohao.game.core.common.client;

/**
 * <pre>
 *     开发者扩展时，用正数的业务码
 *     框架会从负数开始使用
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-12-11
 */
public interface ExternalBizCodeCont {
    /** 用户（玩家）是否在线，ExistUserExternalBizRegion */
    int existUser = -1;
    /** 强制用户（玩家）下线，ForcedOfflineExternalBizRegion */
    int forcedOffline = -2;

    /** 用户（玩家）的元信息同步，AttachmentExternalBizRegion */
    int attachment = -3;
}

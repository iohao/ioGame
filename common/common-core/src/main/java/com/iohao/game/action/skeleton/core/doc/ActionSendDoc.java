/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.action.skeleton.core.doc;

import com.iohao.game.action.skeleton.annotation.DocActionSend;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * @author 渔民小镇
 * @date 2022-02-01
 */
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActionSendDoc {
    /** 主路由 */
    final int cmd;
    /** 子路由 */
    final int subCmd;
    /** 业务类型 */
    final Class<?> dataClass;
    /** 推送描述 */
    final String description;
    /** true 已经被读取过一次或以上 */
    boolean read;

    public ActionSendDoc(DocActionSend docActionSend) {
        this.cmd = docActionSend.cmd();
        this.subCmd = docActionSend.subCmd();
        this.dataClass = docActionSend.dataClass();
        this.description = docActionSend.description();
    }
}

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
package com.iohao.game.bolt.broker.core.message;

import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

/**
 * 广播消息
 *
 * @author 渔民小镇
 * @date 2022-03-10
 */
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BroadcastMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = -8781053474740658678L;

    /** 广播的消息 */
    ResponseMessage responseMessage;
    /** 接收广播的用户列表 */
    Collection<Long> userIdList;
    /** true 给全体广播 */
    boolean broadcastAll;

}

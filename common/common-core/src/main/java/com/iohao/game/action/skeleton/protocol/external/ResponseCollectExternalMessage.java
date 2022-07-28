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
package com.iohao.game.action.skeleton.protocol.external;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 游戏逻辑服访问游戏对外服，同时访问多个游戏对外服 - 响应
 * <pre>
 *     游戏逻辑服访问游戏对外服，因为只有游戏对外服持有这些数据
 *     把多个游戏对外服的结果聚合在一起
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-07-27
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ResponseCollectExternalMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 44170975617598505L;
    /** 由于是调用多个游戏对外服，每条数据来自游戏对外服 */
    List<ResponseCollectExternalItemMessage> messageList;

    /**
     * 是否有成功的
     *
     * @return true 表示在 item 里，其中一个是成功的
     */
    public boolean anySuccess() {

        if (Objects.isNull(this.messageList)) {
            return false;
        }

        for (ResponseCollectExternalItemMessage itemMessage : this.messageList) {
            if (itemMessage.success()) {
                return true;
            }
        }

        return false;
    }

}

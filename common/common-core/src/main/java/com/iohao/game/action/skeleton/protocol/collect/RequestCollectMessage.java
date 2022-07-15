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
package com.iohao.game.action.skeleton.protocol.collect;

import com.iohao.game.action.skeleton.protocol.RequestMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

/**
 * 模块之间的访问，访问【同类型】的多个逻辑服
 * <pre>
 *     如： 模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据
 *     是把多个相同逻辑服结果聚合在一起
 *
 *     具体的意思可以参考文档中的说明：
 *     https://www.yuque.com/iohao/game/nelwuz#gSdya
 *     https://www.yuque.com/iohao/game/rf9rb9
 *
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCollectMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 4271692369352579162L;
    RequestMessage requestMessage;
}

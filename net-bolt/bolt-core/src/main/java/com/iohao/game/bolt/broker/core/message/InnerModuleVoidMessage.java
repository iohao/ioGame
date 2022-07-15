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
package com.iohao.game.bolt.broker.core.message;

import com.iohao.game.action.skeleton.protocol.RequestMessage;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 模块之间的访问
 * <pre>
 *     如： 模块A 访问 模块B 的某个方法，但是不需要任何返回值
 *
 *     如果需要返回值的，see {@link InnerModuleMessage}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-06-07
 */
@Data
public class InnerModuleVoidMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = -5740054570053626336L;
    RequestMessage requestMessage;
}

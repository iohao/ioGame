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
package com.iohao.game.action.skeleton.core.flow;

/**
 * ActionMethod 结果包装器
 * <pre>
 *     将 action 业务方法产生的结果或者错误码 包装到响应(ResponseMessage)对象中 。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public interface ActionMethodResultWrap {
    /**
     * 包装结果
     * <pre>
     *     将 action 业务方法产生的结果或者错误码 包装到响应(ResponseMessage)对象中 。
     * </pre>
     *
     * @param flowContext flow 上下文
     */
    void wrap(FlowContext flowContext);
}

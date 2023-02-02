/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.action.skeleton.core.flow;

/**
 * 业务框架 flow 上下文 工厂，负责创建 FlowContext
 * <pre>
 *     通过这个工厂，开发者可以自定义 FlowContext 的子类，
 *     通常用于给 FlowContext 子类添加上一些自定义方法
 *
 *     FlowContext 还支持开发者自定义，具体参考
 *     https://www.yuque.com/iohao/game/zz8xiz#sLySn
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-08-20
 */
public interface FlowContextFactory {
    /**
     * 创建业务框架 flow 上下文
     *
     * @return FlowContext
     */
    FlowContext createFlowContext();
}

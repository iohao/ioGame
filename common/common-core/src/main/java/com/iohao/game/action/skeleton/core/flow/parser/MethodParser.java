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
package com.iohao.game.action.skeleton.core.flow.parser;

import com.iohao.game.action.skeleton.core.ActionCommand;

/**
 * action 方法解析：解析方法参数、解析方法返回值
 *
 * @author 渔民小镇
 * @date 2022-06-26
 */
public interface MethodParser {

    /**
     * 得到真实类型
     * <pre>
     *     常规的 java class 是指本身，如：
     *     开发者自定义了一个 StudentPb，在 action 方法上参数声明为 xxx(StudentPb studentPb)
     *     那么这个值就是 StudentPb
     * </pre>
     *
     * <pre>
     *     但由于框架现在内置了一些包装类型，如：
     *     int --> IntPb
     *     List&lt;Integer&gt; --> IntListPb
     *
     *     long --> LongPb
     *     List&lt;Long&gt; --> LongListPb
     *
     *     所以当开发者在 action 方法上参数声明为基础类型时；
     *     如声明为 int 那么这个值将会是 IntPb
     *     如声明为 long 那么这个值将会是 LongPb
     *
     *     如声明为 List&lt;Integer&gt; 那么这个值将会是 IntListPb
     *     包装类型相关的以此类推;
     *
     *     这么做的目的是为了生成文档时，不与前端产生争议，
     *     如果提供给前端的文档显示 int ，或许前端同学会懵B，
     *     当然如果提前与前端同学沟通好这些约定，也不是那么麻烦。
     *     但实际上如果前端是新来接手项目的，碰见这种情况也会小懵，
     *     所以为了避免这些小尬，框架在生成文档时，用基础类型的内置包装类型来表示。
     * </pre>
     *
     * @param methodParamResultInfo methodParamResultInfo
     * @return 真实类型
     */
    Class<?> getActualClazz(ActionCommand.MethodParamResultInfo methodParamResultInfo);

    /**
     * 解析 action 方法参数
     *
     * @param data      data
     * @param paramInfo paramInfo
     * @return 解析后的数据
     */
    Object parseParam(byte[] data, ActionCommand.ParamInfo paramInfo);

    /**
     * 解析 action 结果 （方法返回值）
     *
     * @param actionMethodReturnInfo actionMethodReturnInfo
     * @param methodResult           当前的返回值
     * @return 处理后的返回值
     */
    Object parseResult(ActionCommand.ActionMethodReturnInfo actionMethodReturnInfo, Object methodResult);

    /**
     * 是否自定义的方法解析器
     *
     * @return true 是自定义的
     */
    default boolean isCustomMethodParser() {
        return true;
    }
}

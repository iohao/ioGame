/*
 * ioGame 
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
     *     int --> IntValue
     *     List&lt;Integer&gt; --> IntValueList
     *
     *     long --> LongValue
     *     List&lt;Long&gt; --> LongValueList
     *
     *     所以当开发者在 action 方法上参数声明为基础类型时；
     *     如声明为 int 那么这个值将会是 IntValue
     *     如声明为 long 那么这个值将会是 LongValue
     *
     *     如声明为 List&lt;Integer&gt; 那么这个值将会是 IntValueList
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

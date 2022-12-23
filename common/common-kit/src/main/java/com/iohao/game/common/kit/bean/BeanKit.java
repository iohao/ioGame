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
package com.iohao.game.common.kit.bean;

import cn.hutool.core.bean.BeanUtil;
import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * @author 渔民小镇
 * @date 2022-12-23
 */
@UtilityClass
public class BeanKit {
    /**
     * 将bean的部分属性转换成map<br>
     * 可选拷贝哪些属性值，默认是不忽略值为{@code null}的值的。
     *
     * @param bean       bean
     * @param properties 需要拷贝的属性值，{@code null}或空表示拷贝所有值
     * @return Map
     * @since 5.8.0
     */
    public static Map<String, Object> beanToMap(Object bean, String... properties) {
        return BeanUtil.beanToMap(bean, properties);
    }
}

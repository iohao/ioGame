/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - present double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.common.validation.support;

import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.validation.Validator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;

import java.util.Set;

/**
 * 实现 Jakarta.Validation 数据校验器
 *
 * @author shenjk
 * @date 2022-09-26
 */
public class JakartaValidator implements Validator {

    private final jakarta.validation.Validator validator;

    public JakartaValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    /**
     * 执行校验
     *
     * @param data   校验数据
     * @param groups 分组信息
     * @return 校验不通过的返回信息
     */
    @Override
    public String validate(Object data, Class<?>... groups) {
        Set<ConstraintViolation<Object>> violationSet = validator.validate(data, groups);
        if (CollKit.isEmpty(violationSet)) {
            return null;
        }

        if (!violationSet.isEmpty()) {
            final ConstraintViolation<Object> violation = violationSet.iterator().next();
            String propertyName = violation.getPropertyPath().toString();
            return propertyName + " " + violation.getMessage();
        }

        return null;
    }

    /**
     * 参数类型是否需要验证
     *
     * @param paramClazz 参数类型
     * @return true 这是一个需要验证的参数
     */
    @Override
    public boolean isValidator(Class<?> paramClazz) {
        // 根据 class 得到 bean 描述
        BeanDescriptor beanDescriptor = validator.getConstraintsForClass(paramClazz);
        // bean 的属性上添加的验证注解信息
        Set<PropertyDescriptor> descriptorSet = beanDescriptor.getConstrainedProperties();
        return !descriptorSet.isEmpty();
    }
}

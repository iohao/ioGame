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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.common.kit.CollKit;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.Set;


/**
 * 验证相关，主要用户验证业务参数
 * <pre>
 *     符合 JSR-380标准的校验。这里使用 hibernate-validator
 *
 *     用户需引入validation-api的实现，如：hibernate-validator
 *     注意：hibernate-validator还依赖了javax.el，需自行引入。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-01-16
 */
@UtilityClass
public class ValidatorKit {
    @Setter
    private Validator validator;

    public Validator getValidator() {

        if (Objects.nonNull(validator)) {
            return validator;
        }

        ValidatorFactory validatorFactory = Validation
                .buildDefaultValidatorFactory()
//                .byDefaultProvider()
//                .configure()
                // true  快速失败返回模式    false 普通模式
//                .failFast(true)
//                .buildValidatorFactory()
                ;

        validator = validatorFactory.getValidator();
        return validator;
    }

    public String validate(Object data) {
        // 验证参数
        Set<ConstraintViolation<Object>> violationSet = getValidator().validate(data);
        if (CollKit.isEmpty(violationSet)) {
            return null;
        }

        for (ConstraintViolation<Object> violation : violationSet) {
            String propertyName = violation.getPropertyPath().toString();
            return propertyName + " " + violation.getMessage();
        }

        return null;
    }

    /**
     * 业务方法参数验证
     * <pre>
     *     提前查看参数是否需要验证
     *     如果需要验证的，做个标记
     * </pre>
     *
     * @param setting setting
     * @param builder builder
     */
    void buildValidator(BarSkeletonSetting setting, ActionCommand.Builder builder) {
        if (!setting.validator) {
            // 没开启 JSR380 验证， 不做处理
            return;
        }

        ActionCommand.ParamInfo[] paramInfos = builder.paramInfos;

        if (Objects.isNull(paramInfos) || paramInfos.length == 0) {
            // 方法上没有参数，不做处理
            return;
        }

        for (ActionCommand.ParamInfo paramInfo : paramInfos) {

            if (paramInfo.isExtension()) {
                // 过滤不需要验证的参数
                continue;
            }

            Class<?> paramClazz = paramInfo.paramClazz;

            // 根据 class 得到 bean 描述
            BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass(paramClazz);
            // bean 的属性上添加的验证注解信息
            Set<PropertyDescriptor> descriptorSet = beanDescriptor.getConstrainedProperties();

            if (descriptorSet.isEmpty()) {
                // 表示这是一个不需要验证的参数
                continue;
            }

            // true 这是一个需要验证的参数
            paramInfo.validator = true;
        }
    }
}

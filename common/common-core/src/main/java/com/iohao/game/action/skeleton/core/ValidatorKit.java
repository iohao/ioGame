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

import com.iohao.game.common.validation.Validation;
import com.iohao.game.common.validation.Validator;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.util.Objects;


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

    public Validator getValidator() throws RuntimeException {

        if (Objects.nonNull(validator)) {
            return validator;
        }
        try {
            validator = Validation.getValidator();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return validator;
    }

    public String validate(Object data, Class<?>... groups) {
        // 验证参数
        return getValidator().validate(data, groups);
    }

    /**
     * 参数类型是否需要验证
     *
     * @param paramClazz 参数类型
     * @return true 这是一个需要验证的参数
     */
    boolean isValidator(Class<?> paramClazz) {
        if (getValidator().descriptorIsEmpty(paramClazz)) {
            // 表示这个 class 是一个不需要验证的参数
            return false;
        }
        return true;
    }
}

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
import com.iohao.game.common.validation.Validation;
import com.iohao.game.common.validation.Validator;
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

    public Validator getValidator() throws RuntimeException{

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

    public String validate(Object data) {
        // 验证参数
        return getValidator().validate(data);
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
            if (getValidator().descriptorIsEmpty(paramClazz)) {
                // 表示这是一个不需要验证的参数
                continue;
            }

            // true 这是一个需要验证的参数
            paramInfo.validator = true;
        }
    }
}

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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.common.kit.exception.ThrowKit;
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
            ThrowKit.ofRuntimeException(e);
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
        return getValidator().isValidator(paramClazz);
    }
}

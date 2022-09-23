package com.iohao.game.common.validation.support;

import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.validation.Validator;

import javax.validation.ConstraintViolation;

import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import javax.validation.ValidatorFactory;
import java.util.Set;

public class JavaxValidator implements Validator {

    private final javax.validation.Validator validator;

    public JavaxValidator() {
        ValidatorFactory factory = javax.validation.Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    public String validate(Object data,Class<?>... groups) {
        Set<ConstraintViolation<Object>> violationSet = validator.validate(data,groups);
        if (CollKit.isEmpty(violationSet)) {
            return null;
        }
        if (!violationSet.isEmpty()) {
            final javax.validation.ConstraintViolation<Object> violation = violationSet.iterator().next();
            String propertyName = violation.getPropertyPath().toString();
            return propertyName + " " + violation.getMessage();
        }
        return null;
    }

    @Override
    public boolean descriptorIsEmpty(Class<?> paramClazz) {
        // 根据 class 得到 bean 描述
        BeanDescriptor beanDescriptor = validator.getConstraintsForClass(paramClazz);
        // bean 的属性上添加的验证注解信息
        Set<PropertyDescriptor> descriptorSet = beanDescriptor.getConstrainedProperties();
        return descriptorSet.isEmpty();
    }
}

package com.iohao.game.common.validation.support;

import com.iohao.game.common.validation.Validator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;

import java.util.Collections;
import java.util.Set;

public class JakartaValidator implements Validator {

    private final jakarta.validation.Validator validator;

    public JakartaValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();
    }

    @Override
    public String validate(Object data) {
        Set<ConstraintViolation<Object>> violationSet = validator.validate(data);
        if (violationSet==null || violationSet.isEmpty()) {
            return null;
        }
        for (ConstraintViolation<Object> violation : violationSet) {
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

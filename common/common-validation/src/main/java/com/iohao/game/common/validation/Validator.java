package com.iohao.game.common.validation;

public interface Validator {

    String validate(Object data);

    boolean descriptorIsEmpty(Class<?> paramClazz);
}

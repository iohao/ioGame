package com.iohao.game.common.validation;

public interface Validator {

    String validate(Object data,Class<?>... groups);

    boolean descriptorIsEmpty(Class<?> paramClazz);
}

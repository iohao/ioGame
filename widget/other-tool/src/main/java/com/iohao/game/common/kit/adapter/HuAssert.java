package com.iohao.game.common.kit.adapter;


import java.util.function.Supplier;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */

class HuAssert {
    public static <T> T notNull(T object, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        return notNull(object, () -> new IllegalArgumentException(HuStrUtil.format(errorMsgTemplate, params)));
    }

    public static <T extends CharSequence> T notEmpty(T text) throws IllegalArgumentException {
        return notEmpty(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
    }

    public static <T extends CharSequence> T notEmpty(T text, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        return notEmpty(text, () -> new IllegalArgumentException(HuStrUtil.format(errorMsgTemplate, params)));
    }

    public static <T extends CharSequence, X extends Throwable> T notEmpty(T text, Supplier<X> errorSupplier) throws X {
        if (HuStrUtil.isEmpty(text)) {
            throw errorSupplier.get();
        }
        return text;
    }

    public static <T, X extends Throwable> T notNull(T object, Supplier<X> errorSupplier) throws X {
        if (null == object) {
            throw errorSupplier.get();
        }
        return object;
    }

    public static void isFalse(boolean expression, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        isFalse(expression, () -> new IllegalArgumentException(HuStrUtil.format(errorMsgTemplate, params)));
    }

    public static <X extends Throwable> void isFalse(boolean expression, Supplier<X> errorSupplier) throws X {
        if (expression) {
            throw errorSupplier.get();
        }
    }
}

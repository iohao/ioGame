package com.iohao.game.common.kit.adapter;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuObjectUtil {

    public static <T> T defaultIfNull(T source, Supplier<? extends T> defaultValueSupplier) {
        if (isNull(source)) {
            return defaultValueSupplier.get();
        }
        return source;
    }

    public static boolean isNull(Object obj) {
        //noinspection ConstantConditions
        return Objects.isNull(obj);
    }

    public static <T> T defaultIfNull(final T object, final T defaultValue) {
        return isNull(object) ? defaultValue : object;
    }
}

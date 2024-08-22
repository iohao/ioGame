package com.iohao.game.common.kit.adapter;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuExceptionUtil {

    public static String getMessage(Throwable e) {
        if (null == e) {
            return HuStrUtil.NULL;
        }

        return HuStrUtil.format("{}: {}", e.getClass().getSimpleName(), e.getMessage());
    }
}

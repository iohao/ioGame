package com.iohao.game.common.kit.adapter;


import java.io.Serial;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuUtilException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8247610319171014183L;

    public HuUtilException(Throwable e) {
        super(HuExceptionUtil.getMessage(e), e);
    }

    public HuUtilException(String messageTemplate, Object... params) {
        super(HuStrUtil.format(messageTemplate, params));
    }

    public HuUtilException(Throwable throwable, String messageTemplate, Object... params) {
        super(HuStrUtil.format(messageTemplate, params), throwable);
    }
}

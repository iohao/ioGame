package com.iohao.game.common.kit.adapter;


import java.io.Serial;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuIoRuntimeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8247610319171014183L;

    public HuIoRuntimeException(Throwable e) {
        super(HuExceptionUtil.getMessage(e), e);
    }

    public HuIoRuntimeException(String message) {
        super(message);
    }

    public HuIoRuntimeException(String messageTemplate, Object... params) {
        super(HuStrUtil.format(messageTemplate, params));
    }

    public HuIoRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public HuIoRuntimeException(Throwable throwable, String messageTemplate, Object... params) {
        super(HuStrUtil.format(messageTemplate, params), throwable);
    }

}

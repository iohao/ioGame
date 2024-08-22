package com.iohao.game.common.kit.adapter;


import java.io.Serial;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuNoResourceException extends HuIoRuntimeException {
    @Serial
    private static final long serialVersionUID = -623254467603299129L;

    public HuNoResourceException(String message) {
        super(message);
    }

    public HuNoResourceException(String messageTemplate, Object... params) {
        super(HuStrUtil.format(messageTemplate, params));
    }

}

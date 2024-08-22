package com.iohao.game.common.kit.adapter;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuCharsetUtil {

    public static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;

    public static Charset charset(String charsetName) throws UnsupportedCharsetException {
        return HuStrUtil.isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName);
    }

    public static Charset defaultCharset() {
        return Charset.defaultCharset();
    }
}

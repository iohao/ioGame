package com.iohao.game.common.kit.adapter;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
 class HuHexUtil {
    public static final HuBase16Codec CODEC_LOWER = new HuBase16Codec(true);
    public static final HuBase16Codec CODEC_UPPER = new HuBase16Codec(false);
    public static void appendHex(StringBuilder builder, byte b, boolean toLowerCase) {
        (toLowerCase ? CODEC_LOWER : CODEC_UPPER).appendHex(builder, b);
    }
}

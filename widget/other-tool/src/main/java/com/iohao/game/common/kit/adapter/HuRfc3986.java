package com.iohao.game.common.kit.adapter;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuRfc3986 {

    public static final HuPercentCodec PATH = HuPercentCodec
            .of(unreservedChars())
            .orNew(HuPercentCodec.of("!$&'()*+,;="))
            .or(HuPercentCodec.of(":@"))
            .orNew(HuPercentCodec.of("/"));

    private static StringBuilder unreservedChars() {
        StringBuilder sb = new StringBuilder();

        // ALPHA
        for (char c = 'A'; c <= 'Z'; c++) {
            sb.append(c);
        }
        for (char c = 'a'; c <= 'z'; c++) {
            sb.append(c);
        }

        // DIGIT
        for (char c = '0'; c <= '9'; c++) {
            sb.append(c);
        }

        // "-" / "." / "_" / "~"
        sb.append("_.-~");

        return sb;
    }
}

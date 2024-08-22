package com.iohao.game.common.kit.adapter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.BitSet;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuPercentCodec implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final BitSet safeCharacters;

    private boolean encodeSpaceAsPlus = false;

    public static HuPercentCodec of(HuPercentCodec codec) {
        return new HuPercentCodec((BitSet) codec.safeCharacters.clone());
    }

    public static HuPercentCodec of(CharSequence chars) {
        HuAssert.notNull(chars, "chars must not be null");
        final HuPercentCodec codec = new HuPercentCodec();
        final int length = chars.length();
        for (int i = 0; i < length; i++) {
            codec.addSafe(chars.charAt(i));
        }
        return codec;
    }

    public HuPercentCodec() {
        this(new BitSet(256));
    }

    public HuPercentCodec(BitSet safeCharacters) {
        this.safeCharacters = safeCharacters;
    }

    public HuPercentCodec addSafe(char c) {
        safeCharacters.set(c);
        return this;
    }


    public HuPercentCodec or(HuPercentCodec codec) {
        this.safeCharacters.or(codec.safeCharacters);
        return this;
    }

    public HuPercentCodec orNew(HuPercentCodec codec) {
        return of(this).or(codec);
    }


    public String encode(CharSequence path, Charset charset, char... customSafeChar) {
        if (null == charset || HuStrUtil.isEmpty(path)) {
            return HuStrUtil.str(path);
        }

        final StringBuilder rewrittenPath = new StringBuilder(path.length());
        final ByteArrayOutputStream buf = new ByteArrayOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter(buf, charset);

        char c;
        for (int i = 0; i < path.length(); i++) {
            c = path.charAt(i);
            if (safeCharacters.get(c) || HuArrayUtil.contains(customSafeChar, c)) {
                rewrittenPath.append(c);
            } else if (encodeSpaceAsPlus && c == HuCharUtil.SPACE) {
                // 对于空格单独处理
                rewrittenPath.append('+');
            } else {
                // convert to external encoding before hex conversion
                try {
                    writer.write(c);
                    writer.flush();
                } catch (IOException e) {
                    buf.reset();
                    continue;
                }

                // 兼容双字节的Unicode符处理（如部分emoji）
                byte[] ba = buf.toByteArray();
                for (byte toEncode : ba) {
                    // Converting each byte in the buffer
                    rewrittenPath.append('%');
                    HuHexUtil.appendHex(rewrittenPath, toEncode, false);
                }
                buf.reset();
            }
        }
        return rewrittenPath.toString();
    }
}

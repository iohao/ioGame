package com.iohao.game.common.kit.adapter;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuBase16Codec {

    private final char[] alphabets;

    public HuBase16Codec(boolean lowerCase) {
        this.alphabets = (lowerCase ? "0123456789abcdef" : "0123456789ABCDEF").toCharArray();
    }

    public char[] encode(byte[] data) {
        final int len = data.length;
        final char[] out = new char[len << 1];
        //len*2
        // two characters from the hex value.
        for (int i = 0, j = 0; i < len; i++) {
            out[j++] = alphabets[(0xF0 & data[i]) >>> 4];
            // 高位
            out[j++] = alphabets[0x0F & data[i]];
            // 低位
        }
        return out;
    }

    public byte[] decode(CharSequence encoded) {
        if (HuStrUtil.isEmpty(encoded)) {
            return new byte[0];
        }

        encoded = HuStrUtil.cleanBlank(encoded);
        int len = encoded.length();

        if ((len & 0x01) != 0) {
            // 如果提供的数据是奇数长度，则前面补0凑偶数
            encoded = "0" + encoded;
            len = encoded.length();
        }

        final byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(encoded.charAt(j), j) << 4;
            j++;
            f = f | toDigit(encoded.charAt(j), j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }


    public void appendHex(StringBuilder builder, byte b) {
        int high = (b & 0xf0) >>> 4;
        int low = b & 0x0f;
        builder.append(alphabets[high]);
        builder.append(alphabets[low]);
    }

    private static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit < 0) {
            throw new HuUtilException("Illegal hexadecimal character {} at index {}", ch, index);
        }
        return digit;
    }
}
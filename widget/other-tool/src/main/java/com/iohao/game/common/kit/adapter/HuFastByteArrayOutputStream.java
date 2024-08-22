package com.iohao.game.common.kit.adapter;


import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuFastByteArrayOutputStream extends OutputStream {

    private final HuFastByteBuffer buffer;

    public HuFastByteArrayOutputStream() {
        this(1024);
    }

    public HuFastByteArrayOutputStream(int size) {
        buffer = new HuFastByteBuffer(size);
    }

    @Override
    public void write(byte[] b, int off, int len) {
        buffer.append(b, off, len);
    }

    @Override
    public void write(int b) {
        buffer.append((byte) b);
    }

    public int size() {
        return buffer.size();
    }

    @Override
    public void close() {
    }

    public void reset() {
        buffer.reset();
    }


    public byte[] toByteArray() {
        return buffer.toArray();
    }

    @Override
    public String toString() {
        return toString(HuCharsetUtil.defaultCharset());
    }

    public String toString(String charsetName) {
        return toString(HuCharsetUtil.charset(charsetName));
    }

    public String toString(Charset charset) {
        return new String(toByteArray(),
                HuObjectUtil.defaultIfNull(charset, HuCharsetUtil.defaultCharset()));
    }

}

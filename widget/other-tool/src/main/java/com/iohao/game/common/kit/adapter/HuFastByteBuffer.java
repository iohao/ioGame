package com.iohao.game.common.kit.adapter;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuFastByteBuffer {

    private byte[][] buffers = new byte[16][];

    private int currentBufferIndex = -1;
    private byte[] currentBuffer;
    private int offset;
    private int size;

    private final int minChunkLen;


    public HuFastByteBuffer(int size) {
        if (size <= 0) {
            size = 1024;
        }
        this.minChunkLen = Math.abs(size);
    }

    private void needNewBuffer(int newSize) {
        int delta = newSize - size;
        int newBufferSize = Math.max(minChunkLen, delta);

        currentBufferIndex++;
        currentBuffer = new byte[newBufferSize];
        offset = 0;

        // add buffer
        if (currentBufferIndex >= buffers.length) {
            int newLen = buffers.length << 1;
            byte[][] newBuffers = new byte[newLen][];
            System.arraycopy(buffers, 0, newBuffers, 0, buffers.length);
            buffers = newBuffers;
        }
        buffers[currentBufferIndex] = currentBuffer;
    }

    public HuFastByteBuffer append(byte[] array, int off, int len) {
        int end = off + len;
        if ((off < 0) || (len < 0) || (end > array.length)) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return this;
        }
        int newSize = size + len;
        int remaining = len;

        if (currentBuffer != null) {
            // first try to fill current buffer
            int part = Math.min(remaining, currentBuffer.length - offset);
            System.arraycopy(array, end - remaining, currentBuffer, offset, part);
            remaining -= part;
            offset += part;
            size += part;
        }

        if (remaining > 0) {
            // still some data left
            // ask for new buffer
            needNewBuffer(newSize);

            // then copy remaining
            // but this time we are sure that it will fit
            int part = Math.min(remaining, currentBuffer.length - offset);
            System.arraycopy(array, end - remaining, currentBuffer, offset, part);
            offset += part;
            size += part;
        }

        return this;
    }

    public HuFastByteBuffer append(byte[] array) {
        return append(array, 0, array.length);
    }

    public HuFastByteBuffer append(byte element) {
        if ((currentBuffer == null) || (offset == currentBuffer.length)) {
            needNewBuffer(size + 1);
        }

        currentBuffer[offset] = element;
        offset++;
        size++;

        return this;
    }

    public HuFastByteBuffer append(HuFastByteBuffer buff) {
        if (buff.size == 0) {
            return this;
        }
        for (int i = 0; i < buff.currentBufferIndex; i++) {
            append(buff.buffers[i]);
        }
        append(buff.currentBuffer, 0, buff.offset);
        return this;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int index() {
        return currentBufferIndex;
    }

    public int offset() {
        return offset;
    }

    public byte[] array(int index) {
        return buffers[index];
    }

    public void reset() {
        size = 0;
        offset = 0;
        currentBufferIndex = -1;
        currentBuffer = null;
    }

    public byte[] toArray() {
        int pos = 0;
        byte[] array = new byte[size];

        if (currentBufferIndex == -1) {
            return array;
        }

        for (int i = 0; i < currentBufferIndex; i++) {
            int len = buffers[i].length;
            System.arraycopy(buffers[i], 0, array, pos, len);
            pos += len;
        }

        System.arraycopy(buffers[currentBufferIndex], 0, array, pos, offset);

        return array;
    }

    public byte get(int index) {
        if ((index >= size) || (index < 0)) {
            throw new IndexOutOfBoundsException();
        }
        int ndx = 0;
        while (true) {
            byte[] b = buffers[ndx];
            if (index < b.length) {
                return b[index];
            }
            ndx++;
            index -= b.length;
        }
    }

}

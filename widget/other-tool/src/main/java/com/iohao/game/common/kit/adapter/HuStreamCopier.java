package com.iohao.game.common.kit.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuStreamCopier extends HuIoCopier<InputStream, OutputStream> {


    public HuStreamCopier(int bufferSize, long count) {
        super(bufferSize, count);
    }

    @Override
    public long copy(InputStream source, OutputStream target) {
        HuAssert.notNull(source, "InputStream is null !");
        HuAssert.notNull(target, "OutputStream is null !");


        final long size;
        try {
            size = doCopy(source, target, new byte[bufferSize(this.count)]);
            target.flush();
        } catch (IOException e) {
            throw new HuIoRuntimeException(e);
        }


        return size;
    }

    private long doCopy(InputStream source, OutputStream target, byte[] buffer) throws IOException {
        long numToRead = this.count > 0 ? this.count : Long.MAX_VALUE;
        long total = 0;

        int read;
        while (numToRead > 0) {
            read = source.read(buffer, 0, bufferSize(numToRead));
            if (read < 0) {
                // 提前读取到末尾
                break;
            }
            target.write(buffer, 0, read);
            if (flushEveryBuffer) {
                target.flush();
            }

            numToRead -= read;
            total += read;

        }

        return total;
    }
}

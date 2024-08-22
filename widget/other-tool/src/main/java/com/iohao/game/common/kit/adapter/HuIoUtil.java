package com.iohao.game.common.kit.adapter;


import java.io.*;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuIoUtil {

    public static final int DEFAULT_BUFFER_SIZE = 2 << 12;

    public static long copy(InputStream in, OutputStream out) throws HuIoRuntimeException {
        return copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    public static long copy(InputStream in, OutputStream out, int bufferSize) throws HuIoRuntimeException {
        return new HuStreamCopier(bufferSize, -1).copy(in, out);
    }

    public static long copy(FileInputStream in, FileOutputStream out) throws HuIoRuntimeException {
        HuAssert.notNull(in, "FileInputStream is null!");
        HuAssert.notNull(out, "FileOutputStream is null!");

        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = in.getChannel();
            outChannel = out.getChannel();
            return copy(inChannel, outChannel);
        } finally {
            close(outChannel);
            close(inChannel);
        }
    }

    public static FileInputStream toStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new HuIoRuntimeException(e);
        }
    }

    public static BufferedInputStream toBuffered(InputStream in) {
        HuAssert.notNull(in, "InputStream must be not null!");
        return (in instanceof BufferedInputStream) ? (BufferedInputStream) in : new BufferedInputStream(in);
    }

    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                // 静默关闭
            }
        }
    }

    public static long copy(FileChannel inChannel, FileChannel outChannel) throws HuIoRuntimeException {
        HuAssert.notNull(inChannel, "In channel is null!");
        HuAssert.notNull(outChannel, "Out channel is null!");

        try {
            return copySafely(inChannel, outChannel);
        } catch (IOException e) {
            throw new HuIoRuntimeException(e);
        }
    }

    private static long copySafely(FileChannel inChannel, FileChannel outChannel) throws IOException {
        final long totalBytes = inChannel.size();
        for (long pos = 0, remaining = totalBytes; remaining > 0; ) { // 确保文件内容不会缺失
            final long writeBytes = inChannel.transferTo(pos, remaining, outChannel); // 实际传输的字节数
            pos += writeBytes;
            remaining -= writeBytes;
        }
        return totalBytes;
    }

    public static BufferedReader getReader(InputStream in, Charset charset) {
        if (null == in) {
            return null;
        }

        InputStreamReader reader;
        if (null == charset) {
            reader = new InputStreamReader(in);
        } else {
            reader = new InputStreamReader(in, charset);
        }

        return new BufferedReader(reader);
    }


    public static String read(Reader reader) throws HuIoRuntimeException {
        return read(reader, true);
    }


    public static String read(Reader reader, boolean isClose) throws HuIoRuntimeException {
        final StringBuilder builder = new StringBuilder();
        final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
        try {
            while (-1 != reader.read(buffer)) {
                builder.append(buffer.flip());
            }
        } catch (IOException e) {
            throw new HuIoRuntimeException(e);
        } finally {
            if (isClose) {
                HuIoUtil.close(reader);
            }
        }
        return builder.toString();
    }


    public static byte[] readBytes(InputStream in) throws HuIoRuntimeException {
        return readBytes(in, true);
    }

    public static byte[] readBytes(InputStream in, boolean isClose) throws HuIoRuntimeException {
        if (in instanceof FileInputStream) {
            // 文件流的长度是可预见的，此时直接读取效率更高
            final byte[] result;
            try {
                final int available = in.available();
                result = new byte[available];
                final int readLength = in.read(result);
                if (readLength != available) {
                    throw new IOException(HuStrUtil.format("File length is [{}] but read [{}]!", available, readLength));
                }
            } catch (IOException e) {
                throw new HuIoRuntimeException(e);
            } finally {
                if (isClose) {
                    close(in);
                }
            }
            return result;
        }

        // 未知bytes总量的流
        return read(in, isClose).toByteArray();
    }

    public static HuFastByteArrayOutputStream read(InputStream in, boolean isClose) throws HuIoRuntimeException {
        final HuFastByteArrayOutputStream out;
        if (in instanceof FileInputStream) {
            // 文件流的长度是可预见的，此时直接读取效率更高
            try {
                out = new HuFastByteArrayOutputStream(in.available());
            } catch (IOException e) {
                throw new HuIoRuntimeException(e);
            }
        } else {
            out = new HuFastByteArrayOutputStream();
        }
        try {
            copy(in, out);
        } finally {
            if (isClose) {
                close(in);
            }
        }
        return out;
    }
}

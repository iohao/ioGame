package com.iohao.game.common.kit.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
interface HuResource {

    /**
     * 获取资源名，例如文件资源的资源名为文件名
     *
     * @return 资源名
     */
    String getName();

    /**
     * 获得解析后的{@link URL}，无对应URL的返回{@code null}
     *
     * @return 解析后的{@link URL}
     */
    URL getUrl();

    /**
     * 获得 {@link InputStream}
     *
     * @return {@link InputStream}
     */
    InputStream getStream();

    /**
     * 获得Reader
     *
     * @param charset 编码
     * @return {@link BufferedReader}
     */
    default BufferedReader getReader(Charset charset) {
        return HuIoUtil.getReader(getStream(), charset);
    }

    /**
     * 读取资源内容，读取完毕后会关闭流<br>
     * 关闭流并不影响下一次读取
     *
     * @param charset 编码
     * @return 读取资源内容
     * @throws HuIoRuntimeException 包装{@link IOException}
     */
    default String readStr(Charset charset) throws HuIoRuntimeException {
        return HuIoUtil.read(getReader(charset));
    }

}

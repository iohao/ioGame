package com.iohao.game.common.kit.adapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuUrlUtil {

    public static final String CLASSPATH_URL_PREFIX = "classpath:";
    public static final String FILE_URL_PREFIX = "file:";

    public static URL url(URI uri) throws HuUtilException {
        if (null == uri) {
            return null;
        }
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            throw new HuUtilException(e);
        }
    }

    public static InputStream getStream(URL url) {
        HuAssert.notNull(url, "URL must be not null");
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new HuIoRuntimeException(e);
        }
    }

    public static URL getURL(File file) {
        HuAssert.notNull(file, "File is null !");
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new HuUtilException(e, "Error occured when get URL!");
        }
    }

    public static String getDecodedPath(URL url) {
        if (null == url) {
            return null;
        }

        String path = null;
        try {
            // URL对象的getPath方法对于包含中文或空格的问题
            path = toURI(url).getPath();
        } catch (HuUtilException e) {
            // ignore
        }
        return (null != path) ? path : url.getPath();
    }

    public static URI toURI(URL url) throws HuUtilException {
        return toURI(url, false);
    }

    public static URI toURI(URL url, boolean isEncode) throws HuUtilException {
        if (null == url) {
            return null;
        }

        return toURI(url.toString(), isEncode);
    }

    public static URI toURI(String location, boolean isEncode) throws HuUtilException {
        if (isEncode) {
            location = encode(location);
        }
        try {
            return new URI(HuStrUtil.trim(location));
        } catch (URISyntaxException e) {
            throw new HuUtilException(e);
        }
    }

    public static String encode(String url) throws HuUtilException {
        return encode(url, HuCharsetUtil.CHARSET_UTF_8);
    }

    public static String encode(String url, Charset charset) {
        return HuRfc3986.PATH.encode(url, charset);
    }
}

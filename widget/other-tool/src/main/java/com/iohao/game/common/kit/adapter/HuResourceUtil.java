package com.iohao.game.common.kit.adapter;


import java.net.URL;
import java.nio.charset.Charset;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuResourceUtil {

    public static String readStr(String resource, Charset charset) {
        return getResourceObj(resource).readStr(charset);
    }

    public static HuResource getResourceObj(String path) {
        if (HuStrUtil.isNotBlank(path)) {
            if (path.startsWith(HuUrlUtil.FILE_URL_PREFIX) || HuFileUtil.isAbsolutePath(path)) {
                return new HuFileResource(path);
            }
        }

        return new HuClassPathResource(path);
    }

    public static URL getResource(String resource) throws HuIoRuntimeException {
        return getResource(resource, null);
    }

    public static URL getResource(String resource, Class<?> baseClass) {
        resource = HuStrUtil.nullToEmpty(resource);
        return (null != baseClass) ? baseClass.getResource(resource) : HuClassLoaderUtil.getClassLoader().getResource(resource);
    }
}

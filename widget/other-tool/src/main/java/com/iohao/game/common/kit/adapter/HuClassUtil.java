package com.iohao.game.common.kit.adapter;


import java.net.URL;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
 class HuClassUtil {

    public static String getClassPath() {
        final URL classPathURL = HuResourceUtil.getResource("");
        String url = HuUrlUtil.getDecodedPath(classPathURL);
        return HuFileUtil.normalize(url);
    }

    public static ClassLoader getClassLoader() {
        return HuClassLoaderUtil.getClassLoader();
    }
}

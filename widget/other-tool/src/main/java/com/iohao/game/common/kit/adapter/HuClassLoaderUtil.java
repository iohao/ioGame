package com.iohao.game.common.kit.adapter;


/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuClassLoaderUtil {

    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = getContextClassLoader();
        if (classLoader == null) {
            classLoader = HuClassLoaderUtil.class.getClassLoader();
            if (null == classLoader) {
                classLoader = getSystemClassLoader();
            }
        }

        return classLoader;
    }

    public static ClassLoader getSystemClassLoader() {
        return ClassLoader.getSystemClassLoader();
    }

    public static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}

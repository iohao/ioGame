package com.iohao.game.common.kit.adapter;

import java.io.Serial;
import java.net.URL;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuClassPathResource extends HuUrlResource {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String path;
    private final ClassLoader classLoader;
    private final Class<?> clazz;
    static final String SLASH = "/";

    public HuClassPathResource(String path) {
        this(path, null, null);
    }

    public HuClassPathResource(String pathBaseClassLoader, ClassLoader classLoader, Class<?> clazz) {
        super((URL) null);

        Objects.requireNonNull(pathBaseClassLoader);

        final String path = normalizePath(pathBaseClassLoader);
        this.path = path;
        this.name = HuStrUtil.isBlank(path) ? null : HuFileUtil.getName(path);

        this.classLoader = defaultIfNull(classLoader, HuClassUtil::getClassLoader);
        this.clazz = clazz;
        initUrl();
    }

    public static <T> T defaultIfNull(T source, Supplier<? extends T> defaultValueSupplier) {
        if (Objects.isNull(source)) {
            return defaultValueSupplier.get();
        }
        return source;
    }

    public final String getPath() {
        return this.path;
    }

    private void initUrl() {
        if (null != this.clazz) {
            super.url = this.clazz.getResource(this.path);
        } else if (null != this.classLoader) {
            super.url = this.classLoader.getResource(this.path);
        } else {
            super.url = ClassLoader.getSystemResource(this.path);
        }
        if (null == super.url) {
            throw new HuNoResourceException("Resource of path [{}] not exist!", this.path);
        }
    }

    @Override
    public String toString() {
        return (null == this.path) ? super.toString() : "classpath:" + this.path;
    }

    private String normalizePath(String path) {
        // 标准化路径
        path = HuFileUtil.normalize(path);
        path = HuStrUtil.removePrefix(path, SLASH);

        HuAssert.isFalse(HuFileUtil.isAbsolutePath(path), "Path [{}] must be a relative path !", path);
        return path;
    }

}

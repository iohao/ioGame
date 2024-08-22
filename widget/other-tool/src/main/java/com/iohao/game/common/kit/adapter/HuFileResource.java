package com.iohao.game.common.kit.adapter;


import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuFileResource implements HuResource, Serializable {

    private final File file;
    private final String name;

    public HuFileResource(String path) {
        this(HuFileUtil.file(path));
    }

    public HuFileResource(File file) {
        this(file, null);
    }

    public HuFileResource(File file, String fileName) {
        Objects.requireNonNull(file);
        Objects.requireNonNull(file);
        this.file = file;
        this.name = defaultIfNull(fileName, file::getName);
    }

    private static <T> T defaultIfNull(T source, Supplier<? extends T> defaultValueSupplier) {
        if (Objects.isNull(source)) {
            return defaultValueSupplier.get();
        }
        return source;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public URL getUrl() {
        return HuUrlUtil.getURL(this.file);
    }

    @Override
    public InputStream getStream() throws HuNoResourceException {
        return HuFileUtil.getInputStream(this.file);
    }

    public File getFile() {
        return this.file;
    }

    @Override
    public String toString() {
        return this.file.toString();
    }
}

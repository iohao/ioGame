package com.iohao.game.common.kit.adapter;


import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuUrlResource implements HuResource, Serializable {

    protected URL url;
    protected String name;

    public HuUrlResource(URL url) {
        this(url, null);
    }

    public HuUrlResource(URL url, String name) {
        this.url = url;

        this.name = HuObjectUtil.defaultIfNull(name, () -> (null != url ? HuFileUtil.getName(url.getPath()) : null));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public URL getUrl() {
        return this.url;
    }

    @Override
    public InputStream getStream() throws HuNoResourceException {
        if (null == this.url) {
            throw new HuNoResourceException("Resource URL is null!");
        }
        return HuUrlUtil.getStream(url);
    }


    /**
     * 获得File
     *
     * @return {@link File}
     */
    public File getFile() {
        return HuFileUtil.file(this.url);
    }

    /**
     * 返回路径
     *
     * @return 返回URL路径
     */
    @Override
    public String toString() {
        return (null == this.url) ? "null" : this.url.toString();
    }
}

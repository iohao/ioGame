package com.iohao.game.common.kit.adapter;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
@UtilityClass
public class AdapterHuUtils {
    public String readStr(String resource, Charset charset) {
        return HuResourceUtil.readStr(resource, charset);
    }

    public File mkdir(String dirPath) {
        return HuFileUtil.mkdir(dirPath);
    }

    public File file(String path) {
        return HuFileUtil.file(path);
    }

    public File writeUtf8String(String content, String path) {
        return HuFileUtil.writeUtf8String(content, path);
    }

    public boolean isDirectory(String path) {
        return HuFileUtil.isDirectory(path);
    }

    public static boolean exist(File file) {
        return (null != file) && file.exists();
    }

    public String format(@NonNull CharSequence template, @NonNull Map<?, ?> map) {
        return HuStrUtil.format(template, map);
    }

    public String format(@NonNull CharSequence template, Object... params) {
        return HuStrUtil.format(template, params);
    }
}

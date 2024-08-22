package com.iohao.game.common.kit.adapter;


import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuFileUtil extends HuPathUtil {

    private static final Pattern PATTERN_PATH_ABSOLUTE = Pattern.compile("^[a-zA-Z]:([/\\\\].*)?");

    public static String getName(String filePath) {

        if (null == filePath) {
            return null;
        }
        int len = filePath.length();
        if (0 == len) {
            return filePath;
        }
        if (HuCharUtil.isFileSeparator(filePath.charAt(len - 1))) {
            // 以分隔符结尾的去掉结尾分隔符
            len--;
        }

        int begin = 0;
        char c;
        for (int i = len - 1; i > -1; i--) {
            c = filePath.charAt(i);
            if (HuCharUtil.isFileSeparator(c)) {
                // 查找最后一个路径分隔符（/或者\）
                begin = i + 1;
                break;
            }
        }

        return filePath.substring(begin, len);
    }

    public static File file(URL url) {
        return new File(HuUrlUtil.toURI(url));
    }

    public static BufferedInputStream getInputStream(File file) throws HuIoRuntimeException {
        return HuIoUtil.toBuffered(HuIoUtil.toStream(file));
    }

    public static File mkdir(String dirPath) {
        if (dirPath == null) {
            return null;
        }
        final File dir = file(dirPath);
        return mkdir(dir);
    }


    public static File mkdir(File dir) {
        if (dir == null) {
            return null;
        }
        if (!dir.exists()) {
            mkdirSafely(dir, 5, 1);
        }
        return dir;
    }

    public static boolean mkdirSafely(File dir, int tryCount, long sleepMillis) {
        if (dir == null) {
            return false;
        }

        if (dir.isDirectory()) {
            return true;
        }

        for (int i = 1; i <= tryCount; i++) {
            // 高并发场景下，可以看到 i 处于 1 ~ 3 之间
            // 如果文件已存在，也会返回 false，所以该值不能作为是否能创建的依据，因此不对其进行处理
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
            if (dir.exists()) {
                return true;
            }

            if (sleepMillis > 0) {
                try {
                    TimeUnit.MILLISECONDS.sleep(sleepMillis);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return dir.exists();
    }

    public static File file(String path) {
        if (null == path) {
            return null;
        }

        return new File(getAbsolutePath(path));
    }


    public static String getAbsolutePath(String path) {
        return getAbsolutePath(path, null);
    }


    public static String getAbsolutePath(String path, Class<?> baseClass) {
        String normalPath;
        if (path == null) {
            normalPath = "";
        } else {
            normalPath = normalize(path);
            if (isAbsolutePath(normalPath)) {
                // 给定的路径已经是绝对路径了
                return normalPath;
            }
        }

        // 相对于ClassPath路径
        final URL url = HuResourceUtil.getResource(normalPath, baseClass);
        if (null != url) {
            // 对于jar中文件包含file:前缀，需要去掉此类前缀，在此做标准化，since 3.0.8 解决中文或空格路径被编码的问题
            return HuFileUtil.normalize(HuUrlUtil.getDecodedPath(url));
        }

        // 如果资源不存在，则返回一个拼接的资源绝对路径
        final String classPath = HuClassUtil.getClassPath();
        if (null == classPath) {
            // 在jar运行模式中，ClassPath有可能获取不到，此时返回原始相对路径（此时获取的文件为相对工作目录）
            return path;
        }

        // 资源不存在的情况下使用标准化路径有问题，使用原始路径拼接后标准化路径
        return normalize(classPath.concat(Objects.requireNonNull(path)));
    }


    public static String normalize(String path) {
        if (path == null) {
            return null;
        }

        // 兼容Spring风格的ClassPath路径，去除前缀，不区分大小写
        String pathToUse = HuStrUtil.removePrefixIgnoreCase(path, HuUrlUtil.CLASSPATH_URL_PREFIX);
        // 去除file:前缀
        pathToUse = HuStrUtil.removePrefixIgnoreCase(pathToUse, HuUrlUtil.FILE_URL_PREFIX);

        // 识别home目录形式，并转换为绝对路径
        if (HuStrUtil.startWith(pathToUse, '~')) {
            pathToUse = getUserHomePath() + pathToUse.substring(1);
        }

        // 统一使用斜杠
        pathToUse = pathToUse.replaceAll("[/\\\\]+", HuStrUtil.SLASH);
        // 去除开头空白符，末尾空白符合法，不去除
        pathToUse = HuStrUtil.trimStart(pathToUse);
        //兼容Windows下的共享目录路径（原始路径如果以\\开头，则保留这种路径）
        if (path.startsWith("\\\\")) {
            pathToUse = "\\" + pathToUse;
        }

        String prefix = HuStrUtil.EMPTY;
        int prefixIndex = pathToUse.indexOf(HuStrUtil.COLON);
        if (prefixIndex > -1) {
            // 可能Windows风格路径
            prefix = pathToUse.substring(0, prefixIndex + 1);
            if (HuStrUtil.startWith(prefix, HuStrUtil.C_SLASH)) {
                // 去除类似于/C:这类路径开头的斜杠
                prefix = prefix.substring(1);
            }
            if (!prefix.contains(HuStrUtil.SLASH)) {
                pathToUse = pathToUse.substring(prefixIndex + 1);
            } else {
                // 如果前缀中包含/,说明非Windows风格path
                prefix = HuStrUtil.EMPTY;
            }
        }
        if (pathToUse.startsWith(HuStrUtil.SLASH)) {
            prefix += HuStrUtil.SLASH;
            pathToUse = pathToUse.substring(1);
        }

        List<String> pathList = HuStrUtil.split(pathToUse, HuStrUtil.C_SLASH);

        List<String> pathElements = new LinkedList<>();
        int tops = 0;
        String element;
        for (int i = pathList.size() - 1; i >= 0; i--) {
            element = pathList.get(i);
            // 只处理非.的目录，即只处理非当前目录
            if (!HuStrUtil.DOT.equals(element)) {
                if (HuStrUtil.DOUBLE_DOT.equals(element)) {
                    tops++;
                } else {
                    if (tops > 0) {
                        // 有上级目录标记时按照个数依次跳过
                        tops--;
                    } else {
                        // Normal path element found.
                        pathElements.add(0, element);
                    }
                }
            }
        }

        // issue#1703@Github
        if (tops > 0 && HuStrUtil.isEmpty(prefix)) {
            // 只有相对路径补充开头的..，绝对路径直接忽略之
            while (tops-- > 0) {
                //遍历完节点发现还有上级标注（即开头有一个或多个..），补充之
                // Normal path element found.
                pathElements.add(0, HuStrUtil.DOUBLE_DOT);
            }
        }


        return prefix + String.join(HuStrUtil.SLASH, pathElements);
    }

    /**
     * 获取用户路径（绝对路径）
     *
     * @return 用户路径
     * @since 4.0.6
     */
    public static String getUserHomePath() {
        return System.getProperty("user.home");
    }

    public static boolean isAbsolutePath(String path) {
        if (HuStrUtil.isEmpty(path)) {
            return false;
        }

        // 给定的路径已经是绝对路径了
        return HuStrUtil.C_SLASH == path.charAt(0) || isMatch(PATTERN_PATH_ABSOLUTE, path);
    }

    private static boolean isMatch(Pattern pattern, CharSequence content) {
        if (content == null || pattern == null) {
            // 提供null的字符串为不匹配
            return false;
        }
        return pattern.matcher(content).matches();
    }

    public static File writeUtf8String(String content, String path) throws HuIoRuntimeException {
        return writeString(content, path, HuCharsetUtil.CHARSET_UTF_8);
    }

    public static File writeString(String content, String path, Charset charset) throws HuIoRuntimeException {
        return writeString(content, touch(path), charset);
    }

    public static File writeString(String content, File file, Charset charset) throws HuIoRuntimeException {
        return HuFileWriter.create(file, charset).write(content);
    }

    public static File touch(String path) throws HuIoRuntimeException {
        if (path == null) {
            return null;
        }
        return touch(file(path));
    }

    public static boolean isDirectory(String path) {
        return (null != path) && file(path).isDirectory();
    }

    public static File touch(File file) throws HuIoRuntimeException {
        if (null == file) {
            return null;
        }

        if (!file.exists()) {
            mkParentDirs(file);
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (Exception e) {
                throw new HuIoRuntimeException(e);
            }
        }
        return file;
    }

    public static File mkParentDirs(File file) {
        if (null == file) {
            return null;
        }
        return mkdir(getParent(file, 1));
    }

    public static File getParent(File file, int level) {
        if (level < 1 || null == file) {
            return file;
        }

        File parentFile;
        try {
            parentFile = file.getCanonicalFile().getParentFile();
        } catch (IOException e) {
            throw new HuIoRuntimeException(e);
        }
        if (1 == level) {
            return parentFile;
        }
        return getParent(parentFile, level - 1);
    }

}

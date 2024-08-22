package com.iohao.game.common.kit.adapter;


import java.io.IOException;
import java.nio.file.*;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuPathUtil {

    public static Path copyFile(Path src, Path target, CopyOption... options) throws HuIoRuntimeException {
        HuAssert.notNull(src, "Source File is null !");
        HuAssert.notNull(target, "Destination File or directory is null !");

        final Path targetPath = isDirectory(target) ? target.resolve(src.getFileName()) : target;
        // 创建级联父目录
        mkParentDirs(targetPath);

        try {
            return Files.copy(src, targetPath, options);
        } catch (IOException e) {
            throw new HuIoRuntimeException(e);
        }
    }

    public static Path copy(Path src, Path target, CopyOption... options) throws HuIoRuntimeException {
        HuAssert.notNull(src, "Src path must be not null !");
        HuAssert.notNull(target, "Target path must be not null !");

        if (isDirectory(src)) {
            return copyContent(src, target.resolve(src.getFileName()), options);
        }
        return copyFile(src, target, options);
    }

    public static Path copyContent(Path src, Path target, CopyOption... options) throws HuIoRuntimeException {
        HuAssert.notNull(src, "Src path must be not null !");
        HuAssert.notNull(target, "Target path must be not null !");

        try {
            Files.walkFileTree(src, new HuCopyVisitor(src, target, options));
        } catch (IOException e) {
            throw new HuIoRuntimeException(e);
        }
        return target;
    }

    public static boolean isDirectory(Path path) {
        return isDirectory(path, false);
    }

    public static boolean isDirectory(Path path, boolean isFollowLinks) {
        if (null == path) {
            return false;
        }

        final LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
        return Files.isDirectory(path, options);
    }

    public static boolean equals(Path file1, Path file2) throws HuIoRuntimeException {
        try {
            return Files.isSameFile(file1, file2);
        } catch (IOException e) {
            throw new HuIoRuntimeException(e);
        }
    }


    public static boolean exists(Path path, boolean isFollowLinks) {
        final LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
        return Files.exists(path, options);
    }


    public static Path mkdir(Path dir) {
        if (null != dir && !exists(dir, false)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                throw new HuIoRuntimeException(e);
            }
        }
        return dir;
    }

    public static Path mkParentDirs(Path path) {
        return mkdir(path.getParent());
    }

    public static String getName(Path path) {
        if (null == path) {
            return null;
        }
        return path.getFileName().toString();
    }

}

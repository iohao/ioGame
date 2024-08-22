package com.iohao.game.common.kit.adapter;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author 渔民小镇
 * @date 2023-01-19
 */
class HuCopyVisitor extends SimpleFileVisitor<Path> {

    private final Path source;
    private final Path target;
    private final CopyOption[] copyOptions;
    private boolean isTargetCreated;

    public HuCopyVisitor(Path source, Path target, CopyOption... copyOptions) {
        if (HuFileUtil.exists(target, false) && !HuFileUtil.isDirectory(target)) {
            throw new IllegalArgumentException("Target must be a directory");
        }
        this.source = source;
        this.target = target;
        this.copyOptions = copyOptions;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        initTargetDir();
        // 将当前目录相对于源路径转换为相对于目标路径
        final Path targetDir = resolveTarget(dir);

        // 在目录不存在的情况下，copy方法会创建新目录
        try {
            Files.copy(dir, targetDir, copyOptions);
        } catch (FileAlreadyExistsException e) {
            if (!Files.isDirectory(targetDir)) {
                // 目标文件存在抛出异常，目录忽略
                throw e;
            }
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException {
        initTargetDir();
        // 如果目标存在，无论目录还是文件都抛出FileAlreadyExistsException异常，此处不做特别处理
        Files.copy(file, resolveTarget(file), copyOptions);
        return FileVisitResult.CONTINUE;
    }

    private Path resolveTarget(Path file) {
        return target.resolve(source.relativize(file));
    }

    private void initTargetDir() {
        if (!this.isTargetCreated) {
            HuFileUtil.mkdir(this.target);
            this.isTargetCreated = true;
        }
    }
}

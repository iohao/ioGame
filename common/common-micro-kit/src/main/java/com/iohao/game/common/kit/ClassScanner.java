/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.iohao.game.common.kit;

import com.iohao.game.common.consts.IoGameLogName;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashSet;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * class 扫描
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
@Slf4j(topic = IoGameLogName.CommonStdout)
public class ClassScanner {
    /** 需要扫描的包名 */
    final String packagePath;
    /** 存放扫描过的 clazz */
    final Set<Class<?>> clazzSet = new NonBlockingHashSet<>();
    /** true 保留符合条件的class */
    final Predicate<Class<?>> predicateFilter;

    ClassLoader classLoader;

    /**
     * 扫描
     *
     * @param packagePath     扫描路径
     * @param predicateFilter 过滤条件
     */
    public ClassScanner(String packagePath, Predicate<Class<?>> predicateFilter) {
        this.predicateFilter = predicateFilter;

        var path = packagePath.replace('.', '/');
        path = path.endsWith("/") ? path : path + '/';

        this.packagePath = path;
    }

    public List<Class<?>> listScan() {
        try {
            this.initClassLoad();

            List<URL> urlList = listResource();

            urlList.parallelStream().forEach(url -> {
                String protocol = url.getProtocol();
                try {
                    if ("jar".equals(protocol)) {
                        scanJar(url);

                    } else if ("file".equals(protocol)) {
                        scanFile(url);
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ArrayList<>(clazzSet);
    }

    private void initClassLoad() {
        if (Objects.nonNull(this.classLoader)) {
            return;
        }

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        this.classLoader = classLoader != null ? classLoader : ClassScanner.class.getClassLoader();
    }

    public List<URL> listResource() throws IOException {
        this.initClassLoad();

        Enumeration<URL> urlEnumeration = classLoader.getResources(packagePath);
        Set<URL> set = new HashSet<>();
        while (urlEnumeration.hasMoreElements()) {
            URL url = urlEnumeration.nextElement();
            set.add(url);
        }

        return new ArrayList<>(set);
    }

    private void scanJar(URL url) throws IOException {
        URLConnection urlConn = url.openConnection();
        if (urlConn instanceof JarURLConnection jarUrlConn) {
            try (JarFile jarFile = jarUrlConn.getJarFile()) {

                Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                while (jarEntryEnumeration.hasMoreElements()) {
                    JarEntry jarEntry = jarEntryEnumeration.nextElement();
                    String jarEntryName = jarEntry.getName();

                    // 扫描 packagePath 下的类
                    if (jarEntryName.endsWith(".class") && jarEntryName.startsWith(packagePath)) {
                        jarEntryName = jarEntryName.substring(0, jarEntryName.length() - 6).replace('/', '.');
                        scanClazz(jarEntryName);
                    }
                }

            }
        }
    }

    private void scanFile(URL url) {
        String path = url.getPath();
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        File file = new File(path);
        String classPath = getClassPath(file);
        scanFile(file, classPath);
    }

    private void scanFile(File file, String classPath) {
        if (file.isDirectory()) {

            File[] files = file.listFiles();

            if (Objects.isNull(files)) {
                return;
            }

            for (File value : files) {
                scanFile(value, classPath);
            }

        } else if (file.isFile()) {

            String absolutePath = file.getAbsolutePath();

            if (absolutePath.endsWith(".class")) {

                String className = absolutePath
                        .substring(classPath.length(), absolutePath.length() - 6)
                        .replace(File.separatorChar, '.');

                scanClazz(className);

            }
        }
    }

    private String getClassPath(File file) {
        String absolutePath = file.getAbsolutePath();

        if (!absolutePath.endsWith(File.separator)) {
            absolutePath = absolutePath + File.separator;
        }

        String ret = packagePath.replace('/', File.separatorChar);

        int index = absolutePath.lastIndexOf(ret);

        if (index != -1) {
            absolutePath = absolutePath.substring(0, index);
        }

        return absolutePath;
    }

    private void scanClazz(String className) {
        Class<?> clazz = null;

        try {
            clazz = classLoader.loadClass(className);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

        if (clazz != null && !clazzSet.contains(clazz)) {
            if (predicateFilter.test(clazz)) {
                clazzSet.add(clazz);
            }
        }
    }
}

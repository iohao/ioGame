/*
 * Copyright (c) Baidu Inc. All rights reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baidu.bjf.remoting.protobuf.utils.compiler;

import com.baidu.bjf.remoting.protobuf.utils.ClassHelper;
import com.baidu.bjf.remoting.protobuf.utils.OS;
import com.baidu.bjf.remoting.protobuf.utils.StringUtils;
import com.baidu.bjf.remoting.protobuf.utils.ZipUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import javax.tools.JavaFileObject.Kind;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

/**
 * JdkCompiler. (SPI, Singleton, ThreadSafe)
 *
 * @author xiemalin
 * @since 1.0.0
 */
public class JdkCompiler extends AbstractCompiler {

    /** Logger for this class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(JdkCompiler.class.getName());

    /** The compiler. */
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    /** The class loader. */
    private final ClassLoaderImpl classLoader;

    /** The java file manager. */
    private final JavaFileManagerImpl javaFileManager;

    /** The options. */
    private volatile List<String> options;

    /** The Constant DEFAULT_JDK_VERSION. */
    private static final String DEFAULT_JDK_VERSION = "1.8";

    private static final String BOOT_INF_CLASSES = "BOOT-INF/classes/";

    /** The Constant TEMP_PATH. */
    private static final String TEMP_PATH =
            System.getProperty("java.io.tmpdir") + File.separator + "JPROTOBUF_CACHE_DIR";

    /**
     * Instantiates a new jdk compiler.
     *
     * @param loader the loader
     */
    public JdkCompiler(final ClassLoader loader) {
        this(loader, DEFAULT_JDK_VERSION);
    }

    /**
     * Instantiates a new jdk compiler.
     *
     * @param loader     the loader
     * @param jdkVersion the jdk version
     */
    public JdkCompiler(final ClassLoader loader, final String jdkVersion) {
        options = new ArrayList<String>();
        options.add("-source");
        options.add(jdkVersion);
        options.add("-target");
        options.add(jdkVersion);

        // set compiler's classpath to be same as the runtime's
        if (compiler == null) {
            throw new RuntimeException(
                    "compiler is null maybe you are on JRE enviroment please change to JDK environment.");
        }
        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
        StandardJavaFileManager manager =
                compiler.getStandardFileManager(diagnosticCollector, null, StandardCharsets.UTF_8);
        if (loader instanceof URLClassLoader
                && (!loader.getClass().getName().equals("sun.misc.Launcher$AppClassLoader"))) {
            try {
                URLClassLoader urlClassLoader = (URLClassLoader) loader;
                List<File> files = new ArrayList<>();
                boolean isInternalJar = false;
                String rootJar = null;
                Set<String> fileNames = new HashSet<>();
                for (URL url : urlClassLoader.getURLs()) {

                    String file = url.getFile();
                    files.add(new File(file));
                    if (StringUtils.endsWith(file, "!/")) {
                        file = StringUtils.removeEnd(file, "!/");
                    }
                    if (file.startsWith("file:")) {
                        file = StringUtils.removeStart(file, "file:");
                    }

                    if (file.startsWith("nested:")) {
                        // springBoot 3.2
                        file = StringUtils.removeStart(file, "nested:");
                    }

                    if (file.contains("!")) {
                        // if has internal jar like
                        // file:/D:/develop/a.jar!/BOOT-INF/lib/spring-boot-starter-1.5.14.RELEASE.jar
                        isInternalJar = true;
                        rootJar = StringUtils.substringBefore(file, "!");
                        if (OS.isFamilyWindows() || OS.isFamilyWin9x()) {
                            rootJar = StringUtils.removeStart(rootJar, "/");
                        }
                    }
                    File f = new File(file);
                    fileNames.add(f.getName());
                    files.add(f);

                }
                if (isInternalJar && rootJar != null) {
                    ZipUtils.unZip(new File(rootJar), TEMP_PATH);
                    listFiles(TEMP_PATH, files, fileNames);
                    files.add(new File(TEMP_PATH, BOOT_INF_CLASSES));
                }

                manager.setLocation(StandardLocation.CLASS_PATH, files);
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        classLoader = AccessController.doPrivileged(new PrivilegedAction<ClassLoaderImpl>() {
            public ClassLoaderImpl run() {
                return new ClassLoaderImpl(loader);
            }
        });

        javaFileManager = new JavaFileManagerImpl(manager, classLoader);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.baidu.bjf.remoting.protobuf.utils.compiler.AbstractCompiler#doCompile(java.lang.String,
     * java.lang.String, java.io.OutputStream)
     */
    @Override
    public synchronized Class<?> doCompile(String name, String sourceCode, OutputStream os) throws Throwable {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Begin to compile source code: class is '{}'", name);
        }

        int i = name.lastIndexOf('.');
        String packageName = i < 0 ? "" : name.substring(0, i);
        String className = i < 0 ? name : name.substring(i + 1);
        JavaFileObjectImpl javaFileObject = new JavaFileObjectImpl(className, sourceCode);
        javaFileManager.putFileForInput(StandardLocation.SOURCE_PATH, packageName,
                className + ClassUtils.JAVA_EXTENSION, javaFileObject);

        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<JavaFileObject>();
        Boolean result = compiler.getTask(null, javaFileManager, diagnosticCollector, options, null,
                List.of(javaFileObject)).call();
        if (result == null || !result) {
            throw new IllegalStateException(
                    "Compilation failed. class: " + name + ", diagnostics: " + diagnosticCollector.getDiagnostics());
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("compile source code done: class is '{}'", name);
            LOGGER.debug("loading class '{}'", name);
        }

        Class<?> retClass = classLoader.loadClass(name);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("loading class done  '{}'", name);
        }

        if (os != null) {
            byte[] bytes = classLoader.loadClassBytes(name);
            if (bytes != null) {
                os.write(bytes);
                os.flush();
            }
        }
        return retClass;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.baidu.bjf.remoting.protobuf.utils.compiler.Compiler#loadBytes(java.lang.String)
     */
    @Override
    public byte[] loadBytes(String className) {
        return classLoader.loadClassBytes(className);
    }

    /**
     * The Class ClassLoaderImpl.
     */
    private static final class ClassLoaderImpl extends ClassLoader {

        /** The classes. */
        private final Map<String, JavaFileObject> classes = new HashMap<String, JavaFileObject>();

        /**
         * Instantiates a new class loader impl.
         *
         * @param parentClassLoader the parent class loader
         */
        ClassLoaderImpl(final ClassLoader parentClassLoader) {
            super(parentClassLoader);
        }

        /**
         * Files.
         *
         * @return the collection
         */
        Collection<JavaFileObject> files() {
            return Collections.unmodifiableCollection(classes.values());
        }

        /**
         * Load class bytes.
         *
         * @param qualifiedClassName the qualified class name
         * @return the byte[]
         */
        public byte[] loadClassBytes(final String qualifiedClassName) {
            JavaFileObject file = classes.get(qualifiedClassName);
            if (file != null) {
                return ((JavaFileObjectImpl) file).getByteCode();
            }
            return null;
        }

        @Override
        protected Class<?> findClass(final String qualifiedClassName) throws ClassNotFoundException {
            JavaFileObject file = classes.get(qualifiedClassName);
            if (file != null) {
                byte[] bytes = ((JavaFileObjectImpl) file).getByteCode();
                assert bytes != null;
                return defineClass(qualifiedClassName, bytes, 0, bytes.length);
            }
            try {
                return ClassHelper.forNameWithCallerClassLoader(qualifiedClassName, getClass());
            } catch (ClassNotFoundException nf) {
                return super.findClass(qualifiedClassName);
            }
        }

        /**
         * Adds the.
         *
         * @param qualifiedClassName the qualified class name
         * @param javaFile           the java file
         */
        void add(final String qualifiedClassName, final JavaFileObject javaFile) {
            classes.put(qualifiedClassName, javaFile);
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
         */
        @Override
        protected synchronized Class<?> loadClass(final String name, final boolean resolve)
                throws ClassNotFoundException {
            return super.loadClass(name, resolve);
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.ClassLoader#getResourceAsStream(java.lang.String)
         */
        @Override
        public InputStream getResourceAsStream(final String name) {
            if (name.endsWith(ClassUtils.CLASS_EXTENSION)) {
                String qualifiedClassName =
                        name.substring(0, name.length() - ClassUtils.CLASS_EXTENSION.length()).replace('/', '.');
                JavaFileObjectImpl file = (JavaFileObjectImpl) classes.get(qualifiedClassName);
                if (file != null) {
                    return new ByteArrayInputStream(Objects.requireNonNull(file.getByteCode()));
                }
            }
            return super.getResourceAsStream(name);
        }
    }

    /**
     * The Class JavaFileObjectImpl.
     */
    private static final class JavaFileObjectImpl extends SimpleJavaFileObject {

        /** The bytecode. */
        private ByteArrayOutputStream bytecode;

        /** The source. */
        private final CharSequence source;

        /**
         * Instantiates a new java file object impl.
         *
         * @param baseName the base name
         * @param source   the source
         */
        public JavaFileObjectImpl(final String baseName, final CharSequence source) {
            super(ClassUtils.toURI(baseName + ClassUtils.JAVA_EXTENSION), Kind.SOURCE);
            this.source = source;
        }

        /**
         * Instantiates a new java file object impl.
         *
         * @param name the name
         * @param kind the kind
         */
        JavaFileObjectImpl(final String name, final Kind kind) {
            super(ClassUtils.toURI(name), kind);
            source = null;
        }

        /**
         * Instantiates a new java file object impl.
         *
         * @param uri  the uri
         * @param kind the kind
         */
        public JavaFileObjectImpl(URI uri, Kind kind) {
            super(uri, kind);
            source = null;
        }

        /*
         * (non-Javadoc)
         *
         * @see javax.tools.SimpleJavaFileObject#getCharContent(boolean)
         */
        @Override
        public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws UnsupportedOperationException {
            if (source == null) {
                throw new UnsupportedOperationException("source == null");
            }
            return source;
        }

        /*
         * (non-Javadoc)
         *
         * @see javax.tools.SimpleJavaFileObject#openInputStream()
         */
        @Override
        public InputStream openInputStream() {
            return new ByteArrayInputStream(Objects.requireNonNull(getByteCode()));
        }

        /*
         * (non-Javadoc)
         *
         * @see javax.tools.SimpleJavaFileObject#openOutputStream()
         */
        @Override
        public OutputStream openOutputStream() {
            return bytecode = new ByteArrayOutputStream();
        }

        /**
         * Gets the byte code.
         *
         * @return the byte code
         */
        public byte[] getByteCode() {
            if (bytecode == null) {
                return null;
            }
            return bytecode.toByteArray();
        }
    }

    /**
     * The Class JavaFileManagerImpl.
     */
    private static final class JavaFileManagerImpl extends ForwardingJavaFileManager<JavaFileManager> {

        /** The class loader. */
        private final ClassLoaderImpl classLoader;

        /** The file objects. */
        private final Map<URI, JavaFileObject> fileObjects = new HashMap<>();

        /**
         * Instantiates a new java file manager impl.
         *
         * @param fileManager the file manager
         * @param classLoader the class loader
         */
        public JavaFileManagerImpl(JavaFileManager fileManager, ClassLoaderImpl classLoader) {
            super(fileManager);
            this.classLoader = classLoader;
        }

        /*
         * (non-Javadoc)
         *
         * @see javax.tools.ForwardingJavaFileManager#getFileForInput(javax.tools.JavaFileManager.Location,
         * java.lang.String, java.lang.String)
         */
        @Override
        public FileObject getFileForInput(Location location, String packageName, String relativeName)
                throws IOException {
            FileObject o = fileObjects.get(uri(location, packageName, relativeName));
            if (o != null) {
                return o;
            }
            return super.getFileForInput(location, packageName, relativeName);
        }

        /**
         * Put file for input.
         *
         * @param location     the location
         * @param packageName  the package name
         * @param relativeName the relative name
         * @param file         the file
         */
        public void putFileForInput(StandardLocation location, String packageName, String relativeName,
                                    JavaFileObject file) {
            fileObjects.put(uri(location, packageName, relativeName), file);
        }

        /**
         * Uri.
         *
         * @param location     the location
         * @param packageName  the package name
         * @param relativeName the relative name
         * @return the uri
         */
        private URI uri(Location location, String packageName, String relativeName) {
            return ClassUtils.toURI(location.getName() + '/' + packageName + '/' + relativeName);
        }

        /*
         * (non-Javadoc)
         *
         * @see javax.tools.ForwardingJavaFileManager#getJavaFileForOutput(javax.tools.JavaFileManager.Location,
         * java.lang.String, javax.tools.JavaFileObject.Kind, javax.tools.FileObject)
         */
        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String qualifiedName, Kind kind,
                                                   FileObject outputFile) throws IOException {
            JavaFileObject file = new JavaFileObjectImpl(qualifiedName, kind);
            classLoader.add(qualifiedName, file);
            return file;
        }

        /*
         * (non-Javadoc)
         *
         * @see javax.tools.ForwardingJavaFileManager#getClassLoader(javax.tools.JavaFileManager.Location)
         */
        @Override
        public ClassLoader getClassLoader(Location location) {
            return classLoader;
        }

        /*
         * (non-Javadoc)
         *
         * @see javax.tools.ForwardingJavaFileManager#inferBinaryName(javax.tools.JavaFileManager.Location,
         * javax.tools.JavaFileObject)
         */
        @Override
        public String inferBinaryName(Location loc, JavaFileObject file) {
            if (file instanceof JavaFileObjectImpl) {
                return file.getName();
            }
            return super.inferBinaryName(loc, file);
        }

        /*
         * (non-Javadoc)
         *
         * @see javax.tools.ForwardingJavaFileManager#list(javax.tools.JavaFileManager.Location, java.lang.String,
         * java.util.Set, boolean)
         */
        @Override
        public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse)
                throws IOException {
            Iterable<JavaFileObject> result = super.list(location, packageName, kinds, recurse);

            ArrayList<JavaFileObject> files = new ArrayList<JavaFileObject>();

            if (location == StandardLocation.CLASS_PATH && kinds.contains(Kind.CLASS)) {
                for (JavaFileObject file : fileObjects.values()) {
                    if (file.getKind() == Kind.CLASS && file.getName().startsWith(packageName)) {
                        files.add(file);
                    }
                }

                files.addAll(classLoader.files());
            } else if (location == StandardLocation.SOURCE_PATH && kinds.contains(Kind.SOURCE)) {
                for (JavaFileObject file : fileObjects.values()) {
                    if (file.getKind() == Kind.SOURCE && file.getName().startsWith(packageName)) {
                        files.add(file);
                    }
                }
            }

            for (JavaFileObject file : result) {
                files.add(file);
            }

            return files;
        }
    }

    private static void listFiles(String classesPath, final List<File> list, final Set<String> filters) {
        Collection<File> listFiles = FileUtils.listFiles(new File(classesPath), new IOFileFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return filters.contains(name);
            }

            @Override
            public boolean accept(File file) {
                return filters.contains(file.getName());
            }
        }, new IOFileFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return true;
            }

            @Override
            public boolean accept(File file) {
                return true;
            }
        });

        for (File f : listFiles) {
            try {
                list.add(f); // add jar file
            } catch (Exception e) {
                LOGGER.warn(e.getMessage());
            }
        }
    }
}

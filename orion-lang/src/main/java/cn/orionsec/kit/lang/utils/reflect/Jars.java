/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang.utils.reflect;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.io.Files1;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 反射 jar工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/15 13:17
 */
public class Jars {

    private Jars() {
    }

    /**
     * 获取 target 目录 URL
     *
     * @return targetUrl / thisJar
     */
    public static URL getTargetUrl() {
        URL resource = Methods.class.getClassLoader().getResource(Strings.EMPTY);
        if (resource == null) {
            resource = Methods.class.getResource(Strings.EMPTY);
        }
        return resource;
    }

    /**
     * 获取当前jar文件, 如果不是jar环境返回 null
     *
     * @return 当前 jarFile
     */
    public static JarFile getJarFile() {
        URL targetUrl = getTargetUrl();
        if (targetUrl != null && Const.SUFFIX_JAR.equals(targetUrl.getProtocol())) {
            try {
                return ((JarURLConnection) targetUrl.openConnection()).getJarFile();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取jar文件
     *
     * @param file jar文件
     * @return jarFile
     */
    public static JarFile getJarFile(File file) {
        try {
            return new JarFile(file);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取jar文件
     *
     * @param url url 支持jar file
     * @return jarFile
     */
    public static JarFile getJarFile(URL url) {
        String protocol = url.getProtocol();
        try {
            switch (protocol) {
                case Const.SUFFIX_JAR:
                    return ((JarURLConnection) url.openConnection()).getJarFile();
                case Const.SUFFIX_FILE:
                    return new JarFile(url.getFile());
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从jar获取所有类
     *
     * @param jarPath jar
     * @return 类名
     */
    public static List<String> getClassByJar(String jarPath) {
        Valid.notBlank(jarPath, "jar path is null");
        try (JarFile jarFile = new JarFile(jarPath)) {
            return getClassByJar(jarFile);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    /**
     * 从jar获取所有类
     *
     * @param jarFile jar
     * @return 类名
     */
    public static List<String> getClassByJar(JarFile jarFile) {
        Valid.notNull(jarFile, "jar file is null");
        List<String> classes = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            String className = entries.nextElement().getName();
            if (className.endsWith(".class")) {
                classes.add(className.replaceAll("/", ".").substring(0, className.lastIndexOf(".")));
            }
        }
        return classes;
    }

    /**
     * 从jar中获取除class的所有文件
     *
     * @param jarPath jar
     * @return 资源名称
     */
    public static List<String> getSourceByJar(String jarPath) {
        Valid.notBlank(jarPath, "jar path is null");
        List<String> sources = new ArrayList<>();
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                String fileName = entries.nextElement().getName();
                if (!fileName.endsWith(".class") && !fileName.endsWith("/")) {
                    sources.add(Files1.getPath(jarPath + "!/" + fileName));
                }
            }
            return sources;
        } catch (IOException e) {
            return sources;
        }
    }

    /**
     * 从jar中获取除class的所有文件
     *
     * @param jarFile jar
     * @return 资源名称
     */
    public static List<String> getSourceByJar(JarFile jarFile) {
        Valid.notNull(jarFile, "jar file is null");
        List<String> sources = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            String fileName = entries.nextElement().getName();
            if (!fileName.endsWith(".class") && !fileName.endsWith("/")) {
                sources.add("/" + fileName);
            }
        }
        return sources;
    }

    /**
     * 从jar中获取文件
     *
     * @param jarPath jar
     * @param suffix  后缀
     * @return 资源名称
     */
    public static List<String> getSourceByJar(String jarPath, String suffix) {
        Valid.notBlank(jarPath, "jar path is null");
        List<String> sources = new ArrayList<>();
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                String fileName = entries.nextElement().getName();
                if (suffix != null && fileName.endsWith(suffix) && !fileName.endsWith("/")) {
                    sources.add(Files1.getPath(jarPath + "!/" + fileName));
                }
            }
            return sources;
        } catch (IOException e) {
            return sources;
        }
    }

    /**
     * 从jar中获取文件
     *
     * @param jarFile jar
     * @param suffix  后缀
     * @return 资源名称
     */
    public static List<String> getSourceByJar(JarFile jarFile, String suffix) {
        Valid.notNull(jarFile, "jar file is null");
        List<String> sources = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            String fileName = entries.nextElement().getName();
            if (suffix != null && fileName.endsWith(suffix) && !fileName.endsWith("/")) {
                sources.add("/" + fileName);
            }
        }
        return sources;
    }

}
